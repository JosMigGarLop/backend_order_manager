# Imagen base
FROM eclipse-temurin:17-jdk-alpine

# Directorio de trabajo
WORKDIR /app

# Instalar Maven (si no usas mvnw)
RUN apk add --no-cache maven git bash

# Copiar pom.xml y fuentes
COPY pom.xml .
COPY src ./src

# Construir el JAR
RUN mvn clean package -DskipTests

# Copiar el JAR generado
COPY target/order-management-0.0.1-SNAPSHOT.jar app.jar

# Exponer puerto
EXPOSE 8081

# Ejecutar
ENTRYPOINT ["java","-jar","app.jar"]
