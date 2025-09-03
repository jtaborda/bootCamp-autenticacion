FROM eclipse-temurin:21-jdk-alpine
VOLUME /tmp
COPY *.jar autenticacion.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar autenticacion.jar"]