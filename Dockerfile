FROM tomcat:11.0-jdk21-temurin

RUN rm -rf /usr/local/tomcat/webapps/*

COPY target/oleksandraspetitions.war /usr/local/tomcat/webapps/oleksandraspetitions.war

EXPOSE 8080
