# Quick Setup Guide - Overseerr Docker

## 🚀 Fastest Setup (2 minutes)

### Step 1: Start Services
```bash
cd docker
./setup-overseerr-test.sh
```

### Step 2: Complete One-Time Setup

Open http://localhost:5055 and follow these quick steps:

#### 2.1 Create Admin Account (30 seconds)
1. Click "Sign in"
2. Click "Use your Overseerr account"
3. Enter:
   - Email: `admin@overseerr.local`
   - Username: `admin`
   - Password: `admin123`
4. Click "Sign in"

#### 2.2 Skip Plex (Optional)
- Click "Skip" on Plex setup
- Or configure if you have a real Plex server

#### 2.3 Configure Radarr (30 seconds)
1. Click "Add Radarr Server"
2. Enter:
   - Server Name: `Radarr Test`
   - Hostname: `radarr-mock`
   - Port: `7878`
   - API Key: Get from http://localhost:7878/settings/general
   - Quality Profile: Select any
   - Root Folder: `/movies`
3. Click "Test" then "Save"

#### 2.4 Configure Sonarr (30 seconds)
1. Click "Add Sonarr Server"
2. Enter:
   - Server Name: `Sonarr Test`
   - Hostname: `sonarr-mock`
   - Port: `8989`
   - API Key: Get from http://localhost:8989/settings/general
   - Quality Profile: Select any
   - Root Folder: `/tv`
3. Click "Test" then "Save"

#### 2.5 Finish
- Click "Finish Setup"
- Done! 🎉

## 📱 For Android App

### Get Your IP
```bash
hostname -I | awk '{print $1}'
```

### Configure App
- Server URL: `http://YOUR_IP:5055`
- Username: `admin@overseerr.local`
- Password: `admin123`

## 🔑 Get API Key

After setup:
1. Sign in to Overseerr
2. Go to Settings → General
3. Copy your API Key
4. Use in API requests

## ⚡ Even Faster (If You've Done This Before)

If you have a previous `overseerr-config` backup:

```bash
# Restore previous config
cp -r /path/to/backup/overseerr-config docker/

# Start services
cd docker
docker compose up -d
```

Everything will be pre-configured!

## 🎯 Summary

**Total Time**: ~2 minutes
- Start services: 30 seconds
- Create admin: 30 seconds
- Configure Radarr: 30 seconds
- Configure Sonarr: 30 seconds

**Result**: Fully functional Overseerr ready for Android app testing!
