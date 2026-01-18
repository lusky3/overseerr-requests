#!/usr/bin/env bash
set -euo pipefail
source "$(dirname "$0")/../.env.ios"

rsync -azv --delete \
  --exclude .git \
  --filter=':- .gitignore' \
  ./ \
  "$IOS_USER@$IOS_HOST:$IOS_REMOTE_ROOT"
