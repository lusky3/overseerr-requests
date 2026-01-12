# Auto-Configured Overseerr Docker Environment

## 🎉 Zero-Configuration Setup

This Docker environment is **fully pre-configured** and ready to use immediately after starting!

## Quick Start (30 seconds)

```bash
cd docker
./setup-overseerr-test.sh
```

That's it! Overseerr is now running and fully configured.

## 🔑 Pre-Configured Credentials

### Overseerr
- **URL**: http://localhost:5055
- **Username**: `admin`
- **Password**: `admin123`
- **API Key**: `test-api-key-overseerr-12345`

### Services
- **Radarr**: http://localhost:7878 (auto-configured)
- **Sonarr**: http://localhost:8989 (auto-configured)
- **Plex Mock**: http://localhost:32400 (auto-configured)

## ✅ What's Pre-Configured

- ✅ Admin user created (admin/admin123)
- ✅ API key generated
- ✅ Radarr connected and configured
- ✅ Sonarr connected and configured
- ✅ Plex mock server connected
- ✅ Default permissions set
- ✅ Quality profiles configured
- ✅ Root folders configured

## 🚀 For Android App Testing

### 1. Get Your IP Address
```bash
hostname -I | awk '{print $1}'
```

### 2. Configure App
Use server URL: `http://YOUR_IP:5055`

### 3. Sign In
- Username: `admin`
- Password: `admin123`

Or use API Key: `test-api-key-overseerr-12345`

## 📋 API Testing

### Test Server Status
```bash
curl http://localhost:5055/api/v1/status
```

### Test with API Key
```bash
curl -H "X-Api-Key: test-api-key-overseerr-12345" \
  http://localhost:5055/api/v1/discover/trending
```

### Test Authentication
```bash
curl -X POST http://localhost:5055/api/v1/auth/local \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@overseerr.local","password":"admin123"}'
```

## 🔧 How It Works

### Automatic Initialization

The `init-overseerr-auto.sh` script runs automatically when the container starts and:

1. Creates the Overseerr database
2. Adds an admin user with hashed password
3. Generates settings.json with all configurations
4. Connects Radarr and Sonarr
5. Sets up Plex mock server
6. Marks Overseerr as initialized

### No Manual Steps Required

Unlike the standard setup, you don't need to:
- ❌ Complete the setup wizard
- ❌ Create an admin account
- ❌ Configure Plex manually
- ❌ Add Radarr/Sonarr manually
- ❌ Set up API keys

Everything is done automatically!

## 📁 File Structure

```
docker/
├── docker-compose.yml          # Service definitions
├── init-overseerr-auto.sh      # Auto-configuration script
├── setup-overseerr-test.sh     # Setup script
├── plex-mock/
│   └── nginx.conf              # Mock Plex server
├── overseerr-config/           # Overseerr data (auto-created)
├── radarr-config/              # Radarr data (auto-created)
└── sonarr-config/              # Sonarr data (auto-created)
```

## 🔄 Reset Everything

To start fresh:

```bash
cd docker
docker compose down
rm -rf overseerr-config radarr-config sonarr-config
./setup-overseerr-test.sh
```

## 🐛 Troubleshooting

### Check if services are running
```bash
docker compose ps
```

### View Overseerr logs
```bash
docker compose logs -f overseerr
```

### Check initialization
```bash
docker compose logs overseerr | grep "Configuration Complete"
```

### Verify database
```bash
docker compose exec overseerr ls -la /config/db/
```

### Test API
```bash
curl http://localhost:5055/api/v1/status
```

## 🎯 Testing Scenarios

### 1. Sign In
- Open http://localhost:5055
- Username: `admin`
- Password: `admin123`
- Should log in immediately

### 2. Browse Media
- Click "Discover"
- Should see trending movies/TV shows
- Search should work

### 3. Submit Request
- Find a movie
- Click "Request"
- Should show quality profiles
- Should submit successfully

### 4. Check Settings
- Go to Settings
- Should see Radarr configured
- Should see Sonarr configured
- API key should be visible

## 🔐 Security Notes

**This is a TEST environment!**

- Default credentials are intentionally simple
- API key is hardcoded for testing
- No HTTPS
- No authentication on mock services

**For production:**
- Change all passwords
- Generate new API keys
- Enable HTTPS
- Use real Plex server
- Configure proper authentication

## 📊 Performance

### Startup Time
- First start: ~30-40 seconds
- Subsequent starts: ~10-15 seconds

### Resource Usage
- CPU: < 5% idle
- Memory: ~500 MB total
- Disk: ~2 GB

## 🎓 Advanced Usage

### Custom Configuration

Edit `init-overseerr-auto.sh` to customize:
- Admin credentials
- API key
- Default permissions
- Service URLs
- Quality profiles

### Multiple Instances

Run multiple instances on different ports:

```yaml
ports:
  - "5056:5055"  # Different port
```

### Persistent Data

Data is stored in:
- `./overseerr-config` - Overseerr database and settings
- `./radarr-config` - Radarr configuration
- `./sonarr-config` - Sonarr configuration

Backup these directories to preserve your setup.

## 📝 Changelog

### v1.0 - Auto-Configuration
- Added automatic initialization script
- Pre-configured admin user
- Auto-connected Radarr and Sonarr
- Zero manual setup required

## 🆘 Support

### Common Issues

**"Services won't start"**
```bash
docker compose logs
```

**"Can't connect from Android"**
```bash
# Check firewall
sudo ufw allow 5055

# Verify IP
hostname -I
```

**"Overseerr shows setup wizard"**
- Check if init script ran: `docker compose logs overseerr | grep "Configuration Complete"`
- If not, restart: `docker compose restart overseerr`

## ✨ Summary

This auto-configured Docker environment provides:

✅ **Zero manual setup** - Everything pre-configured  
✅ **Instant testing** - Ready in 30 seconds  
✅ **Complete integration** - All services connected  
✅ **Realistic environment** - Real Overseerr API  
✅ **Easy reset** - One command to start fresh  

Perfect for:
- Android app development
- API testing
- Integration testing
- Demo purposes
- Learning Overseerr

**No setup wizard. No manual configuration. Just run and test!** 🚀
