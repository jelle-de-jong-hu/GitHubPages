-- Main DB/user is created via docker-compose
-- Create test database and user for local/CI tests
CREATE USER "hu-s4-lingo-dbadmin-test" WITH CREATEDB PASSWORD 'hu-s4-lingo-pwd';
CREATE DATABASE "hu-s4-lingo-test" OWNER "hu-s4-lingo-dbadmin-test";