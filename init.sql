-- init.sql
CREATE DATABASE IF NOT EXISTS interview_db;
USE interview_db;

-- Crear usuario si no existe
CREATE USER IF NOT EXISTS 'user'@'%' IDENTIFIED BY 'pass';
GRANT ALL PRIVILEGES ON interview_db.* TO 'user'@'%';
FLUSH PRIVILEGES;