#!/usr/bin/env bash
set -e

# Run SQL as the newly created db user
PGPASSWORD="hu-s4-lingo-pwd" psql \
  --username="hu-s4-lingo-dbadmin" \
  --dbname="hu-s4-lingo" \
  -f /docker-entrypoint-initdb.d/lingo_words.sql
