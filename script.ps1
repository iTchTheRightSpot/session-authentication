Write-Host "Pulling the latest mysql image..."
docker pull mysql/mysql-server:latest
docker tag mysql/mysql-server:latest mysql:latest
docker rmi mysql/mysql-server:latest

# Build the Spring Boot application image
Write-Host "Building Spring Boot application image..."
./mvnw spring-boot:build-image

# Run the Docker Compose file
Write-Host "Starting containers with Docker Compose..."
docker compose up -d