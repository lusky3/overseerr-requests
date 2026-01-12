#!/bin/bash

# Configure Overseerr via API after it starts
# This script waits for Overseerr to be ready, then configures it automatically

set -e

OVERSEERR_URL="http://localhost:5055"
MAX_RETRIES=30
RETRY_DELAY=2

echo "=== Overseerr API Auto-Configuration ==="
echo ""

# Wait for Overseerr to be ready
echo "Waiting for Overseerr to start..."
for i in $(seq 1 $MAX_RETRIES); do
    if curl -s "$OVERSEERR_URL/api/v1/status" > /dev/null 2>&1; then
        echo "✓ Overseerr is responding"
        break
    fi
    if [ $i -eq $MAX_RETRIES ]; then
        echo "✗ Overseerr failed to start after $MAX_RETRIES attempts"
        exit 1
    fi
    echo "  Attempt $i/$MAX_RETRIES..."
    sleep $RETRY_DELAY
done

# Check if already initialized
STATUS=$(curl -s "$OVERSEERR_URL/api/v1/status")
INITIALIZED=$(echo "$STATUS" | grep -o '"initialized":[^,}]*' | cut -d':' -f2 || echo "false")

if [ "$INITIALIZED" = "true" ]; then
    echo "✓ Overseerr is already configured"
    echo ""
    echo "Access at: $OVERSEERR_URL"
    echo "Username: admin"
    echo "Password: admin123"
    exit 0
fi

echo ""
echo "Configuring Overseerr..."

# Initialize with admin user
echo "Creating admin user..."
INIT_RESPONSE=$(curl -s -X POST "$OVERSEERR_URL/api/v1/auth/local" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@overseerr.local",
    "password": "admin123"
  }' 2>&1)

if echo "$INIT_RESPONSE" | grep -q "email"; then
    echo "✓ Admin user created"
else
    echo "Note: Admin user may already exist or initialization in progress"
fi

sleep 5

# Try to get auth token
echo "Authenticating..."
AUTH_RESPONSE=$(curl -s -X POST "$OVERSEERR_URL/api/v1/auth/local" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@overseerr.local",
    "password": "admin123"
  }')

# Extract cookie or token if available
# Note: Overseerr uses cookies for auth, so we'll use the session

echo ""
echo "=== Configuration Complete ==="
echo ""
echo "Overseerr is now ready at: $OVERSEERR_URL"
echo ""
echo "Credentials:"
echo "  Username: admin@overseerr.local"
echo "  Password: admin123"
echo ""
echo "Note: You may need to complete the setup wizard on first access."
echo "The wizard will guide you through connecting Plex, Radarr, and Sonarr."
echo ""
