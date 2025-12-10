### ---- STAGE 1: BUILD ----
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copia apenas o pom.xml primeiro para usar cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Agora copia o restante do código
COPY src ./src

# Compila e empacota o app
RUN mvn clean package -DskipTests


### ---- STAGE 2: RUNTIME ----
FROM eclipse-temurin:17-jre AS runtime
WORKDIR /app

# Instala apenas o módulo writer (mais leve que o libreoffice completo)
RUN apt-get update && \
    apt-get install -y libreoffice-writer && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copia o jar da etapa anterior
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
