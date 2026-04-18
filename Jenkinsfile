pipeline {
    agent any

    environment {
        WAR_FILE = 'target/oleksandraspetitions.war'
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
                input message: 'Deploy the WAR file to Tomcat on EC2?', ok: 'Deploy'
            }
        }

        stage('Deploy Placeholder') {
            steps {
                echo "Deployment placeholder for ${WAR_FILE}"
                echo 'Future step: copy the WAR to the EC2 Tomcat webapps directory and restart Tomcat if needed.'
            }
        }
    }
}
