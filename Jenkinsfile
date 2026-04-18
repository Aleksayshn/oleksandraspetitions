pipeline {
    agent any

    environment {
        // Jenkins is installed on the EC2 host and runs these Docker commands locally.
        // Maven package stage produces this WAR and the Docker build copies it into Tomcat.
        WAR_FILE = 'target/oleksandraspetitions.war'

        // Keep deployment values explicit so they are easy to explain in the report.
        DOCKER_IMAGE_NAME = 'oleksandraspetitions-tomcat'
        CONTAINER_NAME = 'oleksandraspetitions-app'
        HOST_PORT = '9090'
        CONTAINER_PORT = '8080'
    }

    options {
        timestamps()
    }

    stages {
        stage('Checkout') {
            steps {
                // Pull the repository that contains the Jenkinsfile and application code.
                checkout scm
            }
        }

        stage('Build') {
            steps {
                // The Maven wrapper is used so Jenkins does not need a separate Maven install.
                sh 'chmod +x mvnw'
                sh './mvnw clean compile'
            }
        }

        stage('Test') {
            steps {
                // Run the automated tests before packaging or deployment.
                sh './mvnw test'
            }
        }

        stage('Package') {
            steps {
                // Build the WAR artifact that will be placed into the Tomcat image.
                sh './mvnw package -DskipTests'
            }
        }

        stage('Archive WAR') {
            steps {
                // Archive the WAR so Jenkins keeps the deployment artifact for this build.
                archiveArtifacts artifacts: "${WAR_FILE}", fingerprint: true
            }
        }

        stage('Manual Approval') {
            steps {
                // Deployment only continues after a manual confirmation.
                input message: 'Deploy the WAR application to the Tomcat Docker container on EC2?', ok: 'Deploy'
            }
        }

        stage('Build Docker Image') {
            steps {
                // Build a fresh Tomcat image that already contains the generated WAR file.
                sh 'docker build -t ${DOCKER_IMAGE_NAME}:latest .'
            }
        }

        stage('Remove Old Container') {
            steps {
                // Remove the existing container if it is present. This keeps redeployment idempotent.
                sh 'docker rm -f ${CONTAINER_NAME} || true'
            }
        }

        stage('Run New Tomcat Container') {
            steps {
                // Start the new container and publish the application on host port 9090.
                sh '''
                    docker run -d \
                      --name ${CONTAINER_NAME} \
                      --restart unless-stopped \
                      -p ${HOST_PORT}:${CONTAINER_PORT} \
                      ${DOCKER_IMAGE_NAME}:latest
                '''

                // Show the running container so the deployment result is visible in Jenkins logs.
                sh 'docker ps --filter "name=${CONTAINER_NAME}"'
            }
        }
    }
}
