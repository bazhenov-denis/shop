# ---------- BUILD STAGE ----------
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Копируем только зависимости и pom, чтобы кэшировать mvn dependency:go-offline
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходники и собираем JAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# ---------- RUN STAGE ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Копируем скомпилированный JAR из билдера
COPY --from=builder /app/target/*.jar app.jar

# Открываем порт (можно настраивать через application.properties)
EXPOSE 8080

# Запуск
ENTRYPOINT ["java", "-jar", "app.jar"]
