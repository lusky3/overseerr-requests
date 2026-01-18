# User Guide

This guide describes the complete feature set of Underseerr.

## 1. Setup & Authentication

### Server Configuration

There are two ways to connect your Overseerr instance:

* **Manual Entry**: Enter your Overseerr URL (e.g., `https://overseerr.example.com`) on the welcome screen.
* **Deep Linking**: Click a structured link to auto-configure the app.
  * Format: `underseerr://setup?server=https://your-domain.com`

### Authentication

* **Plex Login**: Tap "Log in with Plex". You will be redirected to the Plex auth page.
* **App Lock**: If configured, you may be asked to authenticate with Biometrics (fingerprint/face) upon opening the app.

## 2. Dashboard & Discovery

The **Discover** tab is your home screen, featuring:

* **Trending**: Popular Movies and TV Shows.
* **Recommendations**: Content suggested based on your Plex watch history.
* **Search**: Tap the search icon to query TMDB for ANY content (even if not on your server).

## 3. Submitting Requests

1. **Select Media**: Tap on any poster to view details (Plot, Cast, Rating).
2. **Request**: Tap the **Request** button.
3. **Configuration** (if required):
    * **Quality Profile**: Select target quality (e.g., Ultra-HD).
    * **Root Folder**: Choose the library path on your server.
    * **Language**: Select audio/subtitle preferences.
    * **Seasons**: Request specific seasons (for TV).
4. **Submit**: Confirm to send the request to Overseerr.

## 4. Managing Requests

Navigate to the **Requests** tab to track your submissions.

* **Status Indicators**:
  * **Pending Approval**: Waiting for admin review.
  * **Processing**: Approved and sent to Sonarr/Radarr.
  * **Available**: Downloaded and ready to watch on Plex.
  * **Declined**: Rejected by admin.

## 5. Issue Management

Report and track defects in your media library (e.g., "Audio out of sync").

* **Reporting**: Navigate to a media item detail page and select "Report Issue" (if available).
* **Tracking**: Go to the **Issues** tab to see all reports.
  * Filter by **Open**, **Resolved**, or **All**.
* **Actions**:
  * **Resolve**: Mark an issue as fixed.
  * **Reopen**: Reactivate a closed issue.
  * **Comments**: View discussion threads (read-only in list view).

## 6. Settings

Access **Settings** from the Profile tab.

### Appearance

* **Theme**: Switch between **Light**, **Dark**, or **System Default** modes.

### Notifications

Configure push alerts for:

* **Request Approved**
* **Media Available**
* **Request Declined**

### Security

* **Biometric Authentication**: Enable to require Fingerprint/Face ID when opening the app.

### Request Defaults

Set your preferred parameters to skip the configuration step:

* **Default Movie Profile**
* **Default TV Profile**

### Server

* **Manage Servers**: Switch between multiple Overseerr instances or update the current connection.

## 7. Advanced Features

### Deep Links

The app supports deep linking for automation and notifications:

* `underseerr://auth`: Handles user authentication callbacks.
* `underseerr://setup?server=<URL>`: Auto-configure server URL.
* `underseerr://request`: Opens the Requests tab (used by "New Request" notifications).

### Offline Support

* The app caches browsing data, allowing you to view previously loaded content without an internet connection.
