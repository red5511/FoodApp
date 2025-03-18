#!/bin/sh
if [ -f /run/secrets/postgres-db-password ]; then
  # Usuń CR, LF i ewentualny BOM
  export DB_PASSWORD=$(cat /run/secrets/postgres-db-password | sed 's/^\xEF\xBB\xBF//' | tr -d '\r\n' | xargs)
fi
if [ -f /run/secrets/mail-app-password ]; then
  # Usuń CR, LF i ewentualny BOM
  export MAIL_APP_PASSWORD=$(cat /run/secrets/mail-app-password | sed 's/^\xEF\xBB\xBF//' | tr -d '\r\n' | xargs)
fi

if [ -f /run/secrets/rabbitmq-password ]; then
  # Usuń CR, LF i ewentualny BOM
  export RABBITMQ_PASSWORD=$(cat /run/secrets/rabbitmq-password | sed 's/^\xEF\xBB\xBF//' | tr -d '\r\n' | xargs)
fi

if [ -f /run/secrets/admin-login ]; then
  # Usuń CR, LF i ewentualny BOM
  export ADMIN_LOGIN=$(cat /run/secrets/admin-login | sed 's/^\xEF\xBB\xBF//' | tr -d '\r\n' | xargs)
fi

if [ -f /run/secrets/admin-password ]; then
  # Usuń CR, LF i ewentualny BOM
  export ADMIN_PASSWORD=$(cat /run/secrets/admin-password | sed 's/^\xEF\xBB\xBF//' | tr -d '\r\n' | xargs)
fi

if [ -f /run/secrets/pgadmin-email ]; then
  # Usuń CR, LF i ewentualny BOM
  export PG_ADMIN_EMAIL=$(cat /run/secrets/pgadmin-email | sed 's/^\xEF\xBB\xBF//' | tr -d '\r\n' | xargs)
fi

if [ -f /run/secrets/pgadmin-password ]; then
  # Usuń CR, LF i ewentualny BOM
  export PG_ADMIN_PASSWORD=$(cat /run/secrets/pgadmin-password | sed 's/^\xEF\xBB\xBF//' | tr -d '\r\n' | xargs)
fi


exec java \
  -Dspring.datasource.password="${DB_PASSWORD}" \
  -Dspring.mail.password="${MAIL_APP_PASSWORD}" \
  -Dspring.rabbitmq.password="${RABBITMQ_PASSWORD}" \
  -Dapp.admin-login="${ADMIN_LOGIN}" \
  -Dapp.admin-password="${ADMIN_PASSWORD}" \
  -jar /app/foodapp-${JAR_VERSION}.jar