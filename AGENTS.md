# Agent Instructions for Overseerr Android Client Development

This document provides guidance for AI agents working on the Overseerr Android Client project. It describes the available MCP (Model Context Protocol) servers and how to use them effectively during development.

## Available MCP Servers

This project has access to multiple MCP servers for development:

### Project-Specific Servers

#### 1. mobile-mcp (@mobilenext/mobile-mcp)
Primary mobile development server for device interaction and testing.

#### 2. mobile-dev (@cristianoaredes/mcp-mobile-server)
Comprehensive mobile development server with Flutter, Android SDK, and device management capabilities.

### Global Servers

#### 3. Context7
Documentation and code example retrieval for libraries and frameworks. Use this to get up-to-date documentation for Android, Kotlin, Jetpack Compose, and other dependencies.

#### 4. Augments
Framework-specific documentation, patterns, and code examples. Provides comprehensive guides for modern development frameworks including Android/Kotlin ecosystem.

## When to Use Each MCP Server

### Use `mobile-mcp` for:
- **Device Interaction**: Listing, controlling, and interacting with Android devices/emulators
- **App Testing**: Installing, launching, and testing the app on devices
- **UI Automation**: Clicking, swiping, typing, and taking screenshots
- **App Management**: Listing, installing, uninstalling apps
- **Screen Inspection**: Taking screenshots and listing UI elements

### Use `mobile-dev` for:
- **Environment Setup**: Checking Flutter/Android SDK installation and configuration
- **Device Management**: Listing devices, creating AVDs, starting/stopping emulators
- **Build Operations**: Building APKs and app bundles
- **Development Workflow**: Running the app with hot reload, running tests
- **Troubleshooting**: Running flutter doctor, checking health, fixing common issues

### Use `Context7` for:
- **Library Documentation**: Get official documentation for Android libraries (Retrofit, Room, Hilt, etc.)
- **API References**: Look up specific API methods and their usage
- **Code Examples**: Find real-world code examples from official documentation
- **Version-Specific Info**: Get documentation for specific library versions
- **Best Practices**: Learn recommended patterns from official sources

### Use `Augments` for:
- **Framework Guides**: Get comprehensive guides for Kotlin, Jetpack Compose, Material 3
- **Design Patterns**: Learn Android architecture patterns (MVVM, Clean Architecture)
- **Code Patterns**: Get framework-specific code examples and templates
- **Integration Help**: Learn how to integrate multiple frameworks together
- **Troubleshooting**: Find solutions to common framework-specific issues

## Common Development Workflows

### 1. Initial Environment Setup

**Check development environment:**
```
Use mobile-dev: health_check with verbose=true
```
This will verify that all required tools (Android SDK, platform tools, build tools) are installed and properly configured.

**List available devices:**
```
Use mobile-dev: flutter_list_devices
OR
Use mobile-mcp: mobile_list_available_devices
```

**If no devices available, create an Android emulator:**
```
Use mobile-dev: android_list_emulators (to see existing AVDs)
Use mobile-dev: android_create_avd (to create a new one if needed)
Use mobile-dev: android_start_emulator (to start it)
```

### 2. Building the Application

**Build debug APK:**
```
Use mobile-dev: flutter_build
- cwd: /root/git/stream_app
- target: apk
- buildMode: debug
```

**Build release APK:**
```
Use mobile-dev: flutter_build
- cwd: /root/git/stream_app
- target: apk
- buildMode: release
```

**Build app bundle for Play Store:**
```
Use mobile-dev: flutter_build
- cwd: /root/git/stream_app
- target: appbundle
- buildMode: release
```

### 3. Installing and Running the App

**Install APK on device:**
```
Use mobile-dev: native_run_install_app
- platform: android
- appPath: build/app/outputs/flutter-apk/app-debug.apk
- deviceId: <device-id>

OR

Use mobile-mcp: mobile_install_app
- device: <device-id>
- path: build/app/outputs/flutter-apk/app-debug.apk
```

**Launch the app:**
```
Use mobile-mcp: mobile_launch_app
- device: <device-id>
- packageName: com.example.overseerr_client
```

**Run with hot reload (development):**
```
Use mobile-dev: flutter_run
- cwd: /root/git/stream_app
- deviceId: <device-id>
- target: lib/main.dart
```

### 4. Testing and Debugging

**Take a screenshot:**
```
Use mobile-mcp: mobile_take_screenshot
- device: <device-id>

OR

Use mobile-dev: android_screenshot
- serial: <device-id>
- outputPath: ./screenshots/test.png
```

**List UI elements on screen:**
```
Use mobile-mcp: mobile_list_elements_on_screen
- device: <device-id>
```

**Interact with UI:**
```
Use mobile-mcp: mobile_click_on_screen_at_coordinates
- device: <device-id>
- x: <x-coordinate>
- y: <y-coordinate>

Use mobile-mcp: mobile_type_keys
- device: <device-id>
- text: "search query"
- submit: false

Use mobile-mcp: mobile_swipe_on_screen
- device: <device-id>
- direction: up/down/left/right
```

**View Android logs:**
```
Use mobile-dev: android_logcat
- serial: <device-id>
- lines: 100
- filter: "*:E" (for errors only)
```

**Run tests:**
```
Use mobile-dev: flutter_test
- cwd: /root/git/stream_app
- coverage: true
```

### 5. Device Management

**List all connected devices:**
```
Use mobile-dev: android_list_devices
OR
Use mobile-mcp: mobile_list_available_devices
```

**Get screen size:**
```
Use mobile-mcp: mobile_get_screen_size
- device: <device-id>
```

**Change orientation:**
```
Use mobile-mcp: mobile_set_orientation
- device: <device-id>
- orientation: portrait/landscape
```

**Press hardware buttons:**
```
Use mobile-mcp: mobile_press_button
- device: <device-id>
- button: BACK/HOME/VOLUME_UP/VOLUME_DOWN
```

### 6. Troubleshooting

**Fix common issues:**
```
Use mobile-dev: flutter_fix_common_issues
- cwd: /root/git/stream_app
- deep: false (set to true for thorough cleaning)
```

**Clean build artifacts:**
```
Use mobile-dev: flutter_clean
- cwd: /root/git/stream_app
```

**Reinstall dependencies:**
```
Use mobile-dev: flutter_pub_get
- cwd: /root/git/stream_app
```

### 7. Documentation and Code Examples

**Look up library documentation:**
```
Use Context7: resolve-library-id
- libraryName: "retrofit" (or "jetpack compose", "hilt", "room", etc.)
- query: "your question about the library"

Then use Context7: query-docs
- libraryId: "/square/retrofit" (from resolve-library-id result)
- query: "how to make POST requests with JSON body"
```

**Get framework guides and patterns:**
```
Use Augments: search_frameworks
- query: "kotlin coroutines" (or "jetpack compose", "material design", etc.)

Use Augments: get_framework_docs
- framework: "kotlin"
- section: "coroutines" (optional)

Use Augments: get_framework_examples
- framework: "jetpack-compose"
- pattern: "navigation" (or "state management", "theming", etc.)
```

**Get context for multiple frameworks:**
```
Use Augments: get_framework_context
- frameworks: ["kotlin", "jetpack-compose", "material-design"]
- task_description: "implementing authentication with biometric support"
```

## Task-Specific Guidance

### Task 1: Project Setup and Infrastructure
- Use `mobile-dev: health_check` to verify environment
- Use `mobile-dev: flutter_doctor` to check Flutter installation
- Ensure Android SDK is properly configured
- Use `Context7` to look up latest Gradle, Kotlin, and Compose versions
- Use `Augments: get_framework_docs` for Kotlin and Jetpack Compose setup guides

### Tasks 2-6: Core Implementation (Models, Security, Networking, Database)
- Use `Context7` to look up documentation for:
  - Kotlinx Serialization (data models)
  - Android Keystore (security)
  - Retrofit and OkHttp (networking)
  - Room Database (local storage)
  - Hilt/Dagger (dependency injection)
- Use `Augments: get_framework_examples` for:
  - MVVM architecture patterns
  - Kotlin Coroutines and Flow
  - Clean Architecture implementation
- Focus on code implementation and unit tests
- Use standard Kotlin/Android development practices

### Task 7: Authentication Module
- Use `Context7` to look up:
  - Android Biometric API documentation
  - OAuth implementation patterns
  - Secure storage best practices
- Use `Augments` for authentication flow patterns
- Use `mobile-mcp` to test OAuth flow on real devices
- Take screenshots of authentication screens for verification
- Test biometric authentication on physical devices

### Task 8: Checkpoint - Authentication Complete
- Use `mobile-dev: flutter_build` to create test APK
- Use `mobile-mcp: mobile_install_app` to install on test devices
- Use `mobile-mcp: mobile_launch_app` to test authentication flow
- Use `mobile-mcp: mobile_take_screenshot` to document test results

### Tasks 9-10: Discovery and Request Modules
- Use `Context7` to look up:
  - Paging 3 library documentation
  - Coil image loading library
  - Compose LazyColumn and LazyGrid
- Use `Augments` for pagination and infinite scroll patterns
- Use `mobile-mcp` to test search functionality
- Test infinite scrolling by swiping
- Verify request submission flows
- Take screenshots of different states

### Task 11: Checkpoint - Core Features Complete
- Build and install app on multiple device types
- Test end-to-end flows using `mobile-mcp` interaction tools
- Verify on both phone and tablet layouts

### Tasks 12-13: Profile, Settings, and Notifications
- Use `Context7` to look up:
  - DataStore documentation
  - Firebase Cloud Messaging
  - Android notification channels
- Use `Augments` for Material 3 theming patterns
- Test theme changes on devices
- Verify notification delivery
- Test deep link navigation using `mobile-mcp: mobile_open_url`

### Task 14: Offline Support
- Use `Context7` to look up:
  - Room Database caching strategies
  - WorkManager for background sync
  - ConnectivityManager API
- Use `Augments` for offline-first architecture patterns
- Use `mobile-mcp` to test offline scenarios
- Disable network and verify cached content
- Re-enable network and verify sync

### Task 15: Material You UI and Theming
- Use `Context7` to look up Material 3 Compose documentation
- Use `Augments: get_framework_docs` for Material Design 3 guidelines
- Use `Augments: get_framework_examples` for:
  - Dynamic color theming
  - Adaptive layouts
  - Responsive design patterns
- Test on devices with different Android versions
- Verify dynamic theming on Android 12+
- Test adaptive layouts on tablets and foldables
- Use `mobile-mcp: mobile_set_orientation` to test landscape mode

### Task 16: Image Loading
- Use `Context7` to look up Coil library documentation
- Use `Augments` for image loading optimization patterns

### Task 17: Error Handling and Crash Reporting
- Use `Context7` to look up exception handling in Kotlin Coroutines
- Use `Augments` for error handling patterns in Android

### Task 18: Navigation
- Use `Context7` to look up Jetpack Navigation Compose documentation
- Use `Augments` for navigation patterns and deep linking

### Task 19: Final Integration and Polish
- Use `mobile-dev: flutter_test_suite` to run all tests
- Use `mobile-dev: flutter_release_build` for final builds
- Test on multiple devices using `mobile-dev: mobile_device_manager`

### Task 20: Final Checkpoint
- Use `mobile-dev: health_check` to verify environment
- Build release APK/AAB using `mobile-dev: flutter_build`
- Test on all target devices
- Capture screenshots for documentation

## Best Practices

### 1. Always Check Device Availability First
Before running any device-specific commands, list available devices:
```
Use mobile-mcp: mobile_list_available_devices
```

### 2. Use Appropriate Build Modes
- **debug**: For development and testing (includes debug symbols)
- **profile**: For performance testing
- **release**: For production builds (optimized, obfuscated)

### 3. Clean Build When Needed
If you encounter build issues:
```
Use mobile-dev: flutter_clean
Use mobile-dev: flutter_pub_get
```

### 4. Capture Evidence
Take screenshots during testing to document:
- UI states
- Error conditions
- Successful flows
- Different device layouts

### 5. Test on Multiple Devices
- Test on at least one phone and one tablet
- Test on different Android versions (API 26+)
- Test both portrait and landscape orientations

### 6. Monitor Logs
Use `android_logcat` to monitor app behavior and catch errors:
```
Use mobile-dev: android_logcat
- serial: <device-id>
- filter: "com.example.overseerr_client:*"
```

### 7. Automate Testing Flows
Use `mobile-mcp` to automate repetitive testing:
1. Launch app
2. Navigate to screen
3. Interact with UI elements
4. Verify results
5. Take screenshots

### 8. Use Documentation Tools Effectively
When implementing new features:
1. Use `Context7: resolve-library-id` to find the library
2. Use `Context7: query-docs` to get specific API documentation
3. Use `Augments: get_framework_examples` to see implementation patterns
4. Combine official docs with practical examples for best results

### 9. Learn Framework Patterns
Before implementing complex features:
1. Use `Augments: search_frameworks` to find relevant frameworks
2. Use `Augments: get_framework_context` with multiple frameworks for integration guidance
3. Use `Augments: get_framework_examples` for specific patterns
4. Apply learned patterns to your implementation

## Troubleshooting Common Issues

### "No devices found"
```
1. Use mobile-dev: android_list_devices
2. If empty, use mobile-dev: android_list_emulators
3. Start an emulator or connect a physical device
4. Verify with mobile-dev: flutter_list_devices
```

### "Build failed"
```
1. Use mobile-dev: flutter_clean
2. Use mobile-dev: flutter_pub_get
3. Use mobile-dev: flutter_fix_common_issues with deep=true
4. Retry build
```

### "App won't install"
```
1. Uninstall existing version using mobile-mcp: mobile_uninstall_app
2. Rebuild APK with mobile-dev: flutter_build
3. Reinstall using mobile-mcp: mobile_install_app
```

### "Tests failing"
```
1. Check logs with mobile-dev: android_logcat
2. Run tests with coverage: mobile-dev: flutter_test with coverage=true
3. Fix issues and rerun
```

### "How do I implement X feature?"
```
1. Use Context7: resolve-library-id to find relevant libraries
2. Use Context7: query-docs to get API documentation
3. Use Augments: get_framework_examples for implementation patterns
4. Combine documentation with examples to implement the feature
```

### "Library version conflicts"
```
1. Use Context7 to check latest compatible versions
2. Use Augments to find migration guides if updating versions
3. Update dependencies in build.gradle
4. Use mobile-dev: flutter_clean and rebuild
```

## Project-Specific Notes

### Package Name
The app package name should be: `com.example.overseerr_client` (or your chosen package name)

### Minimum SDK
API 26 (Android 8.0) - ensure test devices meet this requirement

### Target SDK
API 35 (Android 15) - test on latest Android versions when possible

### Build Outputs
- Debug APK: `build/app/outputs/flutter-apk/app-debug.apk`
- Release APK: `build/app/outputs/flutter-apk/app-release.apk`
- App Bundle: `build/app/outputs/bundle/release/app-release.aab`

### Test Directories
- Unit tests: `test/`
- Integration tests: `integration_test/`
- Property tests: `test/property/`

## Quick Reference

| Task | MCP Server | Command |
|------|-----------|---------|
| Check environment | mobile-dev | health_check |
| List devices | mobile-mcp | mobile_list_available_devices |
| Build APK | mobile-dev | flutter_build |
| Install app | mobile-mcp | mobile_install_app |
| Launch app | mobile-mcp | mobile_launch_app |
| Take screenshot | mobile-mcp | mobile_take_screenshot |
| View logs | mobile-dev | android_logcat |
| Run tests | mobile-dev | flutter_test |
| Clean build | mobile-dev | flutter_clean |
| Fix issues | mobile-dev | flutter_fix_common_issues |
| Find library docs | Context7 | resolve-library-id + query-docs |
| Get code examples | Context7 | query-docs |
| Search frameworks | Augments | search_frameworks |
| Get framework guide | Augments | get_framework_docs |
| Get code patterns | Augments | get_framework_examples |
| Multi-framework help | Augments | get_framework_context |

## Example Workflows with All MCP Servers

### Implementing a New Feature (e.g., Biometric Authentication)

1. **Research** (Context7 + Augments):
   ```
   Context7: resolve-library-id
   - libraryName: "android biometric"
   - query: "biometric authentication implementation"
   
   Context7: query-docs
   - libraryId: "/androidx/biometric"
   - query: "how to implement fingerprint authentication"
   
   Augments: get_framework_examples
   - framework: "android"
   - pattern: "biometric-authentication"
   ```

2. **Implement** (Code):
   - Write BiometricAuthenticator class based on documentation
   - Integrate with SecurityManager
   - Add UI components

3. **Test** (mobile-mcp + mobile-dev):
   ```
   mobile-dev: flutter_build (build APK)
   mobile-mcp: mobile_install_app (install on device)
   mobile-mcp: mobile_launch_app (launch app)
   Test biometric prompt on physical device
   mobile-mcp: mobile_take_screenshot (document results)
   ```

### Debugging a Network Issue

1. **Check Implementation** (Context7):
   ```
   Context7: query-docs
   - libraryId: "/square/retrofit"
   - query: "debugging network requests and responses"
   ```

2. **Review Patterns** (Augments):
   ```
   Augments: get_framework_examples
   - framework: "retrofit"
   - pattern: "error-handling"
   ```

3. **Test and Debug** (mobile-dev + mobile-mcp):
   ```
   mobile-dev: android_logcat (view network logs)
   mobile-mcp: mobile_list_elements_on_screen (check UI state)
   mobile-mcp: mobile_take_screenshot (capture error state)
   ```

### Setting Up Material 3 Theming

1. **Learn Patterns** (Augments):
   ```
   Augments: get_framework_context
   - frameworks: ["jetpack-compose", "material-design"]
   - task_description: "implementing Material You dynamic theming"
   ```

2. **Get Specific Examples** (Context7 + Augments):
   ```
   Context7: query-docs
   - libraryId: "/androidx/compose-material3"
   - query: "dynamic color scheme implementation"
   
   Augments: get_framework_examples
   - framework: "material-design"
   - pattern: "dynamic-theming"
   ```

3. **Implement and Test** (Code + mobile-mcp):
   - Implement theme configuration
   - Test on Android 12+ device with mobile-mcp
   - Verify dynamic colors change with wallpaper

## Additional Resources

- [Android Developer Documentation](https://developer.android.com/)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Overseerr API Documentation](https://api-docs.overseerr.dev/)

## Key Libraries to Look Up with Context7

When implementing features, use Context7 to get documentation for these key libraries:

- **Networking**: `retrofit`, `okhttp`, `kotlinx-serialization`
- **Dependency Injection**: `hilt`, `dagger`
- **Database**: `room`, `datastore`
- **UI**: `jetpack-compose`, `compose-material3`, `compose-navigation`
- **Image Loading**: `coil`
- **Async**: `kotlin-coroutines`, `kotlin-flow`
- **Testing**: `junit5`, `mockk`, `kotest`
- **Security**: `androidx-biometric`, `androidx-security`
- **Pagination**: `paging3`

## Framework Topics to Explore with Augments

Use Augments to get comprehensive guides and patterns for:

- **Architecture**: MVVM, Clean Architecture, Repository Pattern
- **UI Patterns**: Compose state management, navigation, theming
- **Android Specifics**: Material Design 3, adaptive layouts, dynamic colors
- **Best Practices**: Kotlin idioms, coroutine patterns, error handling
- **Testing**: Unit testing, property-based testing, UI testing
- **Performance**: Memory management, image optimization, lazy loading

---

**Note**: This project uses Kotlin and Jetpack Compose, not Flutter. However, the mobile-dev MCP server provides useful Android SDK and device management capabilities that work with any Android project. When using mobile-dev commands, focus on the Android-specific tools (android_*, native_run_*) rather than Flutter-specific commands.

**Pro Tip**: Combine all four MCP servers for maximum effectiveness:
1. Use **Context7** for official API documentation
2. Use **Augments** for framework patterns and best practices  
3. Use **mobile-dev** for building and environment management
4. Use **mobile-mcp** for device testing and interaction
