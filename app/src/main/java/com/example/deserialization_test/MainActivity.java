package com.example.deserialization_test;
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
import java.util.List;
import java.util.UUID;

import wave_test.SineWaveData;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity{

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

        // When BT is clicked, showdevicedialog
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

        // Timed task for graphupdate
        updateTask = new Runnable() {
            @Override
            public void run() {
                // New data
                if (hasNewData) {
                    updateGraph(lastData);
                    hasNewData = false; // Reset flag
                } else {
                    // No new data
                    if (lastData != null) {
                        lastData.setTime(lastData.getTime() + 1000); // No updata but time moves on
                        updateGraph(lastData);
                    }
                }
                // Update per second
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(updateTask, 1000);

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
                // Exclude BLE
                if (device.getType() != BluetoothDevice.DEVICE_TYPE_LE && device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    if (!bluetoothDevices.contains(device)) {
                        bluetoothDevices.add(device);
                        String deviceInfo = device.getName() + "\n" + device.getAddress();
                        devicesArrayAdapter.add(deviceInfo);
                        Log.d("MainActivity", "Paired non-BLE device added: " + deviceInfo);
                        devicesArrayAdapter.notifyDataSetChanged();
                    }
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
                    if(btnConnect != null && btDeviceTextView != null) {
                        btnConnect.setText("Connect");
                        btDeviceTextView.setText("Please select a device");
                    }
                });
            }
        }
    };


    @SuppressLint("MissingPermission")
    private void discoverDevices() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
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



    private class ConnectThread extends Thread {
        // BT connection establishment
        private final BluetoothSocket mmSocket;

        @SuppressLint("MissingPermission")
        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        @SuppressLint("MissingPermission")
        public void run() {
            bluetoothAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
                bluetoothSocket = mmSocket; // Save socket for later usage
                manageConnectedSocket(mmSocket);
                runOnUiThread(() -> {
                    if(btnConnect != null && btDeviceTextView != null) {
                        btnConnect.setText("Disconnect");
                        btDeviceTextView.setText("Connected device: " + selectedBluetoothDevice.getName());
                        isConnected = true; // Update state
                    }
                });
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
            }
        }

    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        // Pass the socket to a new thread responsible for managing the connection
        ConnectedThread connectedThread = new ConnectedThread(socket);
        connectedThread.start();
    }

    private class ConnectedThread extends Thread { //used to receive data after connection
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

                        // UI update should run on the UI thread)
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


    //Updating graph
    private final Handler handler = new Handler();
    private Runnable updateTask;
    private SineWaveData lastData;
    private boolean hasNewData = false;
    private void updateGraph(SineWaveData data) {
        SineWaveView sineWaveView = findViewById(R.id.sineWaveView);
        sineWaveView.addData(data);
    }


    private class DisconnectThread extends Thread {
        // BT disconnection
        public void run() {
            try {
                if (bluetoothSocket != null) {
                    bluetoothSocket.close();
                    bluetoothSocket = null;
                    runOnUiThread(() -> {
                        if(btnConnect != null && btDeviceTextView != null) {
                            btnConnect.setText("Connect");
                            btDeviceTextView.setText("Please select a device");
                            isConnected = false; // Update connection state
                        }
                    });
                }
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
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
