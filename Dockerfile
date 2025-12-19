# Imagen base
FROM eclipse-temurin:17-jdk-alpine

# Instalar Maven y git
RUN apk add --no-cache maven git bash

# Directorio de trabajo
WORKDIR /app

# Copiar pom.xml y src
COPY pom.xml .
COPY src ./src

# Construir JAR
RUN mvn clean package -DskipTests

# Copiar el JAR generado al mismo directorio de trabajo
RUN cp target/order-management-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto
EXPOSE 8081

# Ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
