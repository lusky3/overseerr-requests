# iOS Remote Development Scripts

This directory contains scripts for developing the iOS application on a remote macOS host from a variety of environments (Linux, WSL, etc.).

## 🤖 Instructions for AI Agents/LLMs

When asked to "build", "test", or "run" the iOS app, ALWAYS use the `ios-remote` wrapper script. Do not try to run individual scripts or gradle commands manually unless debugging a specific script issue.

### Primary Command

```bash
./scripts/ios-remote <command>
```

### Available Commands

| Command | Action | When to use |
| :--- | :--- | :--- |
| `build` | Syncs code & builds via `xcodebuild` | Verifying compilation, checking for Swift errors. |
| `test` | Syncs code & runs KMP tests | Verifying logic, regression testing. |
| `run` | Syncs, installs, and launches app | Manually checking the app on the simulator. |
| `debug` | Launches app & streams logs | specific "Debug this crash" tasks. Returns logs to stdout. |
| `screenshot` | Captures simulator screenshot | Verifying UI layout. Saves to `./ios-screenshot.png`. |
| `clean` | Wipes build artifacts | Fixing weird build errors or stale state. |

### Workflow Example

1. **User:** "The app is crashing on launch."
2. **Agent:** `./scripts/ios-remote debug`
3. **User:** "Add a new button and verify it renders."
4. **Agent:** (Edits code) -> `./scripts/ios-remote build` (to check valid code) -> `./scripts/ios-remote screenshot` (to verify visual).

## Architecture

* **Local:** `ios-*.sh` scripts source `.env.ios`, run `ios-sync.sh` to `rsync` files to the remote, and then `ssh` to trigger the remote script.
* **Remote:** `ios-*-remote.sh` scripts run on the macOS host to execute Xcode or Gradle commands.

## Configuration

Ensure `.env.ios` exists in the project root with:

```bash
IOS_HOST=192.168.1.x
IOS_USER=user
IOS_SSH_OPTS="-i /path/to/key"
IOS_REMOTE_ROOT=/Users/user/git/stream_app
```
