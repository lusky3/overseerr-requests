# Architecture & Tech Stack

This project follows modern Android development practices and utilizes Kotlin Multiplatform (KMP) structure (preparing for potential cross-platform expansion).

## Tech Stack

* **Language**: [Kotlin](https://kotlinlang.org/)
* **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Android) / Compose Multiplatform
* **Dependency Injection**: [Koin](https://insert-koin.io/)
* **Networking**: [Ktor](https://ktor.io/)
* **Image Loading**: [Coil 3](https://coil-kt.github.io/coil/)
* **Asynchronous Programming**: Kotlin Coroutines & Flow
* **Navigation**: Navigation Compose
* **Build System**: Gradle (Kotlin DSL)

## Module Structure

The project is structured into modular components:

### `:androidApp`

The entry point for the Android application.

* Contains `AndroidManifest.xml`.
* Holds platform-specific resource configurations (e.g., `google-services.json`).
* Initializes the dependency graph.

### `:composeApp` (Shared Logic)

The core module containing the bulk of the application logic and UI.

* **`commonMain`**: Shared code (UI, ViewModels, Networking, Domain Logic).
* **`androidMain`**: Android-specific implementations for shared interfaces.
* **`iosMain`**: (Planned/Existing) iOS-specific implementations.

## Design Pattern

The app follows **Clean Architecture** principles + **MVVM** (Model-View-ViewModel):

1. **Docs/UI**: Composable functions in `composeApp`.
2. **Presentation**: ViewModels (using `koin-compose-viewmodel`) manage state and handle UI events.
3. **Domain/Data**: Repositories and Use Cases (if applicable) handle data fetching from the API (Ktor).

## Key Libraries Versions

(As of Jan 2026)

* **Compose BOM**: 2026.01.00
* **Kotlin**: 2.1.0+
* **Gradle**: 8.14.2+
