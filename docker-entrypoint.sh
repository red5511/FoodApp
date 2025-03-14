#!/bin/sh
if [ -f /run/secrets/postgres-db-password ]; then
    # Usuń CR, LF i ewentualny BOM
    export DB_PASSWORD=$(cat /run/secrets/postgres-db-password | sed 's/^\xEF\xBB\xBF//' | tr -d '\r\n' | xargs)
fi

exec java -Dspring.datasource.url="${DB_URL}" -Dspring.datasource.password="${DB_PASSWORD}" -jar /app/foodapp-${JAR_VERSION}.jar
