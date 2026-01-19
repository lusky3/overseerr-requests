#!/usr/bin/env bash
set -euo pipefail
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
export PATH="/opt/homebrew/bin:$PATH"

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT_DIR"

echo "Running KMP iOS Tests..."
# Verify KMP tests
./gradlew :composeApp:iosSimulatorArm64Test
