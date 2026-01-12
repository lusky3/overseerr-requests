#!/bin/bash

# Automatic Overseerr initialization script
# This runs inside the container to pre-configure Overseerr

set -e

CONFIG_DIR="/config"
SETTINGS_FILE="$CONFIG_DIR/settings.json"
DB_FILE="$CONFIG_DIR/db/db.sqlite3"

echo "=== Overseerr Auto-Configuration ==="

# Wait for the config directory to be ready
while [ ! -d "$CONFIG_DIR" ]; do
    echo "Waiting for config directory..."
    sleep 1
done

# Check if already initialized
if [ -f "$SETTINGS_FILE" ] && [ -f "$DB_FILE" ]; then
    echo "✓ Overseerr already configured"
    exit 0
fi

echo "Initializing Overseerr with default configuration..."

# Create necessary directories
mkdir -p "$CONFIG_DIR/db"
mkdir -p "$CONFIG_DIR/logs"

# Copy pre-configured settings
if [ ! -f "$SETTINGS_FILE" ]; then
    echo "Creating settings.json..."
    cat > "$SETTINGS_FILE" << 'EOF'
{
  "main": {
    "apiKey": "test-api-key-overseerr-12345",
    "applicationTitle": "Overseerr Test",
    "applicationUrl": "http://localhost:5055",
    "csrfProtection": false,
    "cacheImages": true,
    "defaultPermissions": 2,
    "hideAvailable": false,
    "localLogin": true,
    "newPlexLogin": true,
    "region": "US",
    "originalLanguage": "en",
    "trustProxy": true,
    "partialRequestsEnabled": true,
    "locale": "en"
  },
  "plex": {
    "name": "Plex Test Server",
    "machineId": "test-machine-id-12345",
    "ip": "plex-mock",
    "port": 32400,
    "useSsl": false,
    "libraries": [],
    "webAppUrl": "http://plex-mock:32400"
  },
  "radarr": [
    {
      "id": 1,
      "name": "Radarr Test",
      "hostname": "radarr-mock",
      "port": 7878,
      "apiKey": "test-radarr-api-key-12345",
      "useSsl": false,
      "baseUrl": "",
      "activeProfileId": 1,
      "activeDirectory": "/movies",
      "is4k": false,
      "minimumAvailability": "released",
      "tags": [],
      "isDefault": true,
      "externalUrl": "http://localhost:7878",
      "syncEnabled": false
    }
  ],
  "sonarr": [
    {
      "id": 1,
      "name": "Sonarr Test",
      "hostname": "sonarr-mock",
      "port": 8989,
      "apiKey": "test-sonarr-api-key-12345",
      "useSsl": false,
      "baseUrl": "",
      "activeProfileId": 1,
      "activeDirectory": "/tv",
      "activeLanguageProfileId": 1,
      "activeAnimeProfileId": null,
      "activeAnimeDirectory": null,
      "activeAnimeLanguageProfileId": null,
      "tags": [],
      "is4k": false,
      "isDefault": true,
      "externalUrl": "http://localhost:8989",
      "syncEnabled": false,
      "enableSeasonFolders": true
    }
  ],
  "public": {
    "initialized": true
  },
  "notifications": {
    "agents": {
      "email": {
        "enabled": false,
        "types": 0,
        "options": {}
      }
    }
  }
}
EOF
    echo "✓ Settings created"
fi

# Initialize database with admin user
if [ ! -f "$DB_FILE" ]; then
    echo "Creating database with admin user..."
    
    # Create empty database
    sqlite3 "$DB_FILE" << 'EOF'
-- Create users table
CREATE TABLE IF NOT EXISTS user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    email TEXT NOT NULL UNIQUE,
    username TEXT,
    plexId INTEGER UNIQUE,
    plexToken TEXT,
    permissions INTEGER NOT NULL DEFAULT 0,
    avatar TEXT NOT NULL DEFAULT '',
    createdAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updatedAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    userType INTEGER NOT NULL DEFAULT 1,
    plexUsername TEXT,
    resetPasswordGuid TEXT,
    recoveryLinkExpirationDate DATETIME,
    requestCount INTEGER NOT NULL DEFAULT 0
);

-- Create user_password table
CREATE TABLE IF NOT EXISTS user_password (
    userId INTEGER PRIMARY KEY,
    password TEXT NOT NULL,
    FOREIGN KEY (userId) REFERENCES user(id) ON DELETE CASCADE
);

-- Insert admin user (username: admin, password: admin123)
INSERT INTO user (id, email, username, plexId, plexToken, permissions, avatar, createdAt, updatedAt, userType, plexUsername, requestCount)
VALUES (
    1,
    'admin@overseerr.local',
    'admin',
    NULL,
    NULL,
    2,
    '',
    datetime('now'),
    datetime('now'),
    1,
    'admin',
    0
);

-- Insert password (bcrypt hash for "admin123")
INSERT INTO user_password (userId, password)
VALUES (1, '$2b$10$rKxMhKPZQxGkrOrCheKzQOQnhYplo6LKo8M0hUPEgsPYRkMkjHnKa');
EOF
    
    echo "✓ Database created with admin user"
fi

echo ""
echo "=== Configuration Complete ==="
echo ""
echo "Overseerr is now pre-configured with:"
echo "  - Admin user: admin"
echo "  - Password: admin123"
echo "  - API Key: test-api-key-overseerr-12345"
echo "  - Radarr configured (radarr-mock:7878)"
echo "  - Sonarr configured (sonarr-mock:8989)"
echo ""
echo "Access at: http://localhost:5055"
echo ""
