# Plex Mock API Reference

This mock Plex Media Server implements the official Plex API endpoints based on the [Plex API documentation](https://developer.plex.tv/pms).

## Base URL

```
http://localhost:32400
```

## Authentication

The mock accepts `X-Plex-Token` as either:

- Query parameter: `?X-Plex-Token=your-token`
- HTTP header: `X-Plex-Token: your-token`

**Note:** The mock doesn't validate tokens - any value is accepted for testing purposes.

## Implemented Endpoints

### Core Server Endpoints

#### Get Server Status

```bash
GET /
```

Returns server capabilities and configuration in XML format.

#### Get Server Identity

```bash
GET /identity
```

Returns server identification information.

**Response:**

```json
{
  "size": 1,
  "claimed": true,
  "machineIdentifier": "mock-plex-server-12345",
  "version": "1.32.0.6865-1234567890"
}
```

#### List Servers

```bash
GET /servers
```

Returns list of available Plex servers.

#### Get Server Preferences

```bash
GET /:/prefs
```

Returns server preferences and settings.

### User & Account Endpoints

#### Get User Account Info

```bash
GET /myplex/account
```

Returns authenticated user account information.

### Library Endpoints

#### List All Library Sections

```bash
GET /library/sections
```

Returns all library sections (Movies, TV Shows, etc.).

**Response:**

```json
{
  "MediaContainer": {
    "size": 2,
    "Directory": [
      {
        "key": "1",
        "type": "movie",
        "title": "Movies",
        "agent": "tv.plex.agents.movie",
        "scanner": "Plex Movie"
      },
      {
        "key": "2",
        "type": "show",
        "title": "TV Shows",
        "agent": "tv.plex.agents.series",
        "scanner": "Plex TV Series"
      }
    ]
  }
}
```

#### Get Library Section Contents

```bash
GET /library/sections/{sectionId}/all
```

Returns all media items in a specific library section.

**Example:**

```bash
curl http://localhost:32400/library/sections/1/all
```

**Response includes:**

- The Matrix (1999) - Rating: 8.7
- Inception (2010) - Rating: 8.8
- Interstellar (2014) - Rating: 8.6

#### Refresh Library Section

```bash
GET /library/sections/{sectionId}/refresh
GET /library/sections/all/refresh
```

Triggers a library scan.

#### Analyze Library Section

```bash
GET /library/sections/{sectionId}/analyze
```

Analyzes media files for intro detection, loudness, etc.

#### Empty Library Trash

```bash
PUT /library/sections/{sectionId}/emptyTrash
```

Empties the trash for a library section.

#### Get Collections in Library

```bash
GET /library/sections/{sectionId}/collections
```

Returns collections within a library section.

#### Search Within Library

```bash
GET /library/sections/{sectionId}/search?query={searchTerm}
```

Searches for media within a specific library section.

### Metadata Endpoints

#### Get Metadata Item

```bash
GET /library/metadata/{metadataId}
```

Returns detailed information about a specific media item.

**Example:**

```bash
curl http://localhost:32400/library/metadata/1001
```

#### Update Metadata

```bash
PUT /library/metadata/{metadataId}
```

Updates metadata fields for a media item.

**Example:**

```bash
curl -X PUT "http://localhost:32400/library/metadata/1001?title.value=New%20Title"
```

#### Get Thumbnail

```bash
GET /library/metadata/{metadataId}/thumb
```

Returns the thumbnail image for a media item.

#### Get Art

```bash
GET /library/metadata/{metadataId}/art
```

Returns the background art for a media item.

#### Upload Poster

```bash
POST /library/metadata/{metadataId}/posters
```

Uploads a custom poster image.

### Search Endpoints

#### Global Search

```bash
GET /search?query={searchTerm}
```

Searches across all libraries.

**Example:**

```bash
curl "http://localhost:32400/search?query=Matrix"
```

**Response:**

```json
{
  "MediaContainer": {
    "size": 2,
    "Metadata": [
      {
        "ratingKey": "1001",
        "type": "movie",
        "title": "The Matrix",
        "year": 1999,
        "librarySectionID": 1,
        "librarySectionTitle": "Movies"
      },
      {
        "ratingKey": "2001",
        "type": "show",
        "title": "The Matrix Animated Series",
        "year": 2003,
        "librarySectionID": 2,
        "librarySectionTitle": "TV Shows"
      }
    ]
  }
}
```

### Playback & Session Endpoints

#### Get Active Sessions

```bash
GET /status/sessions
```

Returns currently active playback sessions.

#### Get Watch History

```bash
GET /status/sessions/history/all
```

Returns watch history for all users.

#### Get Transcode Sessions

```bash
GET /transcode/sessions
```

Returns active transcoding sessions.

#### Delete Transcode Session

```bash
DELETE /transcode/sessions/{sessionKey}
```

Terminates a specific transcode session.

### Client Endpoints

#### List Available Clients

```bash
GET /clients
```

Returns list of available Plex clients.

**Response:**

```json
{
  "MediaContainer": {
    "size": 1,
    "Server": [
      {
        "name": "Mock Client",
        "host": "192.168.1.150",
        "machineIdentifier": "mock-client-123",
        "product": "Plex for TV",
        "deviceClass": "tv"
      }
    ]
  }
}
```

### Collection Endpoints

#### Create Collection

```bash
POST /library/collections
```

Creates a new collection.

**Parameters:**

- `type`: Collection type (1 for movies, 2 for shows)
- `title`: Collection title
- `sectionId`: Library section ID
- `uri`: Comma-separated media URIs

#### Add Items to Collection

```bash
PUT /library/collections/{collectionId}/items
```

Adds items to an existing collection.

### Playlist Endpoints

#### Get All Playlists

```bash
GET /playlists
```

Returns all playlists.

#### Create Playlist

```bash
POST /playlists
```

Creates a new playlist.

**Parameters:**

- `type`: Playlist type (video, audio, photo)
- `title`: Playlist title
- `smart`: Smart playlist flag (0 or 1)
- `uri`: Comma-separated media URIs

#### Add Items to Playlist

```bash
PUT /playlists/{playlistId}/items
```

Adds items to a playlist.

#### Remove Items from Playlist

```bash
DELETE /playlists/{playlistId}/items/{itemId}
```

Removes an item from a playlist.

#### Delete Playlist

```bash
DELETE /playlists/{playlistId}
```

Deletes a playlist.

### Watched Status Endpoints

#### Mark as Watched (Scrobble)

```bash
GET /:/scrobble?key={mediaKey}&identifier=com.plexapp.plugins.library
```

Marks a media item as watched.

**Example:**

```bash
curl "http://localhost:32400/:/scrobble?key=1001&identifier=com.plexapp.plugins.library"
```

#### Mark as Unwatched (Unscrobble)

```bash
GET /:/unscrobble?key={mediaKey}&identifier=com.plexapp.plugins.library
```

Marks a media item as unwatched.

### Media Streaming Endpoints

#### Download/Stream Media Part

```bash
GET /library/parts/{partId}/{fileId}/file.{ext}
```

Returns direct media file stream or download.

**Parameters:**

- `download`: Set to 1 to force download

#### Transcode Photo

```bash
GET /photo/:/transcode
```

Returns transcoded photo/thumbnail.

**Parameters:**

- `url`: Source image URL
- `width`: Desired width
- `height`: Desired height

#### Start Video Transcode

```bash
GET /video/:/transcode/universal/start.m3u8
```

Starts video transcoding and returns HLS manifest.

### Playback Control Endpoints

#### Play Media

```bash
GET /player/playback/playMedia
```

Starts media playback on a client.

#### Pause Playback

```bash
GET /player/playback/pause
```

Pauses current playback.

#### Resume Playback

```bash
GET /player/playback/play
```

Resumes paused playback.

#### Stop Playback

```bash
GET /player/playback/stop
```

Stops current playback.

#### Seek to Position

```bash
GET /player/playback/seekTo?offset={milliseconds}
```

Seeks to a specific position in the media.

### Timeline & Progress Endpoints

#### Update Timeline

```bash
GET /:/timeline
```

Updates playback timeline.

#### Update Progress

```bash
GET /:/progress
```

Updates playback progress.

### Activities & Background Tasks

#### Get Activities

```bash
GET /activities
```

Returns current background activities (scans, analysis, etc.).

## Testing Examples

### Test Server Connection

```bash
curl http://localhost:32400/identity | jq .
```

### List All Movies

```bash
curl http://localhost:32400/library/sections/1/all | jq '.MediaContainer.Metadata[] | {title, year, rating}'
```

### Search for Content

```bash
curl "http://localhost:32400/search?query=Matrix" | jq .
```

### Get Specific Movie Details

```bash
curl http://localhost:32400/library/metadata/1001 | jq .
```

### Mark Movie as Watched

```bash
curl "http://localhost:32400/:/scrobble?key=1001&identifier=com.plexapp.plugins.library"
```

### Create a Playlist

```bash
curl -X POST "http://localhost:32400/playlists?type=video&title=My%20Playlist"
```

## Mock Data

The mock includes the following sample data:

### Movies (Library Section 1)

- **The Matrix** (1999) - Rating: 8.7, Action/Sci-Fi
- **Inception** (2010) - Rating: 8.8, Sci-Fi/Thriller
- **Interstellar** (2014) - Rating: 8.6, Sci-Fi/Drama

### TV Shows (Library Section 2)

- Mock TV show data available via search

### Collections

- **The Matrix Collection** - 3 items

## CORS Support

The mock includes full CORS support with:

- `Access-Control-Allow-Origin: *`
- `Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS`
- `Access-Control-Allow-Headers: X-Plex-Token, X-Plex-Client-Identifier, etc.`

## Notes

1. **No Authentication Required**: The mock accepts any token value for testing
2. **Static Data**: All responses return pre-defined mock data
3. **Write Operations**: POST/PUT/DELETE operations return success but don't persist changes
4. **XML Support**: Some endpoints return XML (server status, account info) to match real Plex API
5. **JSON Support**: Most endpoints return JSON for easier testing

## Differences from Real Plex API

- No actual media files or streaming
- No persistent state (changes don't persist)
- Simplified responses (fewer fields)
- No authentication validation
- No rate limiting
- All operations succeed immediately

## Integration with Overseerr

This mock is designed to work with Overseerr for testing. Overseerr can:

- Authenticate against the mock server
- Discover libraries
- Search for content
- Manage requests

Configure Overseerr to use:

- **Plex Server URL**: `http://plex-mock:32400` (from Docker network)
- **Plex Server URL**: `http://localhost:32400` (from host machine)
- **Plex Token**: Any value (e.g., `mock-token-12345`)
