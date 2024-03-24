Features

Bluetooth Connectivity: Connect to nearby Bluetooth devices, display a list of available devices, and manage connections within the app.
Data Visualization: Visualize data through a custom view that dynamically updates as new data points are received or loaded.
File Selection and Deserialization: Pick files using the system's file picker and deserialize the content to display sine wave data within the application.
Permissions Handling: Request and manage necessary permissions for Bluetooth connectivity and file access, including managing external storage on devices running Android 11 and above.

Usage

Bluetooth Connection: Tap the "Bluetooth" button to start the discovery of nearby Bluetooth devices. Select a device from the list and establish a connection to receive data.
Load Data: Tap the "Load Data" button to open the file picker. Select a file containing serialized sine wave data (.ser extension recommended) to load and visualize the data.
Visualize Data: The main screen displays the data in a graphical format. New data points from Bluetooth devices or loaded files will update this visualization in real-time.
Development
MainActivity: Manages UI interactions, Bluetooth connectivity, file selection, and deserialization of data.
SineWaveView: A custom View that draws data (not only sine wave data) points on the screen.
SineWaveData: A serializable class that represents data points with time and value properties.

Permissions

The application requires the following permissions:
Bluetooth and Bluetooth Admin: Necessary for discovering and connecting to Bluetooth devices.
Access Fine Location (Runtime Permission): Required on some devices for Bluetooth discovery.
Manage External Storage (Android 11+): Required to access the file system for loading serialized data files.
