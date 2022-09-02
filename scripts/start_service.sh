#!/usr/bin/env bash
echo "Starting the application"
cd /home/ubuntu/server
database_password="$database_password"
database_user="$database_user"
emailSender_address="$emailSender_address"
emailSender_password="$emailSender_password"
token_secret="$token_secret"
finnhub_token="$finnhub_token"
client_domain="$client_domain"
sudo java -jar -Dserver.port=80 bullshorn-0.0.1-SNAPSHOT.jar > /dev/null 2> /dev/null < /dev/null &