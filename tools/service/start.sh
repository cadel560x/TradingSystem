#!/bin/sh

# Set default values if variables don't exists
APP_PATH="${APP_PATH:-/home/ubuntu/project/run}"
APP="${APP:-TradingSystem-0.0.10-SNAPSHOT.jar}"

if [ ! -z $1 ]; then
    APP=$1
fi

cd $APP_PATH
java -jar $APP > app.log 2> app.err &
