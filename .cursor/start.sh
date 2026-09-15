#!/usr/bin/env bash
# Per-boot runtime initialization for the Cloud Agent environment.
# Brings up the local PostgreSQL cluster and ensures the application role,
# database and tables exist. Idempotent and safe to run on every boot.
set -euo pipefail

DB_NAME="${SPRING_DATASOURCE_DB:-affiliatebot}"
DB_USER="${SPRING_DATASOURCE_USERNAME:-botuser}"
DB_PASS="${SPRING_DATASOURCE_PASSWORD:-botpass}"

echo "==> Starting PostgreSQL cluster"
PG_VER="$(pg_lsclusters -h 2>/dev/null | awk 'NR==1{print $1}')"
PG_VER="${PG_VER:-16}"
if ! pg_lsclusters -h 2>/dev/null | grep -q online; then
  sudo pg_ctlcluster "$PG_VER" main start || true
fi

# Wait until PostgreSQL accepts connections.
for _ in $(seq 1 30); do
  if pg_isready -q; then break; fi
  sleep 1
done
pg_isready

echo "==> Ensuring role and database exist"
sudo -u postgres psql -tAc "SELECT 1 FROM pg_roles WHERE rolname='${DB_USER}'" | grep -q 1 \
  || sudo -u postgres psql -c "CREATE ROLE ${DB_USER} LOGIN PASSWORD '${DB_PASS}';"
sudo -u postgres psql -tAc "SELECT 1 FROM pg_database WHERE datname='${DB_NAME}'" | grep -q 1 \
  || sudo -u postgres psql -c "CREATE DATABASE ${DB_NAME} OWNER ${DB_USER};"

echo "==> Ensuring application tables exist"
PGPASSWORD="${DB_PASS}" psql -h localhost -U "${DB_USER}" -d "${DB_NAME}" <<'SQL'
CREATE TABLE IF NOT EXISTS rakuten_api_parameters (
    id BIGSERIAL PRIMARY KEY,
    age INTEGER,
    sex SMALLINT
);
CREATE TABLE IF NOT EXISTS openai_api_parameters (
    id BIGSERIAL PRIMARY KEY,
    prompt TEXT,
    model TEXT
);
CREATE TABLE IF NOT EXISTS past_affiliate_urls (
    affiliate_url TEXT PRIMARY KEY
);
-- Seed one row per parameter table so the UI and batch always have a "latest" record.
INSERT INTO rakuten_api_parameters (age, sex)
    SELECT 30, 0 WHERE NOT EXISTS (SELECT 1 FROM rakuten_api_parameters);
INSERT INTO openai_api_parameters (prompt, model)
    SELECT 'あなたは商品を紹介する日本語のツイートを作成するアシスタントです。', 'gpt-4o-mini'
    WHERE NOT EXISTS (SELECT 1 FROM openai_api_parameters);
SQL

echo "==> start.sh complete (PostgreSQL ready on localhost:5432, db=${DB_NAME})"
