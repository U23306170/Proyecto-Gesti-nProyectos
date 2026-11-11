FROM eclipse-temurin:17-jdk AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew && ./gradlew clean bootJar -x test --no-daemon

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

# Perfil dev = H2 en memoria (ddl-auto: update), en vez del perfil por defecto (MySQL)
ENV SPRING_PROFILES_ACTIVE=dev

EXPOSE 8081

# Render inyecta la variable $PORT; si no existe (ej. local), usa 8081
ENTRYPOINT ["sh", "-c", "java -Xmx300m -Xss512k -XX:+UseSerialGC -jar app.jar --server.port=${PORT:-8081}"]