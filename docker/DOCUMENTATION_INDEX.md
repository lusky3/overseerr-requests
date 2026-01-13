# Documentation Index

Complete guide to the Overseerr test environment documentation.

## Quick Start

**New to this project?** Start here:

1. [Docker README](README.md) - Overview and quick start guide
2. [Setup Script](setup-overseerr-test.sh) - Run this to set up everything
3. [Quick Start Guide](../docs/QUICK_START.md) - Step-by-step setup instructions

## Configuration Templates

Pre-configured templates for all services:

### Overview

- **[Config Templates Overview](config-templates/README.md)** - Main template documentation
  - Purpose and structure
  - Usage instructions
  - Resetting configurations
  - Modifying templates

### Service-Specific Templates

- **[Overseerr Template](config-templates/overseerr/README.md)**
  - Pre-configured Plex, Radarr, and Sonarr connections
  - Scan schedules optimized for testing
  - Application settings and security notes
  - First-run instructions

- **[Radarr Template](config-templates/radarr/README.md)**
  - Quality profiles and API access
  - Integration with Overseerr
  - API examples and testing
  - Troubleshooting guide

- **[Sonarr Template](config-templates/sonarr/README.md)**
  - Language profiles and season management
  - Integration with Overseerr
  - API examples and testing
  - Troubleshooting guide

## Mock Services

Documentation for mock/test services:

### Plex Mock Server

- **[Plex Mock API Reference](plex-mock/API_REFERENCE.md)** - Complete API endpoint documentation
  - All implemented endpoints
  - Request/response examples
  - Mock data included
  - Testing examples

## Android App Documentation

Documentation for Android app development:

- **[Android Setup Guide](../docs/ANDROID_SETUP.md)** - Setting up Android development
- **[Android App Testing Guide](../docs/ANDROID_APP_TESTING_GUIDE.md)** - Testing the Android app
- **[Quick Start](../docs/QUICK_START.md)** - Getting started quickly

## Docker Environment

Docker-specific documentation:

### Main Files

- **[Docker Compose](compose.yml)** - Service definitions
  - Overseerr, Radarr, Sonarr, Plex Mock
  - Network configuration
  - Health checks

- **[Setup Script](setup-overseerr-test.sh)** - Automated setup
  - Creates configuration directories
  - Copies templates
  - Starts services
  - Waits for health checks

### Mock Server Guides

- **[Overseerr Docker Guide](../docs/OVERSEERR_DOCKER_GUIDE.md)** - Detailed Overseerr setup
- **[Mock Server Guide](../docs/MOCK_SERVER_GUIDE.md)** - Creating mock servers
- **[Mock Server Example](../docs/MOCK_SERVER_EXAMPLE.md)** - Example implementations

## Documentation by Task

### Setting Up the Environment

1. **First Time Setup**
   - [Docker README](README.md) - Start here
   - [Setup Script](setup-overseerr-test.sh) - Run this
   - [Quick Start Guide](../docs/QUICK_START.md) - Follow along

2. **Understanding the Configuration**
   - [Config Templates Overview](config-templates/README.md)
   - [Overseerr Template](config-templates/overseerr/README.md)
   - [Radarr Template](config-templates/radarr/README.md)
   - [Sonarr Template](config-templates/sonarr/README.md)

3. **Verifying the Setup**
   - [Plex Mock API Reference](plex-mock/API_REFERENCE.md) - Test endpoints
   - [Troubleshooting Guide](TROUBLESHOOTING.md) - If issues arise

### Developing the Android App

1. **Environment Setup**
   - [Android Setup Guide](../docs/ANDROID_SETUP.md)
   - [Docker README](README.md) - Backend services

2. **Testing**
   - [Android App Testing Guide](../docs/ANDROID_APP_TESTING_GUIDE.md)
   - [Plex Mock API Reference](plex-mock/API_REFERENCE.md)

3. **Integration**
   - [Overseerr Template](config-templates/overseerr/README.md) - API endpoints
   - [Radarr Template](config-templates/radarr/README.md) - Movie API
   - [Sonarr Template](config-templates/sonarr/README.md) - TV show API

### Troubleshooting

1. **Connection Issues**
   - [Plex Mock Fix](PLEX_MOCK_FIX.md) - Recent fixes
   - [Troubleshooting Guide](TROUBLESHOOTING.md) - Debugging steps

2. **Configuration Issues**
   - [Config Templates Overview](config-templates/README.md) - Reset instructions
   - Service-specific READMEs for detailed troubleshooting

3. **Service-Specific Issues**
   - [Overseerr Template](config-templates/overseerr/README.md#troubleshooting)
   - [Radarr Template](config-templates/radarr/README.md#troubleshooting)
   - [Sonarr Template](config-templates/sonarr/README.md#troubleshooting)

### Customizing the Environment

1. **Modifying Templates**
   - [Config Templates Overview](config-templates/README.md#modifying-templates)
   - Service-specific customization guides

2. **Adding Mock Endpoints**
   - [Mock Server Guide](../docs/MOCK_SERVER_GUIDE.md)
   - [Plex Mock API Reference](plex-mock/API_REFERENCE.md)

3. **Updating Configuration**
   - [Setup Improvements](SETUP_IMPROVEMENTS.md) - Recent changes
   - Service-specific template READMEs

## Documentation by Service

### Overseerr

- [Template Configuration](config-templates/overseerr/README.md)
- [Docker Guide](../docs/OVERSEERR_DOCKER_GUIDE.md)
- [Setup Complete](../docs/OVERSEERR_SETUP_COMPLETE.md)

### Radarr

- [Template Configuration](config-templates/radarr/README.md)
- API examples in template README

### Sonarr

- [Template Configuration](config-templates/sonarr/README.md)
- API examples in template README

### Plex Mock

- [API Reference](plex-mock/API_REFERENCE.md)
- [Mock Server Summary](../docs/MOCK_SERVER_SUMMARY.md)

## Quick Reference

### Common Commands

```bash
# Start everything
cd docker
./setup-overseerr-test.sh

# View logs
docker compose logs -f overseerr
docker compose logs -f radarr-mock
docker compose logs -f sonarr-mock
docker compose logs -f plex-mock

# Restart services
docker compose restart

# Stop everything
docker compose down

# Clean reset
docker compose down
rm -rf overseerr-config radarr-config sonarr-config
./setup-overseerr-test.sh
```

### Service URLs

- **Overseerr**: <http://localhost:5055>
- **Radarr**: <http://localhost:7878>
- **Sonarr**: <http://localhost:8989>
- **Plex Mock**: <http://localhost:32400>

### API Keys

All services use the same API key for simplicity:

```text
1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x1x
```

⚠️ **Test environment only!**

## Contributing

When adding new documentation:

1. **Create the document** in the appropriate directory
2. **Update this index** with a link and description
3. **Cross-reference** related documents
4. **Test all commands** and examples
5. **Keep it concise** - link to details rather than duplicating

## Documentation Standards

All documentation should include:

- **Clear title** - What this document covers
- **Purpose** - Why this document exists
- **Prerequisites** - What you need before starting
- **Step-by-step instructions** - How to accomplish tasks
- **Examples** - Real commands and code
- **Troubleshooting** - Common issues and solutions
- **References** - Links to related documentation

## Getting Help

If you can't find what you need:

1. **Check this index** - Find the right document
2. **Search the docs** - Use grep or your editor's search
3. **Check the logs** - `docker compose logs <service>`
4. **Review troubleshooting** - [Troubleshooting Guide](TROUBLESHOOTING.md)
5. **Check GitHub issues** - See if others have the same problem

## Document Status

| Document | Status | Last Updated |
| -------- | ------ | ------------ |
| Docker README | ✅ Complete | Current |
| Config Templates Overview | ✅ Complete | Current |
| Overseerr Template | ✅ Complete | Current |
| Radarr Template | ✅ Complete | Current |
| Sonarr Template | ✅ Complete | Current |
| Plex Mock API Reference | ✅ Complete | Current |
| Plex Mock Fix | ✅ Complete | Current |
| Troubleshooting Guide | ⚠️ In Progress | Current |
| Setup Improvements | ✅ Complete | Current |
| Android Setup | ✅ Complete | Previous |
| Android Testing | ✅ Complete | Previous |

Legend:

- ✅ Complete - Fully documented and tested
- ⚠️ In Progress - Partially complete or being updated
- 🔄 Needs Update - Outdated or needs revision
- 📝 Planned - Not yet created

## Feedback

Found an issue with the documentation?

- **Typos/errors**: Fix them and submit a PR
- **Missing information**: Add it and update this index
- **Unclear instructions**: Clarify and improve
- **Broken links**: Fix them immediately

Good documentation is a team effort!
