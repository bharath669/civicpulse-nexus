-- Run once as a MySQL administrator.
CREATE DATABASE IF NOT EXISTS civic_citizen_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS civic_grievance_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'civic_user'@'localhost' IDENTIFIED BY 'civic_pass';
GRANT ALL PRIVILEGES ON civic_citizen_db.* TO 'civic_user'@'localhost';
GRANT ALL PRIVILEGES ON civic_grievance_db.* TO 'civic_user'@'localhost';
FLUSH PRIVILEGES;

-- Hibernate creates/updates tables from the entity classes during local development.
