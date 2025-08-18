FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copier le pom et les sources
COPY pom.xml mvnw ./
COPY .mvn/ .mvn
COPY src ./src

# Installer dos2unix si besoin
RUN apt-get update && apt-get install -y dos2unix
RUN dos2unix ./mvnw

# Compiler le projet et créer le jar
RUN ./mvnw clean package -DskipTests

# Lancer le jar directement
ENTRYPOINT ["java","-jar","target/backend-0.0.1-SNAPSHOT.jar"]
