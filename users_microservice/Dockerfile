# Use a slim, secure base (match your Java version: 17/21/etc.)
FROM eclipse-temurin:21-jre-alpine

# Non-root user for security
RUN addgroup -S appgroup && adduser -S -D -h /home/appuser -s /bin/sh -G appgroup appuser


WORKDIR /app

# Copy the JAR built by Maven
COPY users_microservice/target/users_microservice-*.jar app.jar

# Switch to non-root user
USER appuser

# Your app's port (change if not 8080)
EXPOSE 9098

# Optional: add JVM flags, e.g. for memory or profiles
ENTRYPOINT ["java", "-jar", "/app/app.jar"]