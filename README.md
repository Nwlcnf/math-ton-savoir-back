**Math Ton Savoir – Backend**

  

Backend de l’application **Math Ton Savoir**, une plateforme d’aide aux devoirs en mathématiques pour collégiens.

Ce backend est développé en **Java 17 / Spring Boot 3.4.3**, avec une base de données **PostgreSQL**, et intègre un système de monitoring **ELK** ainsi qu’une pipeline **CI/CD Docker + GitHub Actions**.

**Table des matières**

1.  [Prérequis](#Prérequis)
2.  [Installation et exécution locale](#Installation-et-exécution-locale)
3.  [Configuration](#configuration)
4.  [CI/CD](#cicd)
5.  [Tests et coverage](#tests-et-coverage)
6.  [Monitoring et logs](#monitoring-et-logs)
7.  [Docker](#docker)
8.  [Endpoints API](#endpoints-api)
9.  [Swagger / OpenAPI](#swagger--openapi)
10.  [Contribuer](#contribuer)

**Prérequis**

-   **Java 17**
-   **Maven 3.8+**
-   **Docker & Docker Compose**
-   **Compte Docker Hub** pour push des images
-   **Git**

**Installation et exécution locale**

1.  **Cloner le projet**

git clone https://github.com/Nwlcnf/math-ton-savoir-back.git
cd math-ton-savoir-back

2.  **Configurer la base de données**

Le projet utilise **PostgreSQL** via Docker Compose. Le service `db` est défini sur le port `5432`.

3.  **Lancer les services ELK pour le monitoring** _(optionnel mais recommandé)_

docker-compose up -d elasticsearch logstash kibana

4.  **Lancer l’application**

./mvnw spring-boot:run

L’API sera disponible sur : `http://localhost:8080`

**Configuration**

_Variables d’environnement_

-   `SPRING_DATASOURCE_URL` : _URL de la base PostgreSQL_ – `jdbc:postgresql://db:5432/mathtonsavoir`
-   `SPRING_DATASOURCE_USERNAME` : _Nom d’utilisateur PostgreSQL_ – `postgres`
-   `SPRING_DATASOURCE_PASSWORD` : _Mot de passe PostgreSQL_ – `example`
-   `LOGSTASH_HOST` : _Host Logstash pour monitoring_ – `logstash`
-   `LOGSTASH_PORT` : _Port Logstash_ – `5001`

Ces variables sont définies dans `docker-compose.yml`.

**CI/CD**

La CI/CD est configurée via **GitHub Actions** et comprend :

-   Build Maven
-   Exécution des tests unitaires
-   Vérification du coverage **Jacoco** _(minimum 80 %)_
-   Upload du rapport Jacoco en artifact
-   Build et push de l’image Docker sur Docker Hub

_Workflow principal_ : `./github/workflows/ci-cd.yml`

**Tests et coverage**

-   Tous les tests unitaires se trouvent dans `src/test/java`
-   Jacoco génère le rapport coverage dans `target/site/jacoco`
-   Si le coverage < 80 %, la build échoue
-   Artifact Jacoco téléchargeable via GitHub Actions pour consultation

**Monitoring et logs**

-   Stack **ELK** _(Elasticsearch, Logstash, Kibana)_ pour centraliser les logs
-   Logs envoyés via **Logstash TCP** `(port 5001)`
-   Indices Kibana : `spring-logs-YYYY.MM.DD`
-   Exemple d’accès : `http://localhost:5601 → Stack Management → Discover → spring-logs-*`

**Docker**

Le projet est dockerisé :

-   **Backend** : `nwlcnf/mathtonsavoir-backend:latest`
-   **Base de données** : PostgreSQL
-   **ELK** : Elasticsearch, Logstash, Kibana

Commandes utiles :

\# Lancer tous les services

docker-compose up -d

  

\# Stopper les services

docker-compose down

  

\# Vérifier les containers en cours

  

docker ps

  

**Endpoints API**

Tous les endpoints REST sont exposés sur `/api`.

**Endpoints principaux :**

-   `/api/auth/signup` – POST – _Inscription utilisateur_
-   `/api/auth/login` – POST – _Connexion utilisateur_
-   `/api/lecons` – GET – _Liste des leçons_
-   `/api/lecon/{id}` – GET – _Détails d’une leçon_
-   /api/exercises – GET – _Liste des exercices_
-   `/api/exercises/{id}` – GET – _Détail d’un exercice_

**Swagger / OpenAPI**

Documentation interactive : `http://localhost:8080/swagger-ui.html`

Permet de tester tous les endpoints directement depuis le navigateur.

**Contribuer**

-   Forker le projet
-   Créer une branche `feature/xxx`
-   Commit & push vos changements
-   Ouvrir un Pull Request sur `main`
