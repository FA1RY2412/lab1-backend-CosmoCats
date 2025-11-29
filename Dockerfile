FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Аргумент з шляхом до jar (за замовчуванням — єдиний jar у build/libs)
ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
