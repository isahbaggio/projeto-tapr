FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY ../../../../Users/luizs/AppData/Local/Temp/Rar$DRa31304.8524/gateway-service .
RUN chmod +x mvnw
EXPOSE 8080
ENTRYPOINT ["./mvnw", "spring-boot:run"]