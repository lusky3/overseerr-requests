# CI/CD Workflows

We use **GitHub Actions** for Continuous Integration and Continuous Deployment.

## Workflows

### 1. Continuous Integration (`ci.yml`)

Triggered on: `push` to `main` and `pull_request`.

**Jobs:**

* **Build & Test**:
  * Sets up JDK 24.
  * Injects `google-services.json` from `FIREBASE_GOOGLE_SERVICES_JSON_DEBUG` secret.
  * Runs Unit Tests (`./gradlew testDebugUnitTest`).
  * Runs Lint Checks (`./gradlew lintDebug`).
  * Generates JaCoCo Coverage Report.
  * Uploads Coverage Report and Lint Results as artifacts.
  * Assembles the Debug APK (`./gradlew assembleDebug`).

### 2. Release (`release.yml`)

Triggered on: Pushing a tag (e.g., `v1.0.0`).

**Jobs:**

* **Release**:
  * Sets up JDK 24.
  * Injects **Release** `google-services.json` from `FIREBASE_GOOGLE_SERVICES_JSON_RELEASE` secret.
  * Builds the Release APK (`./gradlew assembleRelease`).
  * *(Future)*: Signs the APK (requires Keystore secrets).
  * Creates a generic GitHub Release with the APK attached.

### 3. Code Quality (`sonarqube.yml`)

Triggered on: `push` to `main` and `pull_request`.

**Jobs:**

* **SonarQube Analysis**:
  * Sets up JDK 24.
  * Runs Gradle Sonar analysis (`./gradlew :androidApp:assembleDebug :composeApp:compileAndroidMain sonar`).
  * Reports to SonarCloud.

## Secrets & Variables

To run these workflows successfully, the following Repository Secrets/Variables must be configured in GitHub:

| Name | Type | Description |
|------|------|-------------|
| `FIREBASE_GOOGLE_SERVICES_JSON_DEBUG` | Secret | Content of the Debug `google-services.json` |
| `FIREBASE_GOOGLE_SERVICES_JSON_RELEASE` | Secret | Content of the Release `google-services.json` |

## Security

* **Permissions**: Workflows have restricted permissions (`contents: read`, etc.) to follow least-privilege principles.
* **Dependabot**: Automatically checks for dependency updates and security vulnerabilities.
