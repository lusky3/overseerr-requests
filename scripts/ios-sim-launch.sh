#!/usr/bin/env bash
set -e

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
APP_PATH="$ROOT_DIR/iosApp/build/Build/Products/Debug-iphonesimulator/iOSApp.app"
BUNDLE_ID="app.lusk.iosApp"

echo "Installing app from $APP_PATH..."
xcrun simctl install "iPhone 14" "$APP_PATH"

echo "Launching app..."
xcrun simctl launch "iPhone 14" "$BUNDLE_ID"
