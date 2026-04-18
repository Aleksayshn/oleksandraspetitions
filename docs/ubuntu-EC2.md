# Ubuntu EC2 Host Setup

This project assumes:

- Jenkins is installed directly on the Ubuntu EC2 VM
- Docker is installed on the same VM
- Jenkins runs shell commands on the host to build and redeploy the Tomcat container

## 1. Install Java 21

```bash
sudo apt update
sudo apt install -y openjdk-21-jdk
java -version
```

## 2. Install Jenkins on the host

```bash
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo tee \
  /usr/share/keyrings/jenkins-keyring.asc > /dev/null
echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/" | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt update
sudo apt install -y jenkins
sudo systemctl enable jenkins
sudo systemctl start jenkins
sudo systemctl status jenkins
```

Jenkins default web UI:

```text
http://<EC2_PUBLIC_IP>:8080
```

## 3. Install Docker on the host

```bash
sudo apt install -y docker.io
sudo systemctl enable docker
sudo systemctl start docker
docker --version
```

## 4. Allow the Jenkins user to run Docker

```bash
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

Optional check:

```bash
sudo -u jenkins docker ps
```

## 5. Open the required EC2 security group ports

- `22` for SSH
- `8080` for Jenkins
- `9090` for the deployed petition application

## 6. Result

After this setup:

- Jenkins builds the WAR on the EC2 host
- Jenkins builds the Tomcat Docker image on the EC2 host
- Jenkins removes the old app container if it exists
- Jenkins runs the new container on `9090:8080`

The application should then be reachable at:

```text
http://<EC2_PUBLIC_IP>:9090/oleksandraspetitions/
```
