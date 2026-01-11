# Android Testing Environment Setup Guide

This guide documents the steps required to set up the Android testing environment for this project. It is intended for developers and AI agents setting up the environment from scratch.

## 1. Prerequisites (Pre-installed)

The environment is expected to have the Android SDK tools installed.

- **SDK Location**: `/usr/lib/android-sdk`
- **ADB Location**: `/usr/bin/adb` created from `/usr/lib/android-sdk/platform-tools/adb`

## 2. One-Time Setup: Install Emulator

The standard Android SDK installation in this environment might miss the `emulator` binary.

1. **Check if installed**:

    ```bash
    ls /usr/lib/android-sdk/emulator/emulator
    ```

    If this file exists, skip to step 3.

2. **Install via APT**:

    ```bash
    sudo apt-get update && sudo apt-get install -y google-android-emulator-installer
    ```

    *Note: This installation command might return an exit code `100` or report failure in the post-installation script. This is often a false negative in this environment; usually, the binaries are successfully unpacked to `/usr/lib/android-sdk/emulator` despite the error.*

## 3. Identify or Create an AVD (Android Virtual Device)

1. **List available AVDs**:

    ```bash
    /usr/lib/android-sdk/cmdline-tools/latest/bin/avdmanager list avd
    # OR older path
    /usr/lib/android-sdk/cmdline-tools/13.0/bin/avdmanager list avd
    ```

2. **Current Standard AVD**:
    - **Name**: `overseerr_test`
    - **Device**: Pixel 5
    - **Image**: system-images;android-34;google_apis;x86_64

## 4. Starting the Emulator

### Option A: Headless Mode (Recommended for AI Agents)

Start the emulator in headless mode (no window) to work within this terminal-based environment. Use `nohup` to keep it running in the background.

```bash
nohup /usr/lib/android-sdk/emulator/emulator -avd overseerr_test -no-window -no-audio -no-boot-anim -gpu auto > /tmp/emulator.log 2>&1 &
```

### Option B: GUI Mode (WSL2 with WSLg)

If you are running in a WSL2 environment with WSLg enabled (typically default on modern Windows 10/11), you can see the emulator window.
*Check support: Run `env | grep WSL2_GUI_APPS_ENABLED`. If `1`, it is supported.*

```bash
nohup /usr/lib/android-sdk/emulator/emulator -avd overseerr_test -no-audio -no-boot-anim -gpu auto > /tmp/emulator_gui.log 2>&1 &
```

**Flags Explanation**:

- `-avd overseerr_test`: The name of the virtual device.
- `-no-window`: (Headless only) Essential for environments without a display. Remove this to see the window.
- `-no-audio`: Disables audio support.
- `-no-boot-anim`: Speeds up boot time.
- `-gpu auto`: Selects the best GPU emulation code.

## 5. Verification

Wait a few moments for the device to boot, then check connection:

```bash
adb devices
```

You should see:

```text
List of devices attached
emulator-5554   device
```

If it says `offline`, wait a bit longer or restart the emulator.

## 6. Using Mobile MCP

Once `adb devices` lists the emulator as `device`, the Mobile MCP server will automatically detect it.

**Example Mobile MCP Check**:

- **Tool**: `mobile_list_available_devices`
- **Expected Output**:

  ```json
  {
    "devices": [
      {
        "id": "emulator-5554",
        "name": "overseerr test",
        "platform": "android",
        "state": "online"
      }
    ]
  }
  ```

## Troubleshooting

- **Emulator missing**: Verify step 2.
- **Permission Denied**: Ensure you have read/execute permissions on `/usr/lib/android-sdk`.
- **ADB not found**: Ensure `/usr/bin/adb` or `/usr/lib/android-sdk/platform-tools` is in your PATH.
