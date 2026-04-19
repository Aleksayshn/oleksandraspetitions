pipeline {
    agent any
    environment {
        WAR_FILE = 'target/oleksandraspetitions.war'
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
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x mvnw'
                sh './mvnw clean compile'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Package') {
            steps {
                sh './mvnw package -DskipTests'
            }
        }

        stage('Archive WAR') {
            steps {
                archiveArtifacts artifacts: "${WAR_FILE}", fingerprint: true
            }
        }

        stage('Manual Approval') {
            steps {
                input message: 'Deploy the WAR application to the Tomcat Docker container on EC2?', ok: 'Deploy'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE_NAME}:latest .'
            }
        }

        stage('Remove Old Container') {
            steps {
                sh 'docker rm -f ${CONTAINER_NAME} || true'
            }
        }

        stage('Run New Tomcat Container') {
            steps {
                sh '''
                    docker run -d \
                      --name ${CONTAINER_NAME} \
                      --restart unless-stopped \
                      -p ${HOST_PORT}:${CONTAINER_PORT} \
                      ${DOCKER_IMAGE_NAME}:latest
                '''

                sh 'docker ps --filter "name=${CONTAINER_NAME}"'
            }
        }
    }
}
