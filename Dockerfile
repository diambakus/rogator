FROM eclipse-temurin:21-jdk-jammy
LABEL authors="adruida"

# Create a system user
RUN addgroup --system orakuma && adduser --system orakuma --ingroup orakuma
# Upload storage (must be writable by the app user; mount a host volume here to persist files)
RUN mkdir -p /opt/uploads/rogator && chown -R orakuma:orakuma /opt/uploads/rogator
VOLUME /opt/uploads/rogator

USER orakuma:orakuma

# Copy the jar (build it locally first)
COPY target/*.jar /opt/app/rogator.jar

# Run the app
CMD ["java", "-showversion", "-jar", "/opt/app/rogator.jar"]
