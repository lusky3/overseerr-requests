# Compose Multiplatform (CMP) Transition Roadmap

This document outlines the phased migration of the Overseerr Android client to a Compose Multiplatform (CMP) project, targeting both Android and iOS.

## Phase 1: Project Restructuring & Infrastructure

**Goal:** Convert the single-module Android project into a multi-module KMP structure.

### 1. Repository Structure (Monorepo)

* Rename the current `app` module to `androidApp`.
* Create a `composeApp` module for shared logic and UI.
* **Directory Layout:**

    ```text
    /composeApp/src/commonMain/      <-- Shared Logic/UI
    /composeApp/src/androidMain/     <-- Android-specific implementations
    /composeApp/src/iosMain/         <-- iOS-specific implementations
    /iosApp/                         <-- Xcode Project
    ```

### 2. Gradle Configuration

* Update `libs.versions.toml` with:
  * `kotlin = "2.1.0"`
  * `compose-multiplatform = "1.7.0"`
  * `ktor = "3.0.0"`
  * `koin = "4.0.0"`
* Configure the `kotlin("multiplatform")` plugin in `composeApp/build.gradle.kts`.

---

## Phase 2: Domain & Data Layer Porting

**Goal:** Migrate networking, database, and mappers to `commonMain`.

### 1. Networking (Retrofit → Ktor)

* **Replace:** `Retrofit` + `OkHttp` with `Ktor Client`.
* **Implementation:**
  * Use `ContentNegotiation` plugin with `kotlinx.serialization`.
  * Implement an `AuthInterceptor` equivalent using Ktor's `defaultRequest` or `Auth` plugin.
  * Move `ApiMediaRequest` and complex mapping logic from `Mappers.kt` to `commonMain`.
* **Verification:** Ensure status code `4` and `5` mapping to `AVAILABLE` remains intact.

### 2. Database (Room Android → Room KMP)

* **Move:** All `@Entity`, `@Dao`, and the `OverseerrDatabase` class to `commonMain`.
* **Abstraction:** Use `expect/actual` for building the database:
  * `androidMain`: Uses `context.getDatabasePath()`.
  * `iosMain`: Uses `NSHomeDirectory()` documents path.
* **Drivers:** Use `BundledSQLiteDriver` for consistent behavior across platforms.

### 3. Model Persistence

* Move all domain models (e.g., `MediaRequest`, `RequestStatus`) to `commonMain`.
* Ensure `IntListConverter` (TypeConverter) is shared to maintain season data integrity.

---

## Phase 3: Dependency Injection Overhaul

**Goal:** Replace Android-specific Hilt with platform-agnostic Koin.

### 1. Framework Swap (Hilt → Koin)

* **Remove:** `@HiltAndroidApp`, `@AndroidEntryPoint`, and all `@Inject` / `@Module` components.
* **New Setup:**

    ```kotlin
    // commonMain DSL
    val appModule = module {
        single { KmpRequestApiService(get()) }
        single<RequestRepository> { RequestRepositoryImpl(get(), get(), ...) }
        viewModel { ProfileViewModel(get(), get()) }
    }
    ```

### 2. ViewModel Migration

* Migrate `androidx.lifecycle.ViewModel` to the Multiplatform-compatible version.
* Update Composables to use `koinViewModel()` instead of `hiltViewModel()`.

### 3. Platform Context Injection

* Use `androidContext()` during Koin initialization on Android to support libraries needing `Context` (like DataStore or Room).
* Initialize Koin directly via `MainViewController` on iOS.

---

## Phase 4: UI Shared Components (Future)

* Move Screen Composables (Discovery, Details, Requests) to `commonMain`.
* Abstract `AsyncImage` (Coil 3) to work without Android `Context`.
* Replace Android Navigation with a KMP-compatible solution (Voyager or CMP Navigation).

## Phase 5: iOS Integration (Future)

* Establish iOS entry point in Xcode.
* Hook up background sync logic via `iosMain` actuals using Apple's BackgroundTasks framework.
