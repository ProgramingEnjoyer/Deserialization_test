# Application Features and Usage Guide

This document outlines the key features, usage, and development insights for the application designed to connect to Bluetooth devices, visualize data, and handle file selection and permissions effectively.

## Features

- **Bluetooth Connectivity:** Enables connection to nearby Bluetooth devices, displays a list of available devices, and manages connections.
- **Data Visualization:** Offers a custom view that dynamically updates to visualize data as new data points are received or loaded.
- **File Selection and Deserialization:** Utilizes the system's file picker to select files and deserialize content for displaying sine wave data.
- **Permissions Handling:** Manages necessary permissions for Bluetooth connectivity and file access, including managing external storage on devices running Android 11 and above.

## Usage

### Bluetooth Connection

- Tap the "Bluetooth" button to start the discovery of nearby Bluetooth devices.
- Select a device from the list to establish a connection and receive data.

### Load Data

- Tap the "Load Data" button to open the file picker.
- Select a file containing serialized sine wave data (`.ser` extension recommended) to load and visualize the data.

### Visualize Data

- The main screen displays data in a graphical format.
- New data points from Bluetooth devices or loaded files will update this visualization in real-time.

## Development Components

- **MainActivity:** Manages UI interactions, Bluetooth connectivity, file selection, and deserialization of data.
- **SineWaveView:** A custom View for drawing data points on the screen.
- **SineWaveData:** A serializable class representing data points with time and value properties.

## Permissions

The application requires the following permissions:

- **Bluetooth and Bluetooth Admin:** Necessary for discovering and connecting to Bluetooth devices.
- **Access Fine Location (Runtime Permission):** Required on some devices for Bluetooth discovery.
- **Manage External Storage (Android 11+):** Needed to access the file system for loading serialized data files.
