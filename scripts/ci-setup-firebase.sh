#!/bin/bash
# Reconstruct google-services.json from environment variable
# Usage: ./scripts/ci-setup-firebase.sh
# Requires: FIREBASE_GOOGLE_SERVICES_JSON environment variable containing the file contents

set -e

if [ -z "$FIREBASE_GOOGLE_SERVICES_JSON" ]; then
    echo "Error: FIREBASE_GOOGLE_SERVICES_JSON environment variable is not set."
    exit 1
fi

echo "Reconstructing androidApp/src/release/google-services.json..."
echo "$FIREBASE_GOOGLE_SERVICES_JSON" > androidApp/src/release/google-services.json
echo "Done."
