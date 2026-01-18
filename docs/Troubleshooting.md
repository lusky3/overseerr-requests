# Troubleshooting

Common issues and how to resolve them.

## Build Failures

### `File google-services.json is missing`

**Cause**: The Firebase configuration file is not present in the expected directory.
**Fix**:

1. Check `androidApp/src/debug/google-services.json`.
2. Check `androidApp/src/release/google-services.json`.
3. Read the [Setup Guide](Setup.md) for instructions on how to obtain these files.

### `Lint Error: AppLinkUrlError`

**Cause**: `AndroidManifest.xml` intent filters for deep links are configured incorrectly (e.g., using `autoVerify="true"` on non-http schemes).
**Fix**: Ensure `android:autoVerify="true"` is ONLY used for `http` or `https` schemes. For custom schemes (e.g., `underseerr://`), remove `autoVerify`.

### Dependency Conflicts (e.g., `protobuf-java`)

**Cause**: Different libraries requesting different versions of a transitive dependency.
**Fix**: Check `build.gradle.kts` for `buildscript { dependencies { constraints { ... } } }` blocks. We sometimes force specific versions to resolve security vulnerabilities.

## Runtime Issues

### App Crashes on Launch

1. Check Logcat in Android Studio: `View > Tool Windows > Logcat`.
2. Filter for `BaseActivity` or `Overseerr`.
3. Ensure your device/emulator has internet access.

### CI/CD Failures

* **"Workflow does not contain permissions"**: Updates to `ci.yml` or `release.yml` must include a strict `permissions` block.
* **"Secrets not found"**: Ensure `FIREBASE_GOOGLE_SERVICES_JSON_DEBUG` is set in the repository secrets.
