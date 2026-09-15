#!/usr/bin/env bash
# Idempotent repository bootstrap for the Cloud Agent environment.
# - Ensures Java 21 + PostgreSQL client/server are available (installed once, then
#   captured in the environment snapshot/build).
# - Generates a local application.properties (git-ignored) that reads secrets from
#   environment variables with safe dev defaults.
# - Warms the Gradle dependency cache and compiles the project.
set -euo pipefail

cd "$(dirname "$0")/.."

echo "==> Ensuring system packages (java 21, postgresql)"
NEED_INSTALL=0
command -v java >/dev/null 2>&1 || NEED_INSTALL=1
command -v pg_ctlcluster >/dev/null 2>&1 || NEED_INSTALL=1
if [ "$NEED_INSTALL" -eq 1 ]; then
  sudo apt-get update -qq
  sudo DEBIAN_FRONTEND=noninteractive apt-get install -y -qq \
    openjdk-21-jdk-headless postgresql postgresql-contrib
fi
java -version

echo "==> Generating src/main/resources/application.properties (if missing)"
PROPS=src/main/resources/application.properties
if [ ! -f "$PROPS" ]; then
  cat > "$PROPS" <<'PROPERTIES'
spring.application.name=automated-affiliate-bot

# --- Datasource (local PostgreSQL provisioned by the Cloud Agent environment) ---
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/affiliatebot}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:botuser}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:botpass}
spring.datasource.driver-class-name=org.postgresql.Driver

# --- Spring Batch metadata tables are created automatically ---
spring.batch.jdbc.initialize-schema=always

# --- Mail (error notifications). Point MAIL_HOST/PORT at a real SMTP server in prod ---
email.recipient=${EMAIL_RECIPIENT:dev@example.com}
spring.mail.host=${MAIL_HOST:localhost}
spring.mail.port=${MAIL_PORT:1025}

# --- Rakuten API ---
rakuten.api.applicationId=${RAKUTEN_API_APPLICATIONID:dummy-application-id}
rakuten.api.affiliateId=${RAKUTEN_API_AFFILIATEID:dummy-affiliate-id}
rakuten.api.apiUrl=${RAKUTEN_API_APIURL:https://app.rakuten.co.jp/services/api/IchibaItem/Ranking/20220601}

# --- OpenAI API ---
openai.api.key=${OPENAI_API_KEY:dummy-openai-key}
openai.api.url=${OPENAI_API_URL:https://api.openai.com/v1/chat/completions}

# --- Twitter (X) API ---
twitter.api.key=${TWITTER_API_KEY:dummy-twitter-key}
twitter.api.secret.key=${TWITTER_API_SECRET_KEY:dummy-twitter-secret}
twitter.access.token=${TWITTER_ACCESS_TOKEN:dummy-access-token}
twitter.access.token.secret=${TWITTER_ACCESS_TOKEN_SECRET:dummy-access-token-secret}
PROPERTIES
  echo "    created $PROPS"
else
  echo "    $PROPS already exists, leaving it untouched"
fi

echo "==> Warming Gradle cache and compiling"
./gradlew --no-daemon classes testClasses

echo "==> install.sh complete"
