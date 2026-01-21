#!/usr/bin/env bash
set -euo pipefail
source "$(dirname "$0")/../.env.ios"

# Sync first
./scripts/ios-sync.sh

# Run remote debug
ssh $IOS_SSH_OPTS "$IOS_USER@$IOS_HOST" "
  xcrun simctl terminate booted app.lusk.iosApp || true
  
  echo 'Launching app...'
  # Clean log
  rm -f /tmp/ios_stdout.log
  touch /tmp/ios_stdout.log
  
  # Start log stream in background to capture KMP logs
  xcrun simctl spawn booted log stream --predicate 'process == \"iOSApp\"' --level debug >> /tmp/ios_stdout.log 2>&1 &
  LOG_PID=\$!
  
  # Launch the app
  xcrun simctl launch --console booted app.lusk.iosApp >> /tmp/ios_stdout.log 2>&1 &
  PID=\$!
  
  echo 'App launched. Tailing logs. Reproduce the crash now!'
  
  # Stream logs
  tail -f /tmp/ios_stdout.log
"
