#!/usr/bin/env bash
set -e

echo "Booting simulator iPhone 14..."
xcrun simctl boot "iPhone 14" || true
open -a Simulator
