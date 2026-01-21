#!/usr/bin/env bash
set -e
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
export PATH="/opt/homebrew/bin:$PATH"
ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
rm -rf "$ROOT_DIR/iosApp/build"
rm -rf "$ROOT_DIR/composeApp/build"
cd "$ROOT_DIR"
./gradlew clean
