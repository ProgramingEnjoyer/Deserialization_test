
# Developer Diary

## [Date: 2024-03-14]

### Today's Tasks

- Task 1: Simulate and serialize sine wave generation in Java, which later can be integrated into Android Studio directly.

### Detailed Notes
#### Task 1: [Simulate Sine Wave Generation in Java]
Today's primary task involved generating a sine wave in Java. This simulation will be useful for later integration into an Android application. The process includes calculating sine wave values, storing them in a list, and then serializing this list for later use.
##### Steps: 
- Defined key parameters for the sine wave: sample size, frequency, and amplitude.
-   Utilized a loop to compute the sine wave values at discrete time intervals.
-   Created a list to store instances of a custom class `SineWaveData`, which holds the time and corresponding sine wave value.
-   Implemented a method to serialize the list of `SineWaveData` objects and save it to a file named `sineWaveDataList.ser`.

Here’s the implementation in Java:
```java
package wave_test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        final int sampleSize = 100;
        final double frequency = 5;
        final double amplitude = 1;
        final double twoPiF = 2 * Math.PI * frequency;
        List<SineWaveData> dataList = new ArrayList<>(); // Create a list to store data points

        for (int i = 0; i < sampleSize; i++) {
            double time = i / (double) sampleSize;
            double sineValue = amplitude * Math.sin(twoPiF * time);
            SineWaveData data = new SineWaveData(time, sineValue);
            dataList.add(data); // Add into the list
        }

        serializeSineWaveData(dataList); // serialize the whole list
    }

    private static void serializeSineWaveData(List<SineWaveData> dataList) {
        try {
            FileOutputStream fileOut = new FileOutputStream("sineWaveDataList.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(dataList); 
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in sineWaveDataList.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
```
## [Date: 2024-03-15]

### Today's Tasks

-   Task 1: Deserialize sine wave data in Java and verify the deserialized data.

### Detailed Notes

#### Task 1: [Deserialize Sine Wave Data in Java]

Today's primary task involved deserializing the previously serialized sine wave data in Java. This step is crucial for reading the sine wave data back into the application, allowing for verification and potential further processing.

##### Steps:

-   Opened the serialized file `sineWaveDataList.ser` to read the objects list.
-   Deserialized the list of `SineWaveData` objects.
-   Printed each deserialized object's information to verify the data.

Here's the implementation in Java:
```java
package wave_test;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.List;

public class DeserializeSineWave {
    public static void main(String[] args) {
        try {
            // Open the .ser file to read the objects list
            FileInputStream fileIn = new FileInputStream("sineWaveDataList.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            
            // Read and deserialize the objects list
            List<SineWaveData> dataList = (List<SineWaveData>) in.readObject();
            
            // Close the input streams
            in.close();
            fileIn.close();
            
            // Print out each deserialized object's information to verify data
            System.out.println("Deserialized SineWaveData List...");
            for (SineWaveData data : dataList) {
                System.out.println("Time: " + data.getTime() + ", Sine Value: " + data.getSineValue());
            }
            
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("SineWaveData class or List not found");
            c.printStackTrace();
            return;
        }
    }
}

// Class to hold sine wave data points
class SineWaveData implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private double time;
    private double value;

    public SineWaveData(double time, double value) {
        this.time = time;
        this.value = value;
    }

    public double getTime() {
        return time;
    }

    public double getSineValue() {
        return value;
    }
}
```
## [Date: 2024-03-18]

### Today's Tasks

-   Task 1: Initialize and check Bluetooth support in the Android application.
-   Task 2: Implement Bluetooth device discovery and manage connections.
-   Task 3: Integrate data deserialization and display in the Android application.

### Detailed Notes

#### Task 1: [Initialize and Check Bluetooth Support]

Today's primary task involved initializing and checking Bluetooth support in the Android application. This included ensuring the device supports Bluetooth, enabling it if it's not already enabled, and preparing the application for Bluetooth operations.

##### Steps:

-   Checked if the device supports Bluetooth.
-   Requested the user to enable Bluetooth if it was not already enabled.
-   Registered a BroadcastReceiver to listen for newly discovered Bluetooth devices.

Here’s a snippet of the implementation:
```java
private void checkBluetoothSupport() {
    if (bluetoothAdapter == null) {
        Toast.makeText(this, "Bluetooth is not supported on this device", Toast.LENGTH_SHORT).show();
        finish(); // End application
    } else if (!bluetoothAdapter.isEnabled()) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }
}
```
#### Task 2: [Implement Bluetooth Device Discovery and Manage Connections]

The second task focused on discovering nearby Bluetooth devices and managing connections with selected devices.

##### Steps:

-   Started device discovery upon user request.
-   Displayed a list of discovered devices in a dialog.
-   Managed connections using threads for connecting and disconnecting from devices.

Here’s a snippet of the implementation:

```java
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

    // Initialization and setup for Bluetooth device selection
    // ...
    
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
```
#### Task 3: [Integrate Data Deserialization and Display]

The third task was to integrate data deserialization and display it in the Android application. This involved loading serialized sine wave data from a file and displaying it in a custom view.

##### Steps:

-   Implemented functionality to request necessary permissions for accessing external storage.
-   Opened a file picker to select the serialized data file.
-   Deserialized the sine wave data and displayed it in a custom view.

Here’s a snippet of the implementation:
```java
public void loadData(Uri fileUri) {
    try {
        FileInputStream fileIn = (FileInputStream) getContentResolver().openInputStream(fileUri);
        ObjectInputStream in = new ObjectInputStream(fileIn);

        List<SineWaveData> dataList = (List<SineWaveData>) in.readObject();
        in.close();
        fileIn.close();

        // Display data in custom view
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
    }
}
```
### Summary
Today’s tasks focused on enhancing the Android application with Bluetooth support and data deserialization functionality. The Bluetooth initialization and discovery process were set up.

## [Date: 2024-03-19]

### Today's Tasks

-   Task 1: Manage data transmission after successful Bluetooth connection.
-   Task 2: Implement user-initiated disconnection and resource cleanup.
-   Task 3: Handle errors and provide clear user guidance.

### Detailed Notes

#### Task 1: [Manage Data Transmission After Successful Bluetooth Connection]

Today's primary task involved managing data transmission after a successful Bluetooth connection. This included creating input/output streams and handling these streams on separate threads to avoid blocking the UI thread.

##### Steps:

-   Created input and output streams from the Bluetooth socket.
-   Implemented separate threads for reading from and writing to the streams.
-   Ensured continuous data handling without blocking the main UI thread.

Here’s a snippet of the implementation:
```java
private class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when creating input/output stream", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];
        int numBytes;

        while (true) {
            try {
                numBytes = mmInStream.read(buffer);
                // Handle the received bytes
                // For example, update UI or process data
            } catch (IOException e) {
                Log.d(TAG, "Input stream was disconnected", e);
                break;
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.e(TAG, "Error occurred when sending data", e);
        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}
```
#### Task 2: [Implement User-Initiated Disconnection and Resource Cleanup]

The second task focused on implementing a way for the user to actively disconnect from the Bluetooth device and ensure all resources are properly released.

##### Steps:

-   Implemented a button to allow the user to disconnect from the Bluetooth device.
-   Ensured the socket and input/output streams are closed when the user disconnects.
-   Unregistered broadcast receivers and released other resources upon application destruction to prevent memory leaks.

Here’s a snippet of the implementation:
```java
private class DisconnectThread extends Thread {
    public void run() {
        try {
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
                bluetoothSocket = null;
                runOnUiThread(() -> {
                    if (btnConnect != null && btDeviceTextView != null) {
                        btnConnect.setText("Connect");
                        btDeviceTextView.setText("Please select a device");
                        isConnected = false; // Update connection status
                    }
                });
            }
        } catch (IOException e) {
            Log.e(TAG, "Could not close the client socket", e);
        }
    }
}

@Override
protected void onDestroy() {
    super.onDestroy();
    // Unregister discovery broadcast receiver
    unregisterReceiver(receiver);
    // Unregister Bluetooth connection state change broadcast receiver
    unregisterReceiver(mReceiver);
}
```
#### Task 3: [Handle Errors and Provide Clear User Guidance]

The third task was to handle errors and provide clear guidance to the user on how to resolve issues, such as permission requests and error messages.

##### Steps:

-   Implemented error handling for Bluetooth operations.
-   Displayed clear error messages to the user and guided them to resolve issues.
-   Added checks for necessary permissions and provided instructions to the user for granting permissions.

Here’s a snippet of the implementation:
```java
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_BLUETOOTH_SCAN_PERMISSION) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, continue with Bluetooth scanning
        } else {
            // Permission denied, explain to the user
            Toast.makeText(this, "Bluetooth scan permission is required to discover devices", Toast.LENGTH_SHORT).show();
        }
    }
}

private void showErrorDialog(String message) {
    new AlertDialog.Builder(this)
        .setTitle("Error")
        .setMessage(message)
        .setPositiveButton(android.R.string.ok, null)
        .show();
}
```
### Summary

Today’s tasks focused on enhancing the Bluetooth connectivity features of the Android application. Data transmission was handled efficiently using separate threads for input and output streams. User-initiated disconnection was implemented, ensuring that resources are properly released and the application remains responsive. Additionally, robust error handling and clear user guidance were added to improve the user experience and address potential issues proactively.

## [Date: 2024-03-21]

### Today's Tasks

-   Task 1: Design the UI for discovering and connecting Bluetooth devices.
-   Task 2: Integrate the UI design into the existing Bluetooth connection logic.

### Detailed Notes

#### Task 1: [Design the UI for Discovering and Connecting Bluetooth Devices]

Today's primary task involved designing a user interface (UI) for discovering and connecting to Bluetooth devices. This UI allows users to see available devices, initiate connections, and handle ongoing connection states.

##### Steps:

-   Created a layout file `bluetooth_device_list.xml` to define the UI components for device discovery and connection.
-   Included elements such as an ImageView for the Bluetooth logo, a TextView for device selection, a ProgressBar for indicating ongoing operations, a ListView for displaying available devices, and buttons for canceling and connecting to devices.

Here’s the layout of UI:
![UI of device list](https://photos.app.goo.gl/i4PeUWFkCkrZ1FEa9)
#### Task 2: [Integrate the UI Design into the Existing Bluetooth Connection Logic]

The second task focused on integrating the designed UI into the existing Bluetooth connection logic. This included updating the UI based on the connection status and user actions.

##### Steps:

-   Updated the `MainActivity` to inflate the new layout and handle user interactions.
-   Managed the visibility and text of the ProgressBar, TextView, and Buttons based on the Bluetooth connection status.
-   Updated the logic for displaying the list of discovered devices and handling user selections.

Here’s a snippet of the integration in `MainActivity`:
```java
private void showDeviceListDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
    View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.bluetooth_device_list, null);
    builder.setView(dialogView);

    btnConnect = dialogView.findViewById(R.id.bt_connect);
    btnCancel = dialogView.findViewById(R.id.bt_cancel);
    btDeviceTextView = dialogView.findViewById(R.id.btDevice);
    ProgressBar btProgress = dialogView.findViewById(R.id.bt_progress);

    // Initially hide the progress bar
    btProgress.setVisibility(View.INVISIBLE);

    // Update the TextView and Button based on connection status
    if (isConnected && selectedBluetoothDevice != null) {
        btDeviceTextView.setText("Connected device: " + selectedBluetoothDevice.getName());
    } else if (selectedBluetoothDevice != null) {
        btDeviceTextView.setText("Selected device: " + selectedBluetoothDevice.getName());
    } else {
        btDeviceTextView.setText("Please select a device");
    }

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
                btProgress.setVisibility(View.VISIBLE);
                new ConnectThread(selectedBluetoothDevice).start();
            }
        }
    });

    btnCancel.setOnClickListener(v -> dialog.dismiss());
    dialog.show();
}
```
### Summary

Today’s tasks focused on designing and integrating a user-friendly UI for discovering and connecting Bluetooth devices. The new UI provides a clear and intuitive interface for users to manage their Bluetooth connections.

## [Date: 2024-03-22]

### Today's Tasks

-   Task 1: Set up and test a rotary encoder on Arduino.
-   Task 2: Implement signal simulation on Arduino using the rotary encoder.
-   Task 3: Transmit simulated signals from Arduino to an Android phone via HC-05.

### Detailed Notes

#### Task 1: [Set Up and Test Rotary Encoder on Arduino]

Today's primary task involved setting up and testing a rotary encoder on an Arduino. This included wiring the encoder to the Arduino and initializing the necessary pins.

##### Steps:

-   Connected the rotary encoder to Arduino pins: CLK (pin 4), DT (pin 6), and SW (pin 5).
-   Configured the pins in the `setup` function.
-   Initialized the Bluetooth connection using the HC-05 module on pins 10 (RX) and 11 (TX).

Here’s the setup function implementation:
```C++
void setup() {
  Serial.begin(9600);
  myBluetooth.begin(9600);

  pinMode(clkPin, INPUT);
  pinMode(dtPin, INPUT);
  pinMode(swPin, INPUT_PULLUP); // Set the button as input with internal pull-up resistor

  // Initialize all the readings to 0:
  for (int thisReading = 0; thisReading < numReadings; thisReading++) {
    readings[thisReading] = 0;
  }

  previousStateCLK = digitalRead(clkPin);
}
```
#### Task 2: [Implement Signal Simulation on Arduino Using the Rotary Encoder]

The second task focused on simulating signals using the rotary encoder and processing the encoder's output with a moving average filter.

##### Steps:

-   Read the encoder's state changes to calculate the raw encoder value.
-   Applied a moving average filter to smooth the raw encoder values.
-   Implemented a debounce mechanism for the encoder button to reset the values.

Here’s the loop function implementation:
```C++
void loop() {
  currentStateCLK = digitalRead(clkPin);
  buttonState = digitalRead(swPin); // Read the button state

  // Check if button state has changed to LOW (button pressed)
  if (buttonState == LOW && lastButtonState == HIGH) {
    rawEncoderValue = 0; 
    encoderValue = 0; 
    // Debounce delay to avoid accidental quick presses
    delay(50); 
  }
  unsigned long currentMillis = millis(); // Get the current time
  if (currentStateCLK != previousStateCLK) {
    previousMillis = currentMillis;

    if (digitalRead(dtPin) != currentStateCLK) {
      rawEncoderValue++; 
    } else {
      rawEncoderValue--; 
    }

    // Update the moving average filter
    total = total - readings[readIndex];
    readings[readIndex] = rawEncoderValue;
    total = total + readings[readIndex];
    readIndex = readIndex + 1;

    if (readIndex >= numReadings) {
      readIndex = 0; // ...wrap around to the beginning:
    }

    // Calculate the average:
    encoderValue = total / numReadings;

    // Send serialized data over Bluetooth
    myBluetooth.print("{ \"time\": ");
    myBluetooth.print(currentMillis);
    myBluetooth.print(", \"value\": ");
    myBluetooth.print(encoderValue);
    myBluetooth.println(" }");

    // Also print to Serial Monitor
    Serial.print("{ \"time\": ");
    Serial.print(currentMillis);
    Serial.print(", \"value\": ");
    Serial.print(encoderValue);
    Serial.println(" }");

    lastEncoderTime = currentMillis; // Update the last encoder time
  }

  previousStateCLK = currentStateCLK;
  lastButtonState = buttonState; // Update the last button state
  delay(1); // A short delay to reduce noise
}
```
#### Task 3: [Transmit Simulated Signals from Arduino to Android Phone via HC-05]

The third task involved transmitting the simulated signals from the Arduino to an Android phone using the HC-05 Bluetooth module.

##### Steps:

-   Set up the HC-05 Bluetooth module for communication.
-   Serialized the data as JSON and sent it over Bluetooth.
-   Displayed the transmitted data on the Arduino Serial Monitor for verification.

Key parts of the transmission implementation:

```C++
// Send serialized data over Bluetooth
myBluetooth.print("{ \"time\": ");
myBluetooth.print(currentMillis);
myBluetooth.print(", \"value\": ");
myBluetooth.print(encoderValue);
myBluetooth.println(" }");

// Also print to Serial Monitor
Serial.print("{ \"time\": ");
Serial.print(currentMillis);
Serial.print(", \"value\": ");
Serial.print(encoderValue);
Serial.println(" }");
```
### Summary

Today’s tasks focused on setting up and testing a rotary encoder on Arduino, simulating signals using the encoder, and transmitting these signals to an Android phone via the HC-05 Bluetooth module. The integration of a moving average filter helped in smoothing out the encoder values, providing a more stable output for transmission.

## [Date: 2024-03-23]

### Today's Tasks

-   Task 1: Conduct connectivity tests between Arduino and Android device.
-   Task 2: Debug and resolve common connectivity issues.
-   Task 3: Ensure reliable real-time transmission of rotary encoder data.

### Detailed Notes

#### Task 1: [Conduct Connectivity Tests Between Arduino and Android Device]

Today's primary task involved testing the connectivity between the Arduino and the Android device using the HC-05 Bluetooth module. This included verifying that data is being sent from the Arduino and received correctly on the Android device.

##### Steps:

-   Established a Bluetooth connection between the Arduino and Android device.
-   Verified data transmission from the Arduino to the Android device.
-   Monitored the data received on the Android application for accuracy.

#### Task 2: [Debug and Resolve Common Connectivity Issues]

During the connectivity tests, several common issues were identified and resolved. Here are some potential bugs and their solutions:

##### Bug 1: Bluetooth Connection Failure

-   **Symptom:** The Bluetooth connection between the Arduino and Android device fails intermittently.
-   **Solution:** Ensure the Bluetooth module is properly powered and within range. Add retries in the connection logic.
```java
private void connectToBluetoothDevice() {
    new Thread(() -> {
        boolean success = false;
        while (!success) {
            try {
                bluetoothSocket.connect();
                success = true;
            } catch (IOException e) {
                try {
                    bluetoothSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Unable to close the socket", closeException);
                }
                success = false;
            }
        }
    }).start();
}

```


##### Bug 2: Data Corruption or Loss

-   **Symptom:** Received data on the Android device is incomplete or corrupted.
-   **Solution:** Implement checksums or validation for received data. Ensure that data is sent in a consistent format from the Arduino.
```C++
void loop() {
    // Data transmission logic
    String data = "{ \"time\": " + String(millis()) + ", \"value\": " + String(encoderValue) + " }";
    myBluetooth.println(data);
    Serial.println(data);
    delay(100); // Adjust delay to prevent data loss
}
```
##### Bug 3: Application Crash on Data Reception

-   **Symptom:** The Android application crashes when receiving data.
-   **Solution:** Ensure that the received data is parsed correctly and that all potential exceptions are handled.
```java
private void startReceivingData() {
    new Thread(() -> {
        try {
            inputStream = bluetoothSocket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            while (true) {
                try {
                    String line = reader.readLine();
                    if (line != null) {
                        JSONObject jsonObject = new JSONObject(line);
                        final double time = jsonObject.getDouble("time");
                        final double value = jsonObject.getDouble("value");

                        runOnUiThread(() -> addDataPoint(time, value));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "JSON parsing error", e);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error in receiving data", e);
        }
    }).start();
}

private void addDataPoint(double time, double value) {
    graphLastXValue += 1d;
    series.appendData(new DataPoint(graphLastXValue, value), true, 100);
}
```

##### Bug 4: Bluetooth Unable to Connect After Multiple Tests

-   **Symptom:** After multiple tests, the Bluetooth connection fails to establish.
-   **Cause:** The issue was traced back to the missing unregistration of broadcast receivers in the `onDestroy` method.
-   **Solution:** Properly unregister the broadcast receivers to prevent resource leaks and ensure a clean state for each test.

```java
@Override
protected void onDestroy() {
    super.onDestroy();
    // Unregister discovery broadcast receiver
    try {
        unregisterReceiver(receiver);
    } catch (IllegalArgumentException e) {
        Log.e(TAG, "Receiver not registered", e);
    }
    // Unregister Bluetooth connection state change broadcast receiver
    try {
        unregisterReceiver(mReceiver);
    } catch (IllegalArgumentException e) {
        Log.e(TAG, "Receiver not registered", e);
    }
}
```


## [Date: 2024-04-24]

### Today's Tasks

-   Task 1: Establish a BLE connection using GATT.
-   Task 2: Handle service and characteristic discovery.
-   Task 3: Manage data transmission over BLE.

### Detailed Notes

#### Task 1: [Establish a BLE Connection Using GATT]

The first task focused on establishing a BLE connection using the Generic Attribute Profile (GATT).

##### Steps:

-   Implemented GATT connection logic in the `ConnectThread`.
-   Added necessary permissions and initialization for BLE scanning and connection.

Here’s a snippet of the updated `ConnectThread`:
```java
private class ConnectThread extends Thread {
    private final BluetoothDevice mmDevice; // BluetoothDevice variable
    private BluetoothSocket mmSocket;
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
                            BluetoothGattCharacteristic characteristic = service.getCharacteristic(CHARACTERISTIC_UUID);
                            if (characteristic != null) {
                                gatt.readCharacteristic(characteristic); // Read the characteristic
                                message = "Service and characteristic UUID found.";
                            } else {
                                message = "Characteristic UUID not found.";
                            }
                        } else {
                            message = "Service UUID not found.";
                        }
                    } else {
                        message = "onServicesDiscovered received: " + status;
                    }
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
            });
        } else {
            // Classic Bluetooth, handle differently
        }
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
        });
    }
}
```
#### Task 2: [Handle Service and Characteristic Discovery]

The second task involved handling the discovery of services and characteristics over the BLE connection.

##### Steps:

-   Implemented callbacks for service and characteristic discovery.
-   Ensured that the necessary services and characteristics were found and accessible.

Here’s a snippet showing the service and characteristic discovery:

```java
@Override
public void onServicesDiscovered(BluetoothGatt gatt, int status) {
    super.onServicesDiscovered(gatt, status);
    String message;
    if (status == BluetoothGatt.GATT_SUCCESS) {
        BluetoothGattService service = gatt.getService(SERVICE_UUID);
        if (service != null) {
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(CHARACTERISTIC_UUID);
            if (characteristic != null) {
                gatt.readCharacteristic(characteristic); // Read the characteristic
                message = "Service and characteristic UUID found.";
            } else {
                message = "Characteristic UUID not found.";
            }
        } else {
            message = "Service UUID not found.";
        }
    } else {
        message = "onServicesDiscovered received: " + status;
    }
    runOnUiThread(() -> showAlertDialog("Service Discovery", message));
}
```
#### Task 3: [Manage Data Transmission Over BLE]

The third task was to manage data transmission over BLE, ensuring that data from the Arduino was transmitted efficiently and received accurately on the Android device.

##### Steps:

-   Implemented methods to read characteristics and handle the received data.
-   Ensured that data was transmitted and received in a consistent format.

Here’s a snippet showing data transmission management:

```java
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

private void handleCharacteristicRead(byte[] value) {
    runOnUiThread(() -> {
        // Update UI or handle data read from characteristic
    });
}
```

### Summary

Today's tasks focused on migrating from traditional Bluetooth to BLE, establishing a BLE connection using GATT, handling service and characteristic discovery, and managing data transmission over BLE. The transition to BLE improved efficiency and compatibility with modern devices. By implementing the necessary callbacks and handling data transmission effectively, the system now supports reliable real-time data communication over BLE.

## [Date: 2024-04-29]

### Today's Tasks

-   Task 1: Set up the Nordic Semiconductor nRF52840 dongle.
-   Task 2: Install and configure the Nordic SoftDevice.
-   Task 3: Manage the portico of the nRF52840 dongle.
-   Task 4: Configure UART over BLE for serial communication.

### Detailed Notes

#### Task 1: [Set Up the Nordic Semiconductor nRF52840 Dongle]

Today's primary task involved setting up the nRF52840 dongle to enhance BLE capabilities. This included using nRFgo Studio and nRF Connect for Desktop Programmer to configure the dongle.

##### Steps:

1.  **Install nRFgo Studio and nRF Connect for Desktop Programmer:**
    
    -   Download and install the latest version of nRFgo Studio from the Nordic Semiconductor website.
    -   Install nRF Connect for Desktop and its Programmer app from the Nordic Semiconductor website.
2.  **Connect the nRF52840 Dongle:**
    
    -   Connect the nRF52840 dongle to the computer via USB.
    -   Open nRFgo Studio and ensure the device is recognized under the Device Manager.

#### Task 2: [Install and Configure the Nordic SoftDevice]

The second task focused on installing and configuring the Nordic SoftDevice using nRFgo Studio and nRF Connect for Desktop Programmer.

##### Steps:

1.  **Download the SoftDevice:**
    
    -   Download the appropriate SoftDevice binary (e.g., S140) from the Nordic Semiconductor website.
2.  **Program the SoftDevice Using nRFgo Studio:**
    
    -   Open nRFgo Studio.
    -   Select the nRF52840 dongle in the Device Manager.
    -   Click on the “Program SoftDevice” tab.
    -   Browse to the downloaded SoftDevice .hex file and click “Program” to flash the SoftDevice onto the dongle.
3.  **Using nRF Connect for Desktop Programmer:**
    
    -   Open nRF Connect for Desktop and launch the Programmer app.
    -   Select the nRF52840 dongle.
    -   Click “Add HEX file” and select the SoftDevice .hex file.
    -   Click “Write” to flash the SoftDevice onto the dongle.

#### Task 3: [Manage the Portico of the nRF52840 Dongle]

The third task involved managing the portico (gateway) of the nRF52840 dongle to ensure smooth communication.

##### Steps:

-   **Configure BLE Settings:**
    -   Use the Nordic SDK to configure BLE settings and ensure proper initialization.
    -   Verify the communication stability through test transmissions.

#### Task 4: [Configure UART Over BLE for Serial Communication]

The fourth task was to configure UART over BLE to enable serial communication, allowing data to be sent and received wirelessly via the dongle.

##### Steps:

1.  **Setup UART Service:**
    
    -   Initialize UART service using the Nordic SDK.
    -   Configure the service to handle data transmissions.
    
### Summary

Today's tasks focused on configuring the nRF52840 dongle to enhance BLE capabilities, installing and configuring the Nordic SoftDevice, managing the dongle's portico for smooth communication, and setting up UART over BLE for serial communication. This setup improved efficiency and compatibility with modern devices, ensuring reliable wireless data transmission and reception.

For detailed steps on setting up and programming the SoftDevice, you can refer to the [Nordic Semiconductor InfoCenter](https://infocenter.nordicsemi.com) and the [Nordic DevZone](https://devzone.nordicsemi.com)​ ([Nordic Semiconductor Developer Hub](https://developer.nordicsemi.com/nRF5_SDK/nRF51_SDK_v8.x.x/doc/8.1.0/s130/html/a00018.html))​​ ([Nordic Semi Info Center](https://infocenter.nordicsemi.com/topic/com.nordic.infocenter.sdk51.v10.0.0/getting_started_softdevice.html))​​ ([Nordic DevZone](https://devzone.nordicsemi.com/nordic/nordic-blog/b/blog/posts/step-by-step-guide-to-setup-and-start-developemt-w))​​ ([Nordic DevZone](https://devzone.nordicsemi.com/guides/short-range-guides/b/software-development-kit/posts/getting-started-with-nordics-secure-dfu-bootloader))​​ ([Nordic Semi Info Center](https://infocenter.nordicsemi.com/topic/sdk_nrf5_v17.0.2/getting_started_examples.html))​.

## [Date: 2024-05-16]

### Today's Tasks

-   Task 1: Establish a BLE connection using GATT.
-   Task 2: Configure service and characteristic discovery for UART communication.
-   Task 3: Handle data received from the RX characteristic and dynamically update the user interface.

### Detailed Notes

#### Task 1: [Establish a BLE Connection Using GATT]

The first task focused on enabling real-time communication with BLE devices by establishing a BLE connection using the Generic Attribute Profile (GATT).

##### Steps:

-   Implemented GATT connection logic in the `ConnectThread`.
-   Added necessary permissions and initialization for BLE scanning and connection.

Here’s a snippet of the updated `ConnectThread`:
```java
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
            });
        } else {
            // Classic Bluetooth, handle differently
        }
    }
}
```
#### Task 2: [Configure Service and Characteristic Discovery for UART Communication]

The second task involved configuring the discovery of services and characteristics for UART communication over the BLE connection.

##### Steps:

-   Implemented callbacks for service and characteristic discovery.
-   Enabled notifications for the TX characteristic.

Here’s a snippet showing the service and characteristic discovery:

```java
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
```
#### Task 3: [Handle Data Received from the RX Characteristic and Dynamically Update the User Interface]

The third task was to handle data received from the RX characteristic, parse it, and dynamically update the user interface.

##### Steps:

-   Implemented methods to handle characteristic changes and parse the received data.
-   Updated the user interface dynamically based on the received data.

Here’s a snippet showing the data handling:
```java
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
```
### Summary

Today's tasks focused on enabling real-time communication with BLE devices on the mobile application. By establishing a BLE connection using GATT, configuring service and characteristic discovery for UART communication, and enabling notifications for the TX characteristic, the application can now handle data received from the RX characteristic and dynamically update the user interface.

## [Date: 2024-05-19]

### Today's Tasks

-   Task 1: Parse and handle received information.
-   Task 2: Visualize tangent force, efficiency, and push arc based on received data.

### Detailed Notes

#### Task 1: [Parse and Handle Received Information]

The first task involved parsing and handling the data received over the BLE connection.

##### Steps:

-   Implemented callbacks for service and characteristic discovery.
-   Parsed data received from the RX characteristic to extract relevant values.

Here’s a snippet showing the data parsing:
```java
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
```
#### Task 2: [Visualize Tangent Force, Efficiency, and Push Arc Based on Received Data]

The second task was to visualize the tangent force, efficiency, and push arc metrics based on the parsed data, updating the user interface dynamically.

##### Steps:

-   Implemented methods to handle characteristic changes and parse the received data.
-   Utilized custom views to plot the extracted data in real-time.

Here’s a snippet showing the data visualization:
```java
private void updateViews(double[] data) {
    TangentForceView tangentForceView = findViewById(R.id.tangentForceView);
    EfficiencyView efficiencyView = findViewById(R.id.efficiencyView);
    PushArcView pushArcView = findViewById(R.id.pushArcView);

    tangentForceView.addData((float) data[1], (float) data[6]);
    efficiencyView.addData((float) data[4], (float) data[9]);
    pushArcView.addData((float) data[2], (float) data[7]);
}
```
### Summary

Today's tasks focused on extending the BLE communication functionality by parsing and handling received data, and visualizing key metrics in real-time. By parsing data received over BLE and utilizing custom views to display tangent force, efficiency, and push arc, the application now provides a dynamic and interactive user experience. 

## [Date: 2024-05-23]

### Today's Tasks

-   Task 1: Integrate AndroidPlot library for improved rendering performance.
-   Task 2: Configure plots for tangent force, efficiency, and push arc.
-   Task 3: Implement dynamic updates and refine UI for real-time data visualization.

### Detailed Notes

#### Task 1: [Integrate AndroidPlot Library for Improved Rendering Performance]

The first task focused on integrating the AndroidPlot library to enhance the rendering performance and overall appearance of the user interface.

##### Steps:

-   Added the AndroidPlot library to the project.
-   Set up the library for use in the application.

Here’s a snippet showing the library setup:
```java
// Add dependencies in build.gradle
dependencies {
    implementation 'com.androidplot:androidplot-core:1.5.7'
}
```
#### Task 2: [Configure Plots for Tangent Force, Efficiency, and Push Arc]

The second task involved configuring the plots for tangent force, efficiency, and push arc using the AndroidPlot library.

##### Steps:

-   Created and configured plots using `SimpleXYSeries` and `LineAndPointFormatter`.
-   Set up plots to ensure smooth and accurate data rendering.

Here’s a snippet showing plot configuration:
```java
// Initialize the plot for tangent force
XYPlot tangentForcePlot = findViewById(R.id.tangentForcePlot);
SimpleXYSeries tangentForceSeries = new SimpleXYSeries("Tangent Force");
LineAndPointFormatter tangentForceFormatter = new LineAndPointFormatter();
tangentForcePlot.addSeries(tangentForceSeries, tangentForceFormatter);

// Initialize the plot for efficiency
XYPlot efficiencyPlot = findViewById(R.id.efficiencyPlot);
SimpleXYSeries efficiencySeries = new SimpleXYSeries("Efficiency");
LineAndPointFormatter efficiencyFormatter = new LineAndPointFormatter();
efficiencyPlot.addSeries(efficiencySeries, efficiencyFormatter);

// Initialize the plot for push arc
XYPlot pushArcPlot = findViewById(R.id.pushArcPlot);
SimpleXYSeries pushArcSeries = new SimpleXYSeries("Push Arc");
LineAndPointFormatter pushArcFormatter = new LineAndPointFormatter();
pushArcPlot.addSeries(pushArcSeries, pushArcFormatter);
```
#### Task 3: [Implement Dynamic Updates and Refine UI for Real-time Data Visualization]

The third task was to implement dynamic updates to the plots to ensure real-time data visualization and refine the UI to be more professional and user-friendly.

##### Steps:

-   Implemented dynamic updates to the plots using data received over BLE.
-   Refined the UI to provide clear, accurate, and aesthetically pleasing data visualizations.

Here’s a snippet showing dynamic updates:
```java
private void updateViews(double[] data) {
    // Update tangent force plot
    tangentForceSeries.setModel(Arrays.asList(data[1], data[6]), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
    tangentForcePlot.redraw();

    // Update efficiency plot
    efficiencySeries.setModel(Arrays.asList(data[4], data[9]), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
    efficiencyPlot.redraw();

    // Update push arc plot
    pushArcSeries.setModel(Arrays.asList(data[2], data[7]), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY);
    pushArcPlot.redraw();
}
```
#### UI Modifications

To accommodate the new plotting features, the user interface was also refined using `ConstraintLayout` for a more professional look.

Here’s a snippet showing the updated UI layout:
```java
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <Button
        android:id="@+id/BluetoothButton"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Bluetooth"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="8dp"/>

    <com.androidplot.xy.XYPlot
        android:id="@+id/tangentForcePlot"
        android:layout_width="0dp"
        android:layout_height="0dp"
        style = "@style/APDefacto.Light"
        app:layout_constraintTop_toBottomOf="@id/BluetoothButton"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toTopOf="@+id/efficiencyPlot"
        app:layout_constraintHeight_default="percent"
        app:layout_constraintHeight_percent="0.3"
        android:layout_marginTop="8dp"/>

    <com.androidplot.xy.XYPlot
        android:id="@+id/efficiencyPlot"
        android:layout_width="0dp"
        android:layout_height="0dp"
        style = "@style/APDefacto.Light"
        app:layout_constraintTop_toBottomOf="@id/tangentForcePlot"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toTopOf="@+id/pushArcPlot"
        app:layout_constraintHeight_default="percent"
        app:layout_constraintHeight_percent="0.3"/>

    <com.androidplot.xy.XYPlot
        android:id="@+id/pushArcPlot"
        android:layout_width="0dp"
        android:layout_height="0dp"
        style = "@style/APDefacto.Light"
        app:layout_constraintTop_toBottomOf="@id/efficiencyPlot"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintHeight_default="percent"
        app:layout_constraintHeight_percent="0.3"
        android:layout_marginBottom="8dp"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```
### Summary

Today's tasks focused on integrating the AndroidPlot library to improve the rendering performance and overall look of the UI. By setting up the library and configuring plots for tangent force, efficiency, and push arc, the application now provides smooth and accurate data visualizations. The implementation of dynamic updates ensures real-time data visualization with minimal latency.
