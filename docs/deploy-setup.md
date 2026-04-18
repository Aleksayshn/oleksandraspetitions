# Docker Deployment Setup

This project is deployed by Jenkins on the same Ubuntu EC2 VM using Docker.

Jenkins is installed directly on the EC2 host, not in a container.
For the base Ubuntu host setup, see:

- `docs/ubuntu-EC2.md`

## 1. Prepare the EC2 VM

Install:

- Java 21
- Jenkins
- Docker
- Git

Open these security group ports:

- `22` for SSH administration
- `8080` for Jenkins if you want web access
- `9090` for the deployed application

## 2. Give Jenkins permission to use Docker

On the EC2 VM:

```bash
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
sudo systemctl restart docker
```

After this, the Jenkins service should be able to run `docker build`, `docker rm`, and `docker run`.

## 3. What the Docker deployment does

The Jenkins pipeline:

1. builds `target/oleksandraspetitions.war`
2. builds a Docker image based on Tomcat
3. copies the WAR into Tomcat inside the image
4. removes the old container if it exists
5. starts a new container with port mapping:

```text
9090:8080
```

The app should then be available at:

```text
http://<EC2_PUBLIC_IP>:9090/oleksandraspetitions/
```

## 4. Jenkinsfile values to review

The Jenkinsfile has simple deployment variables:

- `DOCKER_IMAGE_NAME`
- `CONTAINER_NAME`
- `HOST_PORT`
- `CONTAINER_PORT`

The default mapping is:

- host port `9090`
- container port `8080`

## 5. Manual test commands on the EC2 VM

Build the WAR:

```bash
./mvnw clean package
```

Build the Docker image:

```bash
docker build -t oleksandraspetitions-tomcat:latest .
```

Remove the old container if it exists:

```bash
docker rm -f oleksandraspetitions-app || true
```

Run the new container:

```bash
docker run -d \
  --name oleksandraspetitions-app \
  --restart unless-stopped \
  -p 9090:8080 \
  oleksandraspetitions-tomcat:latest
```

Check the running container:

```bash
docker ps
```

## 6. Keep it simple

This deployment approach is enough for the assignment:

- Jenkins handles CI and deployment
- Tomcat runs inside Docker
- the WAR stays unchanged
- redeployment is just rebuild + replace container
