
# Cấu hình
DOCKER_USERNAME="gnuhhung317"
IMAGE_NAME="ielts-platform"
VERSION="latest"

# Build image
echo "Building Docker image..."
docker build -t $DOCKER_USERNAME/$IMAGE_NAME:$VERSION .

# Login to DockerHub (yêu cầu nhập password)
echo "Logging in to DockerHub..."
docker login

# Push image
echo "Pushing image to DockerHub..."
docker push $DOCKER_USERNAME/$IMAGE_NAME:$VERSION

echo "Image has been pushed successfully!" 