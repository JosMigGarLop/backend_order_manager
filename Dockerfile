# Imagen base
FROM eclipse-temurin:17-jdk-alpine

# Directorio de la app
WORKDIR /app

# Copiar el jar generado
COPY target/order-management-0.0.1-SNAPSHOT.jar app.jar

# Puerto expuesto
EXPOSE 8081

# Comando para ejecutar
ENTRYPOINT ["java","-jar","app.jar"]
