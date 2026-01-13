# Sonarr Configuration Template

This directory contains pre-configured files for Sonarr that set up a complete test environment.

## What's Included

The template provides a fully configured Sonarr instance with:

### Configuration Files

#### config.xml

- API Key: `1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x`
- Port: 8989
- Authentication: None (for testing)
- Instance Name: Sonarr
- URL Base: (empty)
- SSL: Disabled

#### sonarr.db

- Pre-initialized SQLite database
- Quality profiles configured
- Language profiles configured
- System settings
- API key stored in database

### Quality Profiles

The database includes standard quality profiles:

- **Any**: Accepts any quality
- **SD**: Standard definition
- **HD-720p**: 720p high definition
- **HD-1080p**: 1080p full HD
- **Ultra-HD**: 4K content

### Language Profiles

Default language profile:

- **English**: English language content

### Root Folders

Default root folder configured:

- Path: `/tmp` (for testing, no actual downloads)

## Usage

The setup script (`setup-overseerr-test.sh`) automatically:

1. Creates the `sonarr-config` directory if it doesn't exist
2. Copies both `config.xml` and `sonarr.db` from this template
3. Starts the Sonarr container

## First Run

When Sonarr starts:

1. Reads `config.xml` for basic configuration
2. Opens the SQLite database (`sonarr.db`)
3. Applies any pending migrations
4. Starts the web interface on port 8989

## Accessing Sonarr

Once running, access Sonarr at:

- **From host**: <http://localhost:8989>
- **From Docker network**: <http://sonarr-mock:8989>

**No authentication required** - This is a test environment!

## API Access

The API is available at:

```text
http://localhost:8989/api/v3/
```

**API Key**: `1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x`

### Example API Calls

**Get system status:**

```bash
curl http://localhost:8989/api/v3/system/status?apikey=1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x
```

**List quality profiles:**

```bash
curl http://localhost:8989/api/v3/qualityprofile?apikey=1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x
```

**Search for a TV show:**

```bash
curl "http://localhost:8989/api/v3/series/lookup?term=breaking%20bad&apikey=1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x"
```

**List language profiles:**

```bash
curl http://localhost:8989/api/v3/languageprofile?apikey=1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x
```

## Integration with Overseerr

The template is designed to work seamlessly with Overseerr:

**Overseerr Configuration:**

- Hostname: `sonarr-mock`
- Port: `8989`
- API Key: `1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x`
- Use SSL: `false`
- Base URL: (empty)

The Overseerr template already includes this configuration.

## Test-Friendly Settings

The template includes settings optimized for testing:

### Search Prevention

- **Prevent Search**: Enabled in Overseerr integration
- **Why**: Prevents actual download attempts during testing
- **Benefit**: No need for indexers or download clients

### Season Folders

- **Enable Season Folders**: Disabled
- **Why**: Simplifies testing without complex folder structures
- **Benefit**: Easier to verify API responses

### No Download Clients

- No download clients configured
- TV shows can be added but won't actually download
- Perfect for testing request workflows

### No Indexers

- No indexers configured
- Search functionality won't work (by design)
- Focuses testing on API integration

## Database Schema

The database is pre-initialized with Sonarr v4.x schema including:

**Core Tables:**

- `Config` - System configuration
- `Series` - TV show metadata
- `Episodes` - Episode information
- `QualityProfiles` - Quality settings
- `LanguageProfiles` - Language preferences
- `RootFolders` - Storage locations
- `Tags` - Organization tags
- `Metadata` - TV show metadata settings

**Version**: Compatible with Sonarr v4.x (latest stable)

## Resetting Configuration

To reset Sonarr to template defaults:

```bash
cd docker
docker compose down
rm -rf sonarr-config
./setup-overseerr-test.sh
```

This will:

- Remove all Sonarr data (series, episodes, history, etc.)
- Restore the template configuration
- Start fresh with a clean database

## Customizing the Template

To modify the template for your needs:

1. **Start Sonarr and make changes via the web UI**

   ```bash
   cd docker
   ./setup-overseerr-test.sh
   # Open http://localhost:8989
   # Make your changes
   ```

2. **Copy the updated files back to the template**

   ```bash
   docker compose stop sonarr-mock
   cp sonarr-config/config.xml config-templates/sonarr/
   cp sonarr-config/sonarr.db config-templates/sonarr/
   ```

3. **Clean up temporary files**

   ```bash
   # Remove SQLite temporary files
   rm config-templates/sonarr/sonarr.db-shm 2>/dev/null
   rm config-templates/sonarr/sonarr.db-wal 2>/dev/null
   ```

4. **Commit the changes**

   ```bash
   git add config-templates/sonarr/
   git commit -m "Update Sonarr template configuration"
   ```

## Files Created at Runtime

When Sonarr runs, it creates additional files in `sonarr-config/`:

**Configuration:**

- `config.xml` - Main configuration (from template)
- `sonarr.db` - Database (from template)

**Runtime Files (not in template):**

- `sonarr.db-shm` - SQLite shared memory
- `sonarr.db-wal` - SQLite write-ahead log
- `sonarr.pid` - Process ID file
- `logs/` - Application logs
- `logs.db` - Log database
- `MediaCover/` - Cached TV show posters
- `Backups/` - Automatic database backups

These runtime files are excluded from git via `.gitignore`.

## Security Notes

⚠️ **This configuration is for TESTING ONLY!**

The template includes:

- **No authentication** - Anyone can access the web UI
- **Generic API key** - Easy to remember but not secure
- **No SSL** - Unencrypted communication
- **Exposed on all interfaces** - Accessible from network

**DO NOT use this configuration in production!**

For production:

1. Enable authentication in Settings > General
2. Generate a unique API key
3. Use SSL/TLS with a reverse proxy
4. Restrict network access with firewall rules
5. Use strong passwords

## Troubleshooting

### Sonarr won't start

**Check logs:**

```bash
docker compose logs sonarr-mock
```

**Common issues:**

- Port 8989 already in use
- Database corruption
- Permission issues with config files

### Database is corrupted

**Reset from template:**

```bash
docker compose stop sonarr-mock
rm sonarr-config/sonarr.db*
cp config-templates/sonarr/sonarr.db sonarr-config/
docker compose start sonarr-mock
```

### API key not working

**Verify the key in the database:**

```bash
docker exec sonarr-mock cat /config/config.xml | grep ApiKey
```

Should show: `<ApiKey>1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x</ApiKey>`

### Can't connect from Overseerr

**Check network connectivity:**

```bash
docker exec overseerr-test curl http://sonarr-mock:8989/api/v3/system/status?apikey=1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x
```

Should return JSON with system status.

### TV shows not downloading

This is **expected behavior**! The template has:

- No indexers configured
- No download clients configured
- Search prevention enabled in Overseerr

This is intentional for testing the request workflow without actual downloads.

### Language profile errors

If you see language profile errors:

1. Check that language profiles exist in the database
2. Verify the language profile ID in Overseerr matches Sonarr
3. Default language profile ID is usually `1` (English)

## Sonarr vs Radarr

Key differences in the Sonarr template:

**Additional Features:**

- **Language Profiles**: TV shows support multiple languages
- **Season Management**: Episodes organized by seasons
- **Episode Monitoring**: Track individual episodes
- **Series Types**: Standard, Daily, Anime

**API Differences:**

- `/api/v3/series` instead of `/api/v3/movie`
- `/api/v3/episode` for episode management
- `/api/v3/languageprofile` for language settings

## Version Compatibility

**Sonarr Version**: v4.x (latest stable)

The template is compatible with:

- Sonarr v4.0.0 and newer
- LinuxServer.io Docker image: `lscr.io/linuxserver/sonarr:latest`

If you need to upgrade:

1. Let Sonarr perform automatic database migrations
2. Test thoroughly
3. Copy the upgraded database back to the template
4. Update this README with the new version

## API Documentation

For full API documentation, see:

- [Sonarr API Docs](https://sonarr.tv/docs/api/)
- [Sonarr Wiki](https://wiki.servarr.com/sonarr)

## Integration Testing

The template is designed for testing:

**Overseerr Integration:**

- Request TV shows through Overseerr
- Verify API calls to Sonarr
- Check series status updates
- Test quality and language profile selection
- Test season monitoring

**Android App Testing:**

- Configure app with Sonarr endpoint
- Test TV show search
- Test adding series
- Test viewing series status
- Test episode tracking

**API Testing:**

- Test authentication with API key
- Test series lookup
- Test adding series via API
- Test quality profile retrieval
- Test language profile retrieval
- Test episode management

## Common Use Cases

### Adding a TV Show via API

```bash
# 1. Search for a show
curl "http://localhost:8989/api/v3/series/lookup?term=breaking%20bad&apikey=1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x"

# 2. Add the show (use tvdbId from search results)
curl -X POST http://localhost:8989/api/v3/series?apikey=1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x \
  -H "Content-Type: application/json" \
  -d '{
    "tvdbId": 81189,
    "title": "Breaking Bad",
    "qualityProfileId": 1,
    "languageProfileId": 1,
    "rootFolderPath": "/tmp",
    "monitored": true,
    "addOptions": {
      "searchForMissingEpisodes": false
    }
  }'
```

### Monitoring Specific Seasons

```bash
# Get series ID first
SERIES_ID=$(curl -s "http://localhost:8989/api/v3/series?apikey=1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x" | jq '.[0].id')

# Update season monitoring
curl -X PUT http://localhost:8989/api/v3/series/$SERIES_ID?apikey=1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x \
  -H "Content-Type: application/json" \
  -d '{
    "seasons": [
      {"seasonNumber": 1, "monitored": true},
      {"seasonNumber": 2, "monitored": false}
    ]
  }'
```

## Related Files

- `config-templates/sonarr/config.xml` - Configuration file
- `config-templates/sonarr/sonarr.db` - Database file
- `docker/compose.yml` - Docker Compose configuration
- `docker/setup-overseerr-test.sh` - Setup script
- `config-templates/README.md` - Overall template documentation

## References

- [Sonarr Official Site](https://sonarr.tv/)
- [Sonarr GitHub](https://github.com/Sonarr/Sonarr)
- [Sonarr Wiki](https://wiki.servarr.com/sonarr)
- [LinuxServer.io Sonarr Image](https://docs.linuxserver.io/images/docker-sonarr)
