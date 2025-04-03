# Etapa de compilación
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
# Descarga las dependencias
RUN mvn dependency:go-offline -B
# Copia el código fuente
COPY src ./src
# Compila y empaqueta la aplicación
RUN mvn package -DskipTests

# Etapa de ejecución
FROM openjdk:17-jdk-slim
WORKDIR /app
# Copia el JAR desde la etapa de compilación
COPY --from=build /app/target/*.jar mtd.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","mtd.jar"]