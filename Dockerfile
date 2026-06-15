FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY gradlew gradlew.bat settings.gradle.kts build.gradle.kts gradle.properties ./
COPY gradle ./gradle
COPY src ./src
RUN chmod +x gradlew && ./gradlew installDist --no-daemon

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
RUN groupadd -r app && useradd -r -g app app \
    && mkdir -p /data && chown app:app /data
COPY --from=build /app/build/install/morphlect-server/ ./
COPY --chown=app:app models ./models
USER app
EXPOSE 8080
ENV PORT=8080
ENV SQLITE_DB_PATH=/data/db.sqlite
ENV MODELS_PATH=/app/models
VOLUME ["/data"]
CMD ["./bin/morphlect-server"]