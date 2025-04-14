#!/bin/bash

# Cấu hình
DOCKER_USERNAME="gnuhhung317"
IMAGE_NAME="ielts-platform"
VERSION="latest"

# Pull image mới nhất
echo "Pulling latest image..."
docker pull $DOCKER_USERNAME/$IMAGE_NAME:$VERSION

# Dừng và xóa container cũ nếu có
echo "Stopping and removing old containers..."
docker-compose down

# Chạy container mới
echo "Starting new containers..."
docker-compose up -d

echo "Containers are running!"
echo "You can check logs using: docker-compose logs -f" 