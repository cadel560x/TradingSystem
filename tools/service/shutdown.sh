#!/bin/sh

# Set default values if variables don't exists
SHUT_USER="${SHUT_USER:-jefe}"
SHUT_PASSWORD="${SHUT_PASSWORD:-Molagavita1!}"
SHUT_SERVER="${SHUT_SERVER:-localhost}"
SHUT_PORT="${SHUT_PORT:-8080}"
SHUT_PATH="${SHUT_PATH:-actuator/shutdown}"
SHUT_URL=$SHUT_SERVER:$SHUT_PORT/$SHUT_PATH

curl -s -XPOST http://$SHUT_USER:$SHUT_PASSWORD@$SHUT_URL > /dev/null 2>&1
