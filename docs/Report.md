# CT5209 Cloud DevOps CA1

`oleksandraspetitions` is a Spring Boot web application built with Java 21 and Maven. The application provides four main user-facing pages: a page to create a petition, a page to view all petitions, a search page with a results page, and a petition details page where users can view information and sign a petition. To keep the assignment simple and easy to demonstrate, the application uses in-memory Java data structures rather than a database.

The project follows a clear Spring Boot MVC structure:

- `controller` handles HTTP requests and page navigation
- `service` contains the main application logic
- `repository` stores petition data in memory
- `model` contains domain classes such as `Petition` and `Signature`
- `templates` contains Thymeleaf views
- `static` contains CSS resources

The Maven project is organized in the standard structure:

- `src/main/java` for application source code
- `src/main/resources` for templates and static files
- `src/test/java` for test classes
- `pom.xml` for dependency management and build configuration

The application is packaged as a WAR file so that it can be deployed in Tomcat. The generated artifact is:

- `target/oleksandraspetitions.war`

The CI/CD workflow is implemented with Jenkins on a single Ubuntu EC2 Free Tier virtual machine. In this environment, Jenkins runs on port `8080`, while the deployed application runs on port `9090` inside a Tomcat Docker container. This separation keeps administration access and application traffic clearly separated on the same host.

The assignment workflow can be summarized as:

```text
Developer -> GitHub -> Jenkins -> Build / Test / Package -> Docker Image -> Tomcat Container on EC2
```

The pipeline is logically grouped into six assignment stages:

1. **GetProject**  
   Jenkins checks out the source code from GitHub.
2. **Build**  
   Maven compiles the application using the Maven Wrapper.
3. **Test**  
   Maven runs the automated tests to verify that the core application logic still works.
4. **Package / Archive**  
   Maven packages the project as `oleksandraspetitions.war`, and Jenkins archives the WAR artifact.
5. **Manual Approval**  
   Jenkins pauses and waits for a deployment approval step before releasing to the EC2 environment.
6. **Deploy**  
   Jenkins builds a Docker image based on Tomcat, removes the previous application container if it exists, and starts a new container mapped to host port `9090`.

Although the Jenkinsfile uses several declarative stages internally, these map cleanly to the six practical stages above. This makes the pipeline easy to explain in an academic report while still keeping the implementation structured and safe.

The Dockerfile is responsible for creating the runtime image used during deployment. It uses a Tomcat base image, removes the default Tomcat applications, copies `target/oleksandraspetitions.war` into the Tomcat `webapps` directory, and exposes port `8080` inside the container. Jenkins then runs the container with port mapping `9090:8080`, which makes the application publicly available on:

- `http://<EC2_PUBLIC_IP>:9090/oleksandraspetitions/`

The automated tests currently use:

- `spring-boot-starter-test`
- JUnit 5
- Spring Boot Test
- AssertJ

These tests focus on the application context and the main service-layer features, including listing petitions, creating petitions, searching petitions, and signing petitions.

## Reflection on Challenges

### Hardest Parts

The hardest part of this assignment was designing a simple but correct deployment model on a single EC2 virtual machine. Because Jenkins and the application both run on the same Ubuntu host, port allocation had to be handled carefully. Jenkins needed to remain accessible on port `8080`, while the application had to be deployed separately on port `9090`. If both services were configured to use the same host port, they would conflict and one of them would fail to start. The final solution was to let Jenkins continue running on the host at `8080`, while the Tomcat application container exposes `8080` internally and is mapped to host port `9090`. This is a small configuration detail, but it is a critical part of making the overall DevOps workflow reliable.

Another major challenge was Docker permissions for the Jenkins user. Jenkins runs as its own system user on Ubuntu, and by default that user does not have permission to execute Docker commands. As a result, pipeline steps such as `docker build`, `docker rm`, and `docker run` fail unless the Jenkins user is explicitly added to the Docker group. This issue highlights an important DevOps principle: CI/CD pipelines do not only depend on application code, but also on correct host-level permissions and environment setup. Solving this required understanding how Linux groups, Jenkins service accounts, and Docker interact on the EC2 instance.

From a DevOps lifecycle perspective, these challenges were useful because they showed that successful delivery is not only about writing application features. It also depends on integrating source control, build automation, test execution, containerization, host configuration, and deployment runtime behavior into one consistent workflow.

### Requirements Not Covered

One important limitation of the current implementation is that the application does not use a persistent database. Petition data is stored in in-memory Java lists, which means all created petitions and signatures are lost whenever the application restarts or the container is redeployed. This approach was acceptable for the scope of the assignment because it reduced complexity and allowed the CI/CD workflow to be demonstrated clearly, but it would not be sufficient for a real production system.

### Future Improvements

The most important improvement would be adding a persistent relational database such as MySQL. This would allow petitions and signatures to survive restarts, support larger data volumes, and make the application more realistic from both a software engineering and DevOps perspective. The application could then be extended to use Spring Data JPA with proper schema management and environment-based configuration.

From an operations perspective, a future improvement would be to move from single-container redeployment to an orchestration platform such as Kubernetes. This would make it possible to implement rolling or blue-green deployments and reduce or eliminate downtime during updates. Kubernetes would also improve scalability, recovery, and operational consistency across environments. For this assignment, that level of infrastructure would be unnecessary, but it is the logical next step for a more advanced Cloud DevOps solution.

## Dockerfile

```dockerfile
FROM tomcat:11.0-jdk21-temurin

# Remove default Tomcat web applications so the deployed app is the main focus.
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR built by Maven into Tomcat for automatic deployment.
COPY target/oleksandraspetitions.war /usr/local/tomcat/webapps/oleksandraspetitions.war

EXPOSE 8080
```

## Jenkinsfile

```groovy
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
```
