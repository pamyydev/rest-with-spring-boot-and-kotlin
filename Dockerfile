# Multi-stage build para otimizar o tamanho da imagem final

# Stage 1: Build da aplicação
FROM gradle:8.5-jdk17 AS builder

# Diretório de trabalho no container
WORKDIR /app

# Copia arquivos de configuração primeiro (melhor cache das layers)
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle/ gradle/

# Download das dependências (fica em cache se não mudar)
RUN gradle dependencies --no-daemon

# Copia o código fonte
COPY src/ src/

# Build da aplicação (gera o JAR)
# --no-daemon evita problemas em containers
# -x test pula os testes para build mais rápido (rode os testes antes!)
RUN gradle build -x test --no-daemon

# Stage 2: Imagem final de runtime
FROM openjdk:17-jre-slim

# Informações da imagem
LABEL maintainer="seu-email@exemplo.com"
LABEL description="Task Manager API - Spring Boot + Kotlin"
LABEL version="1.0.0"

# Cria usuário não-root por segurança
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Instala curl para health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Diretório da aplicação
WORKDIR /app

# Copia o JAR do stage anterior
COPY --from=builder /app/build/libs/*.jar app.jar

# Muda ownership para o usuário não-root
RUN chown appuser:appuser app.jar

# Muda para usuário não-root
USER appuser

# Porta que a aplicação usa
EXPOSE 8080

# Health check para container
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/api/actuator/health || exit 1

# Comando para iniciar a aplicação
# -XX:+UseContainerSupport: otimizações para containers
# -Djava.security.egd=file:/dev/./urandom: melhora performance do SecureRandom
ENTRYPOINT ["java", \
            "-XX:+UseContainerSupport", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-jar", \
            "app.jar"]

# Variáveis de ambiente que podem ser configuradas na AWS
ENV SPRING_PROFILES_ACTIVE=prod
ENV DATABASE_URL=""
ENV DATABASE_USERNAME=""
ENV DATABASE_PASSWORD=""
