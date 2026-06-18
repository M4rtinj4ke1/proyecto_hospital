FROM container-registry.oracle.com/java/openjdk:25 AS build
WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw mvnw
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY src ./src
RUN ./mvnw clean package -DskipTests

FROM container-registry.oracle.com/java/openjdk:25-oraclelinux9
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]