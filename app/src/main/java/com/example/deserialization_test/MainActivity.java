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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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
import android.content.DialogInterface;
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
import java.util.Arrays;
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
    private SineWaveData lastData;
    private boolean hasNewData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Load data Initialisation
        Button loadDataButton = findViewById(R.id.loadDataButton);
        loadDataButton.setOnClickListener(v -> loadData());

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

    // Load data functions
    private final ActivityResultLauncher<Intent> openDocument = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    loadData(uri);
                }
            });

    private void loadData() {
        // Load data from .ser file in phone
        if (!hasManageExternalStoragePermission()) {
            requestManageExternalStoragePermission();
        } else {
            // Open selector
            openFilePicker();
        }
    }

    private boolean hasManageExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        }
        return true; // Before Android 11, no need for the manage external storage permission.
    }

    private void requestManageExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        } else {
            // No action needed for versions before Android 11.
            openFilePicker();
        }
    }

    public void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        openDocument.launch(intent);
    }

    public void loadData(Uri fileUri) {
        StringBuilder dataString = new StringBuilder();

        try {
            FileInputStream fileIn = (FileInputStream) getContentResolver().openInputStream(fileUri);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            List<SineWaveData> dataList = (List<SineWaveData>) in.readObject();
            in.close();
            fileIn.close();

            for (SineWaveData data : dataList) {
                dataString.append("Time: ").append(data.getTime())
                        .append(", Sine Value: ").append(data.getSineValue()).append("\n");
            }
            SineWaveView sineWaveView = findViewById(R.id.sineWaveView);
            sineWaveView.setDataList(dataList);

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
    private void updateGraph(SineWaveData data) {
        SineWaveView sineWaveView = findViewById(R.id.sineWaveView);
        sineWaveView.addData(data);
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
    }

    private BluetoothGatt bluetoothGatt; // Used in ConnectThread & DisconnectThread

    private class ConnectThread extends Thread {
        private final BluetoothDevice mmDevice; // BluetoothDevice variable

        public ConnectThread(BluetoothDevice device) {
            this.mmDevice = device;
        }

        @SuppressLint("MissingPermission")
        public void run() {
            if (mmDevice.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                // BLE device, use GATT to connect
                bluetoothAdapter.cancelDiscovery();
                bluetoothGatt = mmDevice.connectGatt(MainActivity.this, false, new BluetoothGattCallback() {
                    @Override
                    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                        super.onConnectionStateChange(gatt, status, newState);
                        if (newState == BluetoothProfile.STATE_CONNECTED) {
                            Log.i(TAG, "Connected to GATT server.");
                            gatt.discoverServices(); // Start service discovery

                            runOnUiThread(() -> {
                                if (btnConnect != null && btDeviceTextView != null) {
                                    btnConnect.setText("Disconnect");
                                    btDeviceTextView.setText("Connected device: " + mmDevice.getName());
                                    isConnected = true;
                                }
                            });
                        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                            Log.i(TAG, "Disconnected from GATT server.");

                            runOnUiThread(() -> {
                                if (btnConnect != null && btDeviceTextView != null) {
                                    btnConnect.setText("Connect");
                                    btDeviceTextView.setText("Please select a device");
                                    isConnected = false;
                                }
                            });
                        }
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
                                    // Enable notifications for TX characteristic
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
                        dataQueue.add(characteristic.getValue());
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
                            // Update UI or handle data read from characteristic
                            // Example: Parse the data and update the graph
                            try {
                                String receivedData = new String(value);
                                JSONObject json = new JSONObject(receivedData);
                                SineWaveData data = new SineWaveData(json.getDouble("time"), json.getDouble("value"));
                                hasNewData = true;
                                lastData = data;
                                updateGraph(data);
                            } catch (JSONException e) {
                                Log.e(TAG, "Failed to parse data", e);
                            }
                        });
                    }
                });
            } else {
                // Classic Bluetooth, handle differently
            }
        }

        private void handleDisconnect() {
            runOnUiThread(() -> {
                if (btnConnect != null && btDeviceTextView != null) {
                    btnConnect.setText("Connect");
                    btDeviceTextView.setText("Please select a device");
                    isConnected = false;
                }
            });
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
                        // ReceivedData is a string in JSON format
                        // Use JSONObject for parsing and then create the SineWaveData object
                        JSONObject json = new JSONObject(receivedData);
                        SineWaveData data = new SineWaveData(json.getDouble("time"), json.getDouble("value"));

                        // UI update should run on the UI thread
                        runOnUiThread(() -> {
                            hasNewData = true;
                            lastData = data;
                            updateGraph(data);
                        });
                    }
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break; // End thread
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse data", e);
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
                try {
                    String receivedData = new String(value);
                    JSONObject json = new JSONObject(receivedData);
                    SineWaveData data = new SineWaveData(json.getDouble("time"), json.getDouble("value"));
                    hasNewData = true;
                    lastData = data;
                    updateGraph(data);
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse data", e);
                }
            }
            handler.postDelayed(this, 1000); // Update every second
        }
    };

    private class DisconnectThread extends Thread {
        public void run() {
            if (bluetoothGatt != null) {
                bluetoothGatt.disconnect(); // 断开GATT连接
                bluetoothGatt.close(); // 释放资源
                bluetoothGatt = null; // 清除引用
                runOnUiThread(() -> {
                    if (btnConnect != null && btDeviceTextView != null) {
                        btnConnect.setText("Connect");
                        btDeviceTextView.setText("Please select a device");
                        isConnected = false; // 更新连接状态
                    }
                });
            } else if (bluetoothSocket != null) {
                try {
                    bluetoothSocket.close(); // 关闭传统蓝牙连接
                    bluetoothSocket = null;
                    runOnUiThread(() -> {
                        if (btnConnect != null && btDeviceTextView != null) {
                            btnConnect.setText("Connect");
                            btDeviceTextView.setText("Please select a device");
                            isConnected = false; // 更新连接状态
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
