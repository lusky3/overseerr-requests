
import http.server
import socketserver
import json
import re
from urllib.parse import urlparse, parse_qs

PORT = 5055

FAKE_MOVIES = [
    {
        "id": 1001, "mediaType": "movie", "title": "Neon Horizons", 
        "overview": "In a futuristic city where technology and humanity collide, a rogue AI gains consciousness and must choose between its programmed directives and its newfound sense of morality.",
        "posterPath": "/poster/movie_1001.jpg", "backdropPath": "/backdrop/movie_1001.jpg",
        "releaseDate": "2025-06-15", "voteAverage": 8.7, "mediaInfo": {"status": 5, "available": True}
    },
    {
        "id": 1002, "mediaType": "movie", "title": "The Last Cartographer",
        "overview": "An aging mapmaker discovers that the mysterious islands he's been charting for decades are actually gateways to parallel dimensions.",
        "posterPath": "/poster/movie_1002.jpg", "backdropPath": "/backdrop/movie_1002.jpg",
        "releaseDate": "2024-06-15", "voteAverage": 8.2, "mediaInfo": {"status": 5, "available": True}
    },
    {
        "id": 1003, "mediaType": "movie", "title": "Velvet Club",
        "overview": "A jazz pianist uncovers a citywide conspiracy after witnessing a murder at the legendary Velvet Club.",
        "posterPath": "/poster/movie_1003.jpg", "backdropPath": "/backdrop/movie_1001.jpg",
        "releaseDate": "2025-01-15", "voteAverage": 7.9, "mediaInfo": {"status": 2, "available": False}
    },
    {
        "id": 1004, "mediaType": "movie", "title": "Echoes of Tomorrow",
        "overview": "After a devastating solar storm erases all digital data on Earth, humanity must rebuild society from memory.",
        "posterPath": "/poster/movie_1004.jpg", "backdropPath": "/backdrop/movie_1002.jpg",
        "releaseDate": "2024-11-20", "voteAverage": 8.5, "mediaInfo": {"status": 5, "available": True}
    }
]

FAKE_USER = {
    "id": 1,
    "email": "admin@example.com",
    "displayName": "Admin User",
    "avatar": "/avatar/admin.jpg",
    "requestCount": 18,
    "permissions": 2
}

class MockHandler(http.server.SimpleHTTPRequestHandler):
    def do_GET(self):
        parsed = urlparse(self.path)
        path = parsed.path
        print(f"GET {path}")
        
        if path == "/api/v1/status":
            self.send_json({
                "version": "1.33.2",
                "initialized": True,
                "applicationUrl": "http://10.0.2.2:5055"
            })
        elif path == "/api/v1/auth/me" or path == "/api/v1/user":
            self.send_json(FAKE_USER)
        elif path == "/api/v1/user/1/quota" or path.endswith("/quota"):
            self.send_json({"movie": {"limit": 10, "remaining": 7, "days": 7}, "tv": {"limit": 10, "remaining": 7, "days": 7}})
        elif path.endswith("/stats"):
            self.send_json({"totalRequests": 32, "approvedRequests": 24, "declinedRequests": 2, "pendingRequests": 4, "availableRequests": 2})
        elif "/discover/" in path or "/search" in path:
            self.send_json({
                "page": 1, "totalPages": 1, "totalResults": len(FAKE_MOVIES),
                "results": FAKE_MOVIES
            })
        elif path.startswith("/api/v1/movie/"):
            self.send_json(FAKE_MOVIES[0])
        elif path.startswith("/api/v1/tv/"):
            # Return one show
            self.send_json({**FAKE_MOVIES[0], "name": "Fake Show", "mediaType": "tv"})
        elif path == "/api/v1/request":
            self.send_json({"pageInfo": {"pages": 1, "pageSize": 20, "results": 0, "page": 1}, "results": []})
        elif "genres" in path or "settings" in path:
            self.send_json([])
        elif path.startswith("/poster"):
            self.send_image("website/screenshots/issues.png")
        elif path.startswith("/backdrop"):
            self.send_image("website/screenshots/discover.png")
        elif path.startswith("/avatar"):
            self.send_image("website/screenshots/profile.png")
        else:
            self.send_error(404)

    def send_json(self, data):
        self.send_response(200)
        self.send_header('Content-type', 'application/json')
        self.send_header('Access-Control-Allow-Origin', '*')
        self.end_headers()
        self.wfile.write(json.dumps(data).encode('utf-8'))

    def send_image(self, filename):
        try:
            with open(filename, "rb") as f:
                content = f.read()
            self.send_response(200)
            self.send_header('Content-type', 'image/png')
            self.end_headers()
            self.wfile.write(content)
            print(f"Served image: {filename}")
        except:
            self.send_error(404)

    def do_POST(self):
        parsed = urlparse(self.path)
        path = parsed.path
        print(f"POST {path}")
        
        if path == "/api/v1/auth/plex":
            self.send_json({
                "apiKey": "test-key",
                "userId": 1,
                "user": FAKE_USER
            })
        else:
            self.send_json({"id": 1, "status": 1})

socketserver.TCPServer.allow_reuse_address = True
if __name__ == "__main__":
    with socketserver.TCPServer(("0.0.0.0", PORT), MockHandler) as httpd:
        print(f"Serving mock at port {PORT}")
        httpd.serve_forever()
