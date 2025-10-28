# Base image: Tomcat 9 + JDK17
FROM tomcat:9.0-jdk17

# Remove default Tomcat apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy WAR file to ROOT.war
ARG WAR_FILE=build/libs/metal-wallet-backend-1.0-SNAPSHOT.war
COPY ${WAR_FILE} /usr/local/tomcat/webapps/ROOT.war

# Expose port
EXPOSE 8080

# Start Tomcat
CMD ["sh", "-c", "catalina.sh run"]
