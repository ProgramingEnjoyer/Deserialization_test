package com.example.deserialization_test;
import android.content.pm.PackageManager;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import wave_test.SineWaveData;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load data Initialisation
        Button loadDataButton = findViewById(R.id.loadDataButton);
        loadDataButton.setOnClickListener(v -> loadData());

        //BT Initialisation
        Button BluetoothButton = findViewById(R.id.BluetoothButton);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBluetoothSupport();

        //List Initialisation
        ListView deviceList = findViewById(R.id.device_list);
        // 只初始化一次
        devicesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        deviceList.setAdapter(devicesArrayAdapter);
        deviceList.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice selectedDevice = bluetoothDevices.get(position);
            // 连接设备的代码
            ConnectThread connectThread = new ConnectThread(selectedDevice);
            connectThread.start();
        });

        BluetoothButton.setOnClickListener(v -> {
            if (bluetoothAdapter.isEnabled()) {
                deviceList.setVisibility(View.VISIBLE);
                discoverDevices();
            } else {
                Toast.makeText(MainActivity.this, "Bluetooth is not enabled", Toast.LENGTH_SHORT).show();
            }
        });

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
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


//Bluetooth Connection
private BluetoothAdapter bluetoothAdapter;
    private static final int REQUEST_BLUETOOTH_SCAN_PERMISSION = 1;
    private final ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private ArrayAdapter<String> devicesArrayAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private static final String TAG = "BluetoothConnection";


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
                if (!bluetoothDevices.contains(device)) {
                    bluetoothDevices.add(device);
                    String deviceInfo = device.getName() + "\n" + device.getAddress();
                    devicesArrayAdapter.add(deviceInfo);
                    Log.d("MainActivity", "Device added: " + deviceInfo);
                    devicesArrayAdapter.notifyDataSetChanged();
                }
            }
        }
    };



    @SuppressLint("MissingPermission")
    private void discoverDevices() {
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
        showDeviceListDialog();
    }

    private void showDeviceListDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View dialogView = getLayoutInflater().inflate(R.layout.listview, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        ListView listViewDevices = dialogView.findViewById(R.id.listViewDevices);
        listViewDevices.setAdapter(devicesArrayAdapter);
        listViewDevices.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice selectedDevice = bluetoothDevices.get(position);
            ConnectThread connectThread = new ConnectThread(selectedDevice);
            connectThread.start();
            dialog.dismiss(); // 设备选择后关闭弹窗
        });

        dialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private class ConnectThread extends Thread {
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
                // Connection was successful. You can now manage your connection (e.g., perform I/O) here.
                // This could include starting another thread to manage I/O or passing the socket to another service.
            } catch (IOException connectException) {
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // Manage the connection in a separate method.
            manageConnectedSocket(mmSocket);
        }

        private void manageConnectedSocket(BluetoothSocket mmSocket) {
            // Once a connection has been made, this method is responsible for managing the connection.
            // This could involve setting up streams to read from and write to the socket,
            // and starting a separate thread or service to perform read/write operations.
            // Note: It's essential to perform I/O operations on a separate thread from the UI to avoid freezing the app.
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
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
                // 权限被授予，可以继续蓝牙扫描
            } else {
                // 权限被拒绝，向用户解释为什么需要这个权限
                Toast.makeText(this, "Bluetooth scan permission is required to discover devices", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
