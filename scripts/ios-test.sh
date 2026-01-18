#!/usr/bin/env bash
set -euo pipefail
source "$(dirname "$0")/../.env.ios"

./scripts/ios-sync.sh

ssh $IOS_SSH_OPTS "$IOS_USER@$IOS_HOST" ios-test.sh
