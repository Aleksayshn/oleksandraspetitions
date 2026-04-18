FROM tomcat:11.0-jdk21-temurin

# Remove default Tomcat web applications so the deployed app is the main focus.
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the WAR built by Maven into Tomcat for automatic deployment.
COPY target/oleksandraspetitions.war /usr/local/tomcat/webapps/oleksandraspetitions.war

EXPOSE 8080
