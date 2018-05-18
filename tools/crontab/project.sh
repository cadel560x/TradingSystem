#!/bin/sh

COMMON_PATH="/home/ubuntu"
TOOLS_PATH=$COMMON_PATH/tools

LD_PATH="${LD_PATH:-$COMMON_PATH/jmeter}"
LD_EXE="${LD_EXE:-jmeterld.sh}"

SERVICE_PATH="${SERVICE_PATH:-$TOOLS_PATH/service}"
SERVICE_START="${SERVICE_START:-start.sh}"
SERVICE_STOP="${SERVICE_STOP:-shutdown.sh}"

DB_PATH="${DB_PATH:-$TOOLS_PATH/db}"
DB_SCRIPT="${DB_SCRIPT:-DBscript.sh}"

APP_PATH="${APP_PATH:-$COMMON_PATH/project/run}"

# Redirect stdout & stderr to /dev/null. No log files in crontab, please
exec > /dev/null
exec 2>&1

# First, shutdown
# Stop load driver
$LD_PATH/$LD_EXE stop
rm -f $LD_PATH/logs/*
sleep 2
# Stop application
$SERVICE_PATH/$SERVICE_STOP
rm -f $APP_PATH/*.log
rm -f $APP_PATH/*.err
# Truncate the data base
sleep 2
$DB_PATH/$DB_SCRIPT

# Second, start up
# Start application
$SERVICE_PATH/$SERVICE_START
sleep 10
# Start load driver
$LD_PATH/$LD_EXE start
