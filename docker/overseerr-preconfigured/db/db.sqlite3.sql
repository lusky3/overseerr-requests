-- SQLite database initialization for Overseerr
-- This creates a pre-configured admin user

-- Create admin user (password: admin123)
-- Password hash is bcrypt for "admin123"
INSERT OR IGNORE INTO user (id, email, username, plexId, plexToken, permissions, avatar, createdAt, updatedAt, userType, plexUsername, resetPasswordGuid, recoveryLinkExpirationDate, requestCount)
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
  NULL,
  NULL,
  0
);

-- Set password for admin user
-- This is the bcrypt hash for "admin123"
INSERT OR IGNORE INTO user_password (userId, password)
VALUES (1, '$2b$10$rKxMhKPZQxGkrOrCheKzQOQnhYplo6LKo8M0hUPEgsPYRkMkjHnKa');
