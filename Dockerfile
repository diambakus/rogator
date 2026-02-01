FROM eclipse-temurin:21-jdk-jammy
LABEL authors="adruida"

# Create a system user
RUN addgroup --system orakuma && adduser --system orakuma --ingroup orakuma
USER orakuma:orakuma

# Copy the jar (build it locally first)
COPY target/*.jar /opt/app/rogator.jar

# Run the app
CMD ["java", "-showversion", "-jar", "/opt/app/rogator.jar"]
