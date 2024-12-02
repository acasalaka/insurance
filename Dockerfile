FROM eclipse-temurin:21-jre-alpine

COPY insurance2206823682/build/libs/insurance2206823682-0.0.1-SNAPSHOT.jar /app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app.jar"]