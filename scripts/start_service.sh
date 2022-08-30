#!/usr/bin/env bash
echo "Starting the application"
cd /home/ubuntu/server
sudo java -jar -Dserver.port=80 bullshorn-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &