# ---------- Сборка проекта ----------
FROM gradle:8.5-jdk17 AS builder

WORKDIR /app

# Кэшируем зависимости
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle
COPY gradle.properties ./
RUN gradle --no-daemon dependencies

# Копируем исходники и собираем fat jar
COPY . .
RUN gradle clean shadowJar --no-daemon

# ---------- Финальный образ ----------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Копируем собранный fat jar из предыдущего этапа
COPY --from=builder /app/build/libs/*.jar app.jar

# Порт, который слушает Ktor (можно поменять)
EXPOSE 8081

# Запуск приложения
ENTRYPOINT ["java", "-jar", "app.jar"]
