package com.example.deserialization_test;
import android.content.pm.PackageManager;
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

        // BT Initialisation
        Button BluetoothButton = findViewById(R.id.BluetoothButton);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        checkBluetoothSupport();

        // 初始化 ArrayAdapter
        devicesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        // 当蓝牙按钮被点击时显示设备列表的弹窗
        BluetoothButton.setOnClickListener(v -> {
            if (bluetoothAdapter.isEnabled()) {
                discoverDevices();
                showDeviceListDialog(); // 显示带有设备列表的弹窗
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

    private BluetoothSocket bluetoothSocket = null;
    private static final int REQUEST_BLUETOOTH_SCAN_PERMISSION = 1;
    private final ArrayList<BluetoothDevice> bluetoothDevices = new ArrayList<>();
    private ArrayAdapter<String> devicesArrayAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private static final String TAG = "BluetoothConnection";
    private BluetoothDevice selectedBluetoothDevice;


    // UI of BT connection & disconnection
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
                // 检查设备是否是非BLE设备且已经配对
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
                // 设备已连接
                Toast.makeText(context, device.getName() + " is connected", Toast.LENGTH_LONG).show();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                // 设备已断开
                Toast.makeText(context, device.getName() + " is disconnected", Toast.LENGTH_LONG).show();
                // 更新UI
                runOnUiThread(() -> {
                    // 这里假设btnConnect和btDeviceTextView已经在showDeviceListDialog中初始化
                    isConnected = false;
                    selectedBluetoothDevice = null; // 如果不再跟踪断开连接的设备
                    // 可以选择不重置selectedBluetoothDevice，这样用户重新打开对话框时仍然显示之前选中的设备

                    // 这里需要确保这些UI组件已经初始化了
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

        // 根据当前状态更新文本视图
        if (isConnected && selectedBluetoothDevice != null) {
            btDeviceTextView.setText("Connected device: " + selectedBluetoothDevice.getName());
        } else if (selectedBluetoothDevice != null) {
            btDeviceTextView.setText("Selected device: " + selectedBluetoothDevice.getName());
        } else {
            btDeviceTextView.setText("Please select a device");
        }

        // 更新连接/断开按钮文本
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
        // 注销发现设备的广播接收器
        unregisterReceiver(receiver);
        // 注销蓝牙连接状态变化的广播接收器
        unregisterReceiver(mReceiver);
    }


    private class ConnectThread extends Thread { //used to establish connection
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
                bluetoothSocket = mmSocket; // 保存对socket的引用以便以后使用
                manageConnectedSocket(mmSocket);
                // 更新UI以反映连接状态
                runOnUiThread(() -> {
                    if(btnConnect != null && btDeviceTextView != null) {
                        btnConnect.setText("Disconnect");
                        btDeviceTextView.setText("Connected device: " + selectedBluetoothDevice.getName());
                        isConnected = true; // 更新连接状态
                    }
                });
                // Connection was successful. You can now manage your connection.

            } catch (IOException connectException) {
                // 连接失败的处理
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
            }
        }

    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        // 传递socket到一个新的线程，负责管理连接
        ConnectedThread connectedThread = new ConnectedThread(socket);
        connectedThread.start();
        // 可以在这里保存connectedThread的引用，以便于后续发送数据
    }

    private class ConnectedThread extends Thread { //used to receive data after connection
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;

            // 获取蓝牙socket的输入流
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
                    // 读取输入流的下一行
                    String receivedData = reader.readLine();
                    if (receivedData != null && !receivedData.isEmpty()) {
                        // 在这里，我们假设receivedData是JSON格式的字符串
                        // 使用JSONObject进行解析，然后创建SineWaveData对象
                        JSONObject json = new JSONObject(receivedData);
                        SineWaveData data = new SineWaveData(json.getDouble("time"), json.getDouble("value"));

                        // 更新UI（请记住，在UI线程上运行此操作）
                        runOnUiThread(() -> updateGraph(data));
                    }
                } catch (IOException e) {
                    Log.d(TAG, "Input stream was disconnected", e);
                    break; // 退出循环，结束线程
                } catch (JSONException e) {
                    Log.e(TAG, "Failed to parse data", e);
                }
            }
        }

        // 调用此方法来关闭线程
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    // 在UI线程上调用此方法来更新图表
    private void updateGraph(SineWaveData data) {
        SineWaveView sineWaveView = findViewById(R.id.sineWaveView);
        sineWaveView.addData(data); // 假设SineWaveView有一个addData方法来更新图表
    }

// 其他 MainActivity 方法...


    private class DisconnectThread extends Thread {   //disconnect logic
        public void run() {
            try {
                if (bluetoothSocket != null) {
                    bluetoothSocket.close();
                    bluetoothSocket = null;
                    runOnUiThread(() -> {
                        if(btnConnect != null && btDeviceTextView != null) {
                            btnConnect.setText("Connect");
                            btDeviceTextView.setText("Please select a device");
                            isConnected = false; // 更新连接状态
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
                // 权限被授予，可以继续蓝牙扫描
            } else {
                // 权限被拒绝，向用户解释为什么需要这个权限
                Toast.makeText(this, "Bluetooth scan permission is required to discover devices", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
