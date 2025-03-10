# Spring Next.js Samples

![Spring Next.js Samples](https://github.com/susimsek/spring-nextjs-samples/blob/main/images/introduction.png)

## Overview

This project is a full-stack web application using Spring Boot for the backend and Next.js for the frontend. The backend
provides a RESTful API, while the frontend interacts with these endpoints to deliver a dynamic, responsive user
experience.

## Usage

Once the application is running, navigate to [http://localhost:8080](http://localhost:8080) to access the main web
interface.

![Spring Next.js Web Application](https://github.com/susimsek/spring-nextjs-samples/blob/main/images/webapp.png)

This interface is powered by Next.js and interacts with the Spring Boot backend through RESTful API endpoints.

### Login

This server already has preconfigured users.
Therefore, to login please use one of these predefined credentials:

| Username | Email             | Password | Roles |
| -------- | ----------------- | -------- | ----- |
| user     | user@example.com  | password | USER  |
| admin    | admin@example.com | password | ADMIN |

## Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21**
- **Maven 3.x**
- **Node.js 21**
- **Docker** (for running dependencies like PostgreSQL etc.)
- **Kubernetes** (if deploying to a Kubernetes cluster)

## Build

To install dependencies and build the project, run the following command:

```sh
mvn clean install
```

## Run the Application

To run the application locally with an in-memory H2 database, run the following command:

```sh
mvn spring-boot:run
```

Access H2 Console:
You can access the H2 console at:

http://localhost:8080/h2-console

Use the following credentials to log in:

| Field    | Value                                                                       |
| -------- | --------------------------------------------------------------------------- |
| Jdbc Url | `jdbc:h2:mem:demo;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE` |
| Username | `admin`                                                                     |
| Password | `root`                                                                      |

## GraalVM Native Image Support

GraalVM is used to create a native image of the backend, significantly reducing the startup time and resource usage
compared to running a JVM.

### Prerequisites

Before you begin, ensure you have the following installed:

- **Java 21**
- **GraalVM 22.3+** (ensure `native-image` is available)
- Add GraalVM to your `PATH` and set it as your active Java version

### Building a Native Image

To build a native image for the Spring Boot backend, execute:

```bash
./mvnw package -B -ntp -Pnative -DskipTests
```

This will generate a native executable in the `target` directory. You can then run the application using:

```bash
./target/native-executable
```

## Testing

To run the application's tests, use the following command:

```sh
mvn verify
```

## Code Quality

To assess code quality locally using SonarQube, execute:

```sh
mvn -Psonar compile initialize sonar:sonar
```

## Checkstyle

To check the Java code style using Checkstyle, execute:

```sh
mvn checkstyle:check
```

## Detekt

To check the Kotlin code style using Detekt, execute:

```sh
mvn detekt:check -Ddetekt.config=detekt.yml
```

## ESLint

To check the JavaScript and TypeScript code style using ESLint, execute:

```sh
npm run lint
```

To automatically fix linting issues, execute:

```sh
npm run lint:fix
```

## Prettier

To format the code using Prettier, execute:

```sh
npm run prettier:format
```

## Desktop Application

This project also supports desktop application development using **Nextron**, which is a combination of **Electron** and
**Next.js**, allowing seamless integration for cross-platform desktop applications.

![Spring Next.js Desktop Application](https://github.com/susimsek/spring-nextjs-samples/blob/main/images/desktop-app.png)

### Development

To install dependencies:

```sh
npm install
```

To start developing the desktop application:

```sh
npm run dev
```

The application will open as a desktop window.

### Build

To build the desktop application for distribution:

```sh
npm run build
```

The application packages for Windows, macOS, and Linux will be available in the `dist` folder.

## Deployment

### Docker Deployment

The application can also be fully dockerized. To achieve this, first build a Docker image of your app:

```sh
mvn verify jib:dockerBuild
```

To deploy the application and dependencies (PostgreSQL) using Docker Compose for a production environment, follow these
steps:

From the root directory of the project, use Docker Compose to deploy the application:

```sh
docker-compose -f deploy/docker-compose/prod/docker-compose.yml up -d
```

After the containers are up and running, check the logs to ensure everything is working as expected:

```sh
docker-compose -f deploy/docker-compose/prod/docker-compose.yml logs -f
```

To stop the containers, run the following command:

```sh
docker-compose -f deploy/docker-compose/prod/docker-compose.yml down
```

### Kubernetes Deployment

To deploy the application on a Kubernetes cluster using Helm, follow these steps:

Install PostgreSQL using Helm:

```sh
helm install postgresql bitnami/postgresql --values deploy/helm/postgresql/values.yaml --version 12.11.1
```

Install Spring Next.js App using Helm:

```sh
helm install spring-nextjs-app deploy/helm/spring-nextjs-app
```

To uninstall and delete the deployments, use the following commands:

Uninstall PostgreSQL:

```sh
helm uninstall postgresql
```

Uninstall Spring Next.js App:

```sh
helm uninstall spring-nextjs-app
```

## Documentation

For API documentation, please refer to the following:

### Swagger UI

Access the Swagger UI for REST API documentation:

http://localhost:8080/swagger-ui.html

![Api Documentation](https://github.com/susimsek/spring-nextjs-samples/blob/main/images/swagger-ui.png)

### GraphQL Playground

Use the GraphiQL interface to explore and test GraphQL queries:

http://localhost:8080/graphiql

![GraphQL Documentation](https://github.com/susimsek/spring-nextjs-samples/blob/main/images/graphiql.png)

## Internationalization (i18n) Support

This project supports internationalization (i18n) for English (`en`) and Turkish (`tr`). Language selection is
determined based on the `Accept-Language` header, allowing users to experience the application in their preferred
language automatically.

- **Backend (Spring Boot)**: The backend uses a custom `ParameterMessageSource` that extends Spring's `MessageSource` to
  manage multilingual support for server-side messages and responses with named parameter support.
- **Frontend (Next.js)**: The frontend utilizes `next-i18next` and `Next Language Detector` to manage translations and
  detect the user's preferred language.

### Configuration

The `i18n` configuration in Next.js automatically detects and loads the appropriate translations based on the user's
language preference. This is reflected throughout the UI components, ensuring a consistent experience in either
language.

## ER Diagram

Below is the ER Diagram used for the project:

![ER Diagram](https://github.com/susimsek/spring-nextjs-samples/blob/main/images/er-diagram.png)

## Used Technologies

- Java 21
- Kotlin
- Spring Boot 3.x
- GraalVM
- Upx
- Docker
- Kubernetes
- Checkstyle
- Detekt
- SonarQube
- Helm
- GitHub Actions
- Spring Boot Starter Web
- Spring Boot Starter GraphQL
- Spring Boot Starter Validation
- Spring Boot Starter Data JPA
- Spring Boot Starter OAuth2 Resource Server
- Spring Boot Starter Security
- Spring Boot Starter AOP
- Spring Boot Starter Cache
- Spring Boot Actuator
- Spring Boot Configuration Processor
- SpringDoc OpenAPI Starter WebMVC UI
- GraphiQL
- Liquibase
- PostgreSQL
- H2
- Spring Boot DevTools
- Caffeine Cache
- Hibernate Jpamodelgen
- Hibernate JCache
- Lombok
- Mapstruct
- Micrometer Tracing
- Micrometer Tracing Bridge OTel
- Logback Appender For Loki
- Jib Maven Plugin
- Node.js 21
- React
- Next.js
- TypeScript
- ESLint
- Prettier
- Husky
- Redux
- Next i18next
- Next Language Detector
- Axios
- Apollo Client
- GraphQL Codegen
- GraphQL ws
- React Hook Form
- Bootstrap
- Font Awesome
- Nextron
- Electron
- Electron Store
