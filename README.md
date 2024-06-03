
# Deserialization Test Project

This project demonstrates the implementation of a Bluetooth-based application on Android that connects to Bluetooth Low Energy (BLE) devices and classic Bluetooth devices. The application collects and processes data, displaying it in real-time on three plots: Tangent Force, Efficiency, and Push Arc.

## Table of Contents

-   [Features](#features)
-   [Getting Started](#getting-started)
    -   [Prerequisites](#prerequisites)
    -   [Installation](#installation)
-   [Usage](#usage)
-   [Plotting](#plotting)
-   [Bluetooth Connection Management](#bluetooth-connection-management)
-   [Permissions](#permissions)
-   [License](#license)

## Features

-   Connects to both BLE and classic Bluetooth devices.
-   Discovers nearby Bluetooth devices.
-   Reads and processes data from connected devices.
-   Displays data on three plots (Tangent Force, Efficiency, Push Arc) using `AndroidPlot`.
-   Handles reconnections and disconnections gracefully.

## Getting Started

### Prerequisites

Ensure you have the following tools and libraries installed:

-   Android Studio
-   Android SDK
-   Bluetooth-enabled Android device

### Installation

1.  **Clone the repository:**
2.  **Open the project in Android Studio:**
    
    -   Launch Android Studio.
    -   Select "Open an existing Android Studio project".
    -   Navigate to the cloned repository and select it.
3.  **Build the project:**
    
    -   Allow Android Studio to install any necessary dependencies.
    -   Build the project by selecting `Build > Make Project`.
4.  **Run the project:**
    
    -   Connect your Android device via USB.
    -   Select your device in the Android Studio toolbar.
    -   Click the `Run` button.

## Usage

1.  **Enable Bluetooth:**
    
    -   Ensure Bluetooth is enabled on your Android device.
2.  **Discover Devices:**
    
    -   Click the `Bluetooth` button.
    -   The app will start discovering nearby Bluetooth devices.
3.  **Select and Connect to a Device:**
    
    -   Select a device from the list of discovered devices.
    -   Click `Connect` to establish a connection.
4.  **View Data:**
    
    -   The app will display real-time data on the Tangent Force, Efficiency, and Push Arc plots.

## Plotting

The application uses the `AndroidPlot` library to display real-time data on three plots:

-   **Tangent Force Plot:** Displays force data.
-   **Efficiency Plot:** Displays efficiency data.
-   **Push Arc Plot:** Displays push arc data in degrees.

## Bluetooth Connection Management

-   **BroadcastReceiver:** Listens for Bluetooth device discovery and connection status changes.
-   **discoverDevices():** Starts the discovery of Bluetooth devices.
-   **showDeviceListDialog():** Displays a dialog with the list of discovered devices and manages the connection UI.
-   **ConnectThread:** Establishes a connection to a selected Bluetooth device.
-   **ConnectedThread:** Manages the data communication with the connected device.
-   **DisconnectThread:** Handles disconnection from the device.

## Permissions

The application requires the following permissions:

-   **Bluetooth:** To discover and connect to Bluetooth devices.
-   **Bluetooth Admin:** To manage Bluetooth settings.
-   **Location:** Required for Bluetooth device discovery on Android 6.0 and above.

Ensure these permissions are requested at runtime for devices running Android 6.0 (API level 23) or higher.

## License

This project is licensed under the MIT License. See the LICENSE file for more details.

----------

Feel free to contribute to the project by opening issues or submitting pull requests. For any questions, contact rmhkjme@ucl.ac.uk
