FROM maven:3.8.5-jdk-11-slim as build
COPY src src
COPY pom.xml ./
RUN mvn clean package

FROM amazoncorretto:11-alpine
COPY --from=build /target/reviews-api-1.0.0.jar ./

ENTRYPOINT ["java","-jar","reviews-api-1.0.0.jar"]

EXPOSE 8384