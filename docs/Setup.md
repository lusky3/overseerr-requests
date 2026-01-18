# Development Setup Guide

Follow these steps to set up your environment and run the Underseerr application.

## Prerequisites

1. **JDK 17+**: Ensure you have a compatible Java Development Kit installed (JDK 17 or newer is recommended).
2. **Android Studio**: Install the latest stable version of Android Studio (Ladybug or newer recommended).
3. **Git**: For version control.

## Cloning the Repository

```bash
git clone https://github.com/lusky3/overseerr-requests.git
cd overseerr-requests
```

## Configuration

This project uses **Firebase** and requires `google-services.json` files for both Debug and Release build variants.

### Local Development (Debug)

For local development, you need a valid `google-services.json` file.

1. Obtain the **Debug** `google-services.json` from the Firebase Console or the project administrator.
2. Place it in: `androidApp/src/debug/google-services.json`.

### Release Builds

Release builds require a separate configuration file.

1. Obtain the **Release** `google-services.json`.
2. Place it in: `androidApp/src/release/google-services.json`.

> **Note**: These files are ignored by git for security. In CI/CD, they are injected via repository secrets/variables.

## Building and Running

### Command Line

You can build the app using Gradle wrapper:

```bash
# Run unit tests
./gradlew testDebugUnitTest

# Run Lint checks
./gradlew lintDebug

# Assemble Debug APK
./gradlew assembleDebug
```

### Android Studio

1. Open the project in Android Studio.
2. Wait for Gradle sync to complete.
3. Select the `androidApp` run configuration.
4. Connect a device or start an emulator.
5. Click **Run** (green arrow).

## Common Issues

If you encounter `File google-services.json is missing`, ensure you have placed the files in the correct directories as described above. See [Troubleshooting](Troubleshooting.md) for more details.
