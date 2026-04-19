pipeline {
    agent any
    environment {
        WAR_FILE = 'target/oleksandraspetitions.war'
        DOCKER_IMAGE_NAME = 'oleksandraspetitions-tomcat'
        CONTAINER_NAME = 'oleksandraspetitions-app'
        HOST_PORT = '9090'
        CONTAINER_PORT = '8080'

        // // Maven JVM: Limit Maven memory to prevent Jenkins from crashing on t3.micro/small
        MAVEN_OPTS = '-Xms128m -Xmx384m -XX:MaxMetaspaceSize=192m'
    }

    options {
        timestamps()
        // Automatically stop old builds to save disk space
        buildDiscarder(logRotator(numToKeepStr: '3'))
        disableConcurrentBuilds()

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
                timeout(time: 10, unit: 'MINUTES') {
                    input message: 'Deploy to EC2?', ok: 'Deploy'
        }
    }
}
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE_NAME}:latest .'
            }
        }

        stage('Deploy') {
             steps {
                 sh 'docker rm -f ${CONTAINER_NAME} || true'
                 sh '''
                     docker run -d \
                       --name ${CONTAINER_NAME} \
                       --restart unless-stopped \
                       --memory="384m" \
                       --memory-swap="512m" \
                       -e JAVA_OPTS="-Xms128m -Xmx256m -XX:MaxMetaspaceSize=128m" \
                       -p ${HOST_PORT}:${CONTAINER_PORT} \
                       ${DOCKER_IMAGE_NAME}:latest
                 '''
                 sh 'docker ps --filter "name=${CONTAINER_NAME}"'
             }
         }


        stage('Cleanup Docker') {
            steps {
                sh 'docker image prune -af || true'
                sh 'docker builder prune -af || true'
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}
