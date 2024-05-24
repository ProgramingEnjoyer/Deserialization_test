package com.example.deserialization_test;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import wave_test.SineWaveData;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final UUID SERVICE_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E");
    private static final UUID TX_CHARACTERISTIC_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");
    private static final UUID RX_CHARACTERISTIC_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");

    private BlockingQueue<byte[]> dataQueue = new LinkedBlockingQueue<>();
    private Handler handler = new Handler();
    private boolean hasNewData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BT Connection Initialisation
        Button BluetoothButton = findViewById(R.id.BluetoothButton);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBluetoothSupport();

        // Device list Initialisation
        devicesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        // When BT is clicked, show device dialog
        BluetoothButton.setOnClickListener(v -> {
            if (bluetoothAdapter.isEnabled()) {
                discoverDevices();
                showDeviceListDialog();
            } else {
                Toast.makeText(MainActivity.this, "Bluetooth is not enabled", Toast.LENGTH_SHORT).show();
            }
        });

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);

        // Timed task for graph update
        handler.post(updateTask);
    }

    private double[] parseData(byte[] value) {
        // Convert bytes to a list of doubles
        double[] data = new double[10];
        for (int i = 0; i < 10; i++) {
            long bits = 0;
            for (int j = 0; j < 8; j++) {
                bits |= ((long) value[i * 8 + j] & 0xFF) << (8 * (7 - j));
            }
            data[i] = Double.longBitsToDouble(bits);
        }
        return data;
    }

    private void updateViews(double[] data) {
        TangentForceView tangentForceView = findViewById(R.id.tangentForceView);
        EfficiencyView efficiencyView = findViewById(R.id.efficiencyView);
        PushArcView pushArcView = findViewById(R.id.pushArcView);

        tangentForceView.addData((float) data[1], (float) data[6]);
        efficiencyView.addData((float) data[4], (float) data[9]);
        pushArcView.addData((float) data[2], (float) data[7]);
    }


    private void loadData(Uri fileUri) {
        try {
            FileInputStream fileIn = (FileInputStream) getContentResolver().openInputStream(fileUri);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            List<SineWaveData> dataList = (List<SineWaveData>) in.readObject();
            in.close();
            fileIn.close();

            double[] data = new double[dataList.size()];
            for (int i = 0; i < dataList.size(); i++) {
                data[i] = dataList.get(i).getSineValue();
            }

            updateViews(data);
        } catch (FileNotFoundException e) {
            Log.e("MainActivity", "File not found", e);
            Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("MainActivity", "IO Exception", e);
            Toast.makeText(getApplicationContext(), "IO Exception", Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            Log.e("MainActivity", "Class not found", e);
            Toast.makeText(getApplicationContext(), "Class not found", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("MainActivity", "Error loading data", e);
            Toast.makeText(getApplicationContext(), "Error loading data", Toast.LENGTH_SHORT).show();
        }
    }

    // BLE Connection

    // Bluetooth Connection
    private BluetoothAdapter bluetoothAdapter;

    private BluetoothSocket bluetoothSocket = null;
    private static final int REQUEST_BLUETOOTH_SCAN_PERMISSION = 1;
    private final ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private ArrayAdapter<String> devicesArrayAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private static final String TAG = "BluetoothConnection";
    private BluetoothDevice selectedBluetoothDevice;

    // UI update for BT connection & disconnection
    private Button btnConnect, btnCancel;
    private TextView btDeviceTextView;
    private boolean isConnected = false;

    @SuppressLint("MissingPermission")
    private void checkBluetoothSupport() {
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
            finish(); // End application
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Include BLE
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    bluetoothDevices.add(device);
                    String deviceInfo = device.getName() + "\n" + device.getAddress();
                    devicesArrayAdapter.add(deviceInfo);
                    Log.d("MainActivity", "Device added: " + deviceInfo);
                    devicesArrayAdapter.notifyDataSetChanged();
                }
            }
        }
    };

    private void setFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                // Connected
                Toast.makeText(context, device.getName() + " is connected", Toast.LENGTH_LONG).show();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                // Disconnected
                Toast.makeText(context, device.getName() + " is disconnected", Toast.LENGTH_LONG).show();
                // Update UI
                runOnUiThread(() -> {
                    isConnected = false;
                    selectedBluetoothDevice = null;
                    if (btnConnect != null && btDeviceTextView != null) {
                        btnConnect.setText("Connect");
                        btDeviceTextView.setText("Please select a device");
                    }
                });
            }
        }
    };

    @SuppressLint("MissingPermission")
    private void discoverDevices() {
        // Clear the previous list of devices
        bluetoothDevices.clear();
        devicesArrayAdapter.clear();

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery(); // Cancel any existing discovery process
        }

        // Start discovery for classic Bluetooth devices and BLE devices
        bluetoothAdapter.startDiscovery();
        bluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Add the device if it's not already in the list to avoid duplicates
                        if (!bluetoothDevices.contains(device)) {
                            bluetoothDevices.add(device);
                            String deviceInfo = device.getName() + "\n" + device.getAddress();
                            devicesArrayAdapter.add(deviceInfo);
                            Log.d("MainActivity", "Adding BLE device: " + deviceInfo);
                            devicesArrayAdapter.notifyDataSetChanged(); // Update the ListView with new data
                        }
                    }
                });
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void showDeviceListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.bsheet_bluetooth, null);
        builder.setView(dialogView);

        btnConnect = dialogView.findViewById(R.id.bt_connect);
        btnCancel = dialogView.findViewById(R.id.bt_cancel);
        btDeviceTextView = dialogView.findViewById(R.id.btDevice);

        // Update UI according to connection state
        if (isConnected && selectedBluetoothDevice != null) {
            btDeviceTextView.setText("Connected device: " + selectedBluetoothDevice.getName());
        } else if (selectedBluetoothDevice != null) {
            btDeviceTextView.setText("Selected device: " + selectedBluetoothDevice.getName());
        } else {
            btDeviceTextView.setText("Please select a device");
        }

        // Update text in UI
        btnConnect.setText(isConnected ? "Disconnect" : "Connect");

        AlertDialog dialog = builder.create();

        ListView listView = dialogView.findViewById(R.id.device_list);
        listView.setAdapter(devicesArrayAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            selectedBluetoothDevice = bluetoothDevices.get(position);
            btDeviceTextView.setText("Selected device: " + selectedBluetoothDevice.getName());
        });

        btnConnect.setOnClickListener(v -> {
            if (isConnected) {
                new DisconnectThread().start();
            } else {
                if (selectedBluetoothDevice != null) {
                    new ConnectThread(selectedBluetoothDevice).start();
                }
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Cancel handler
        handler.removeCallbacks(updateTask);
        // Deregister the broadcast receiver of the discovery device
        unregisterReceiver(receiver);
        // Deregistering the radio receiver for Bluetooth connection status changes
        unregisterReceiver(mReceiver);
        // Release GATT
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            bluetoothGatt = null;
        }

    }

    private BluetoothGatt bluetoothGatt; // Used in ConnectThread & DisconnectThread

    private class ConnectThread extends Thread {
        private final BluetoothDevice mmDevice; // BluetoothDevice 变量

        public ConnectThread(BluetoothDevice device) {
            this.mmDevice = device;
        }

        @SuppressLint("MissingPermission")
        public void run() {
            if (mmDevice.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                // BLE设备，使用GATT连接
                bluetoothAdapter.cancelDiscovery();
                bluetoothGatt = mmDevice.connectGatt(MainActivity.this, false, new BluetoothGattCallback() {
                    @Override
                    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                        super.onConnectionStateChange(gatt, status, newState);
                        if (newState == BluetoothProfile.STATE_CONNECTED) {
                            Log.i(TAG, "Connected to GATT server.");
                            gatt.discoverServices(); // 开始服务发现

                            runOnUiThread(() -> {
                                if (btnConnect != null && btDeviceTextView != null) {
                                    btnConnect.setText("Disconnect");
                                    btDeviceTextView.setText("Connected device: " + mmDevice.getName());
                                    isConnected = true;
                                }
                            });
                        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                            Log.i(TAG, "Disconnected from GATT server.");

                            // 处理重新连接尝试
                            if (status != BluetoothGatt.GATT_SUCCESS) {
                                handleReconnection(gatt);
                            } else {
                                closeGatt(gatt);
                            }

                            runOnUiThread(() -> {
                                if (btnConnect != null && btDeviceTextView != null) {
                                    btnConnect.setText("Connect");
                                    btDeviceTextView.setText("Please select a device");
                                    isConnected = false;
                                }
                            });
                        }
                    }

                    private void handleReconnection(BluetoothGatt gatt) {
                        // Close current GATT
                        closeGatt(gatt);
                        runOnUiThread(() -> {
                            Toast.makeText(MainActivity.this, "Reconnecting...", Toast.LENGTH_SHORT).show();
                        });
                        bluetoothGatt = mmDevice.connectGatt(MainActivity.this, false, this);
                    }

                    @Override
                    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                        super.onServicesDiscovered(gatt, status);
                        String message;
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            BluetoothGattService service = gatt.getService(SERVICE_UUID);
                            if (service != null) {
                                BluetoothGattCharacteristic txCharacteristic = service.getCharacteristic(TX_CHARACTERISTIC_UUID);
                                BluetoothGattCharacteristic rxCharacteristic = service.getCharacteristic(RX_CHARACTERISTIC_UUID);
                                if (txCharacteristic != null && rxCharacteristic != null) {
                                    gatt.setCharacteristicNotification(txCharacteristic, true);
                                    BluetoothGattDescriptor descriptor = txCharacteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                                    if (descriptor != null) {
                                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                        gatt.writeDescriptor(descriptor);
                                    }
                                    message = "Service and characteristics UUID found and notification enabled.";
                                } else {
                                    message = "Characteristics UUID not found.";
                                }
                            } else {
                                message = "Service UUID not found.";
                            }
                        } else {
                            message = "onServicesDiscovered received: " + status;
                        }
                        Log.i(TAG, message);
                        runOnUiThread(() -> showAlertDialog("Service Discovery", message));
                    }

                    @Override
                    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                        super.onCharacteristicRead(gatt, characteristic, status);
                        String message;
                        if (status == BluetoothGatt.GATT_SUCCESS) {
                            String hexValue = bytesToHex(characteristic.getValue());
                            Log.i(TAG, "Characteristic value read: " + hexValue);
                            message = "Characteristic value read: " + hexValue;
                            handleCharacteristicRead(characteristic.getValue());
                        } else {
                            message = "Failed to read characteristic value with status: " + status;
                        }
                        runOnUiThread(() -> showAlertDialog("Characteristic Read", message));
                    }

                    @Override
                    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                        super.onCharacteristicChanged(gatt, characteristic);
                        byte[] value = characteristic.getValue();
                        Log.d(TAG, "Received data: " + bytesToHex(value));

                        double[] data = parseData(value);
                        runOnUiThread(() -> updateViews(data));
                    }

                    private String bytesToHex(byte[] bytes) {
                        StringBuilder sb = new StringBuilder();
                        for (byte b : bytes) {
                            sb.append(String.format("%02X ", b));
                        }
                        return sb.toString().trim();
                    }

                    private void handleCharacteristicRead(byte[] value) {
                        runOnUiThread(() -> {
                            // update UI
                            double[] data = parseData(value);
                            updateViews(data);
                        });
                    }

                    private void closeGatt(BluetoothGatt gatt) {
                        if (gatt != null) {
                            gatt.disconnect();
                            gatt.close();
                        }
                    }
                });
            } else {
                // Classical BT
            }
        }
    }


    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        // Pass the socket to a new thread responsible for managing the connection
        ConnectedThread connectedThread = new ConnectedThread(socket);
        connectedThread.start();
    }

    private class ConnectedThread extends Thread { // used to receive data after connection
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;

            // Get the input stream of the Bluetooth socket
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }

            mmInStream = tmpIn;
        }

        public void run() {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mmInStream));
            while (true) {
                try {
                    // Read the next line of the input stream
                    String receivedData = reader.readLine();
                    if (receivedData != null && !receivedData.isEmpty()) {
                        // Parse the received data
                        byte[] bytes = receivedData.getBytes();
                        double[] data = parseData(bytes);

                        // UI update should run on the UI thread
                        runOnUiThread(() -> updateViews(data));
                    }
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break; // End thread
                }
            }
        }


        // Thread cancellation
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    // Updating graph
    private Runnable updateTask = new Runnable() {
        @Override
        public void run() {
            byte[] value;
            while ((value = dataQueue.poll()) != null) {
                // Handle each data value from the queue
                double[] data = parseData(value);
                updateViews(data);
            }
            handler.postDelayed(this, 1000); // Update every second
        }
    };

    private class DisconnectThread extends Thread {
        public void run() {
            if (bluetoothGatt != null) {
                bluetoothGatt.disconnect(); // Disconnect GATT connection
                bluetoothGatt.close(); // Release resources
                bluetoothGatt = null; // Clear reference
                runOnUiThread(() -> {
                    if (btnConnect != null && btDeviceTextView != null) {
                        btnConnect.setText("Connect");
                        btDeviceTextView.setText("Please select a device");
                        isConnected = false; // Update connection status
                    }
                });
            } else if (bluetoothSocket != null) {
                try {
                    bluetoothSocket.close(); // Close classic Bluetooth connection
                    bluetoothSocket = null;
                    runOnUiThread(() -> {
                        if (btnConnect != null && btDeviceTextView != null) {
                            btnConnect.setText("Connect");
                            btDeviceTextView.setText("Please select a device");
                            isConnected = false; // Update connection status
                        }
                    });
                } catch (IOException e) {
                    Log.e(TAG, "Could not close the client socket", e);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_SCAN_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission declined
                Toast.makeText(this, "Bluetooth scan permission is required to discover devices", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
