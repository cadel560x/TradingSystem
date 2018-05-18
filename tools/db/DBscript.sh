#!/bin/bash

# Set default values if variables don't exists
DB_USER="${DB_USER:-root}"
DB_PASSWORD="${DB_PASSWORD:-Molagavita1!}"
DB_SERVER="${DB_SERVER:-dbserver.cznmhqwngipk.us-east-2.rds.amazonaws.com}"
DATABASE="${DATABASE:-trading_system}"
SQL_DIR=/home/ubuntu/tools/db/sql
SQL_FILE="${SQL_FILE:-$SQL_DIR/truncate.sql}"

if [ ! -z $1 ]; then
    SQL_FILE=$1
fi


mysql -Bq -u $DB_USER -p$DB_PASSWORD -h $DB_SERVER $DATABASE < $SQL_FILE
