#!/bin/bash

echo "Starting the application"
cd /home/ec2-user/server
sudo java -jar *.jar > /dev/null 2> /dev/null < /dev/null &