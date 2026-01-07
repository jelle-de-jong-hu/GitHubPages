#!/usr/bin/env bash
set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"

PGPASSWORD="hu-s4-lingo-pwd" psql \
  --username="hu-s4-lingo-dbadmin" \
  --dbname="hu-s4-lingo" \
  -f "$SCRIPT_DIR/lingo_words.sql"
