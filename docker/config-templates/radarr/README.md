# Radarr Configuration Template

This directory contains pre-configured files for Radarr that set up a complete test environment.

## What's Included

The template provides a fully configured Radarr instance with:

### Configuration Files

#### config.xml

- API Key: `1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x`
- Port: 7878
- Authentication: None (for testing)
- Instance Name: Radarr
- URL Base: (empty)
- SSL: Disabled

#### radarr.db

- Pre-initialized SQLite database
- Quality profiles configured
- System settings
- API key stored in database

### Quality Profiles

The database includes standard quality profiles:

- **Any**: Accepts any quality
- **SD**: Standard definition
- **HD-720p**: 720p high definition
- **HD-1080p**: 1080p full HD
- **Ultra-HD**: 4K content

### Root Folders

Default root folder configured:

- Path: `/tmp` (for testing, no actual downloads)

## Usage

The setup script (`setup-overseerr-test.sh`) automatically:

1. Creates the `radarr-config` directory if it doesn't exist
2. Copies both `config.xml` and `radarr.db` from this template
3. Starts the Radarr container

## First Run

When Radarr starts:

1. Reads `config.xml` for basic configuration
2. Opens the SQLite database (`radarr.db`)
3. Applies any pending migrations
4. Starts the web interface on port 7878

## Accessing Radarr

Once running, access Radarr at:

- **From host**: <http://localhost:7878>
- **From Docker network**: <http://radarr-mock:7878>

**No authentication required** - This is a test environment!

## API Access

The API is available at:

```text
http://localhost:7878/api/v3/
```

**API Key**: `1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x`

### Example API Calls

**Get system status:**

```bash
curl http://localhost:7878/api/v3/system/status?apikey=1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x
```

**List quality profiles:**

```bash
curl http://localhost:7878/api/v3/qualityprofile?apikey=1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x
```

**Search for a movie:**

```bash
curl "http://localhost:7878/api/v3/movie/lookup?term=matrix&apikey=1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x"
```

## Integration with Overseerr

The template is designed to work seamlessly with Overseerr:

**Overseerr Configuration:**

- Hostname: `radarr-mock`
- Port: `7878`
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

### No Download Clients

- No download clients configured
- Movies can be added but won't actually download
- Perfect for testing request workflows

### No Indexers

- No indexers configured
- Search functionality won't work (by design)
- Focuses testing on API integration

## Database Schema

The database is pre-initialized with Radarr v5.x schema including:

**Core Tables:**

- `Config` - System configuration
- `Movies` - Movie metadata
- `QualityProfiles` - Quality settings
- `RootFolders` - Storage locations
- `Tags` - Organization tags
- `Metadata` - Movie metadata settings

**Version**: Compatible with Radarr v5.x (latest stable)

## Resetting Configuration

To reset Radarr to template defaults:

```bash
cd docker
docker compose down
rm -rf radarr-config
./setup-overseerr-test.sh
```

This will:

- Remove all Radarr data (movies, history, etc.)
- Restore the template configuration
- Start fresh with a clean database

## Customizing the Template

To modify the template for your needs:

1. **Start Radarr and make changes via the web UI**

   ```bash
   cd docker
   ./setup-overseerr-test.sh
   # Open http://localhost:7878
   # Make your changes
   ```

2. **Copy the updated files back to the template**

   ```bash
   docker compose stop radarr-mock
   cp radarr-config/config.xml config-templates/radarr/
   cp radarr-config/radarr.db config-templates/radarr/
   ```

3. **Clean up temporary files**

   ```bash
   # Remove SQLite temporary files
   rm config-templates/radarr/radarr.db-shm 2>/dev/null
   rm config-templates/radarr/radarr.db-wal 2>/dev/null
   ```

4. **Commit the changes**

   ```bash
   git add config-templates/radarr/
   git commit -m "Update Radarr template configuration"
   ```

## Files Created at Runtime

When Radarr runs, it creates additional files in `radarr-config/`:

**Configuration:**

- `config.xml` - Main configuration (from template)
- `radarr.db` - Database (from template)

**Runtime Files (not in template):**

- `radarr.db-shm` - SQLite shared memory
- `radarr.db-wal` - SQLite write-ahead log
- `radarr.pid` - Process ID file
- `logs/` - Application logs
- `logs.db` - Log database
- `MediaCover/` - Cached movie posters
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

### Radarr won't start

**Check logs:**

```bash
docker compose logs radarr-mock
```

**Common issues:**

- Port 7878 already in use
- Database corruption
- Permission issues with config files

### Database is corrupted

**Reset from template:**

```bash
docker compose stop radarr-mock
rm radarr-config/radarr.db*
cp config-templates/radarr/radarr.db radarr-config/
docker compose start radarr-mock
```

### API key not working

**Verify the key in the database:**

```bash
docker exec radarr-mock cat /config/config.xml | grep ApiKey
```

Should show: `<ApiKey>1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x</ApiKey>`

### Can't connect from Overseerr

**Check network connectivity:**

```bash
docker exec overseerr-test curl http://radarr-mock:7878/api/v3/system/status?apikey=1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x
```

Should return JSON with system status.

### Movies not downloading

This is **expected behavior**! The template has:

- No indexers configured
- No download clients configured
- Search prevention enabled in Overseerr

This is intentional for testing the request workflow without actual downloads.

## Version Compatibility

**Radarr Version**: v5.x (latest stable)

The template is compatible with:

- Radarr v5.0.0 and newer
- LinuxServer.io Docker image: `lscr.io/linuxserver/radarr:latest`

If you need to upgrade:

1. Let Radarr perform automatic database migrations
2. Test thoroughly
3. Copy the upgraded database back to the template
4. Update this README with the new version

## API Documentation

For full API documentation, see:

- [Radarr API Docs](https://radarr.video/docs/api/)
- [Radarr Wiki](https://wiki.servarr.com/radarr)

## Integration Testing

The template is designed for testing:

**Overseerr Integration:**

- Request movies through Overseerr
- Verify API calls to Radarr
- Check movie status updates
- Test quality profile selection

**Android App Testing:**

- Configure app with Radarr endpoint
- Test movie search
- Test adding movies
- Test viewing movie status

**API Testing:**

- Test authentication with API key
- Test movie lookup
- Test adding movies via API
- Test quality profile retrieval

## Related Files

- `config-templates/radarr/config.xml` - Configuration file
- `config-templates/radarr/radarr.db` - Database file
- `docker/compose.yml` - Docker Compose configuration
- `docker/setup-overseerr-test.sh` - Setup script
- `config-templates/README.md` - Overall template documentation

## References

- [Radarr Official Site](https://radarr.video/)
- [Radarr GitHub](https://github.com/Radarr/Radarr)
- [Radarr Wiki](https://wiki.servarr.com/radarr)
- [LinuxServer.io Radarr Image](https://docs.linuxserver.io/images/docker-radarr)
