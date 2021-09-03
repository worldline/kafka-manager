#!/bin/bash -x
set -eo pipefail

# Start Nginx with kafkamanager config
nginx $@ -c /etc/nginx/nginx.kafkamanager.conf -g "daemon off;" &

# Start Spring boot app with a Tomcat embedded
java $JAVA_OPTS -jar /app/back/app.jar $JAVA_ARGS