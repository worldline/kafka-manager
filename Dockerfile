# Back compilation
FROM maven:3.8-openjdk-11 AS maven-builder
WORKDIR /backend
COPY backend .
RUN mvn package -DskipTests

# Front compilation
FROM node:16-alpine AS angular-builder
WORKDIR /frontend
COPY frontend .
RUN yarn && yarn build --prod

# KafkManager container
FROM openjdk:11-jre-slim-buster AS production-stage

WORKDIR /app

RUN apt update && \
    apt install -y nginx && \
    mkdir /app/front /app/back && \
    chmod -R 777 /var/lib/nginx /var/log/nginx /run /app/front /app/back

# Nginx Configuration Installation
COPY conf/nginx.*.conf /etc/nginx/

# Scripts
COPY scripts/docker-entrypoint.sh /

RUN chmod +x /docker-entrypoint.sh

# KafkaManager Front
COPY --from=angular-builder /frontend/dist/kafkamanager-front/ /app/front/

# KafkaManager Back
COPY --from=maven-builder /backend/target/*.jar /app/back/app.jar

# Mount Volumes
VOLUME ["/tmp", "/run", "/srv/log"]

EXPOSE 8001

ENTRYPOINT ["/docker-entrypoint.sh"]