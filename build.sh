#!/bin/bash -e
echo "Setting environment variables..."
PROJECT_HOME=$(pwd)
export SERVICE_NAME="cp_payment_service"
export SERVICE_PORT="9005"
export DOCKER_CONTAINER_NAME="cp_payment_service"
BUILD_ROOT_DIR="/tmp/cp_services"
SERVICE_BUILD_DIR="$BUILD_ROOT_DIR/$SERVICE_NAME"
BUILD_ID=$(date '+%Y%m%d%H%M%S')
export FINAL_IMAGE_NAME="v1v3k6/$DOCKER_CONTAINER_NAME:$BUILD_ID"
. /home/vivek/docker-env.sh

echo "Checking if build directory is available, if not it will be created..."
if [ ! -d "$SERVICE_BUILD_DIR" ]; then
  mkdir -pv "$SERVICE_BUILD_DIR"
fi

echo "Copying updated files from source..."
cp -a "$PROJECT_HOME/src" "$SERVICE_BUILD_DIR/src"
cp -a "$PROJECT_HOME/pom.xml" "$SERVICE_BUILD_DIR/pom.xml"
cp -a "$PROJECT_HOME/Dockerfile" "$SERVICE_BUILD_DIR/Dockerfile"

echo "Entered $SERVICE_BUILD_DIR..."
cd "$SERVICE_BUILD_DIR"
mvn clean package -DskipTests=true
echo "Building docker image $FINAL_IMAGE_NAME..."
docker build -t "$FINAL_IMAGE_NAME" .
echo "Replacing tokens for compose..."
envsubst < "$PROJECT_HOME/docker-compose.yml" > "$SERVICE_BUILD_DIR/docker-compose.yml"

echo "Starting container..."
docker compose up -d

echo "Cleaning up old build files..."
rm -rf "$SERVICE_BUILD_DIR"

echo "Deployment completed, please check for errors in console and fix if required!"
echo "Leaving $SERVICE_BUILD_DIR and going back to $PROJECT_HOME..."
cd "$PROJECT_HOME"

echo "Cleaning unused images to save space!"
echo "y" | docker image prune -a

echo "Unsetting variables to avoid conflict..."
unset SERVICE_NAME
unset SERVICE_PORT
unset DOCKER_CONTAINER_NAME
unset FINAL_IMAGE_NAME