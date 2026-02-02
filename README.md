# My Java Web Application

This is a simple Java web application built using Spring Boot. It connects to a database and can be deployed on Azure.

## Project Structure

```
my-java-webapp
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           ├── Application.java
│   │   │           ├── controller
│   │   │           │   └── HomeController.java
│   │   │           ├── service
│   │   │           │   └── DataService.java
│   │   │           └── repository
│   │   │               └── DataRepository.java
│   │   └── resources
│   │       ├── application.properties
│   │       ├── db
│   │       │   └── migration
│   │       │       └── V1__init.sql
│   │       └── templates
│   │           └── index.html
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── ApplicationTests.java
├── pom.xml
├── Dockerfile
├── azure-pipelines.yml
├── .gitignore
└── README.md
```

## Prerequisites

- Java 11 or higher
- Maven
- Docker (for containerization)
- Azure account (for deployment)

## Setup Instructions

1. **Clone the repository:**
   ```
   git clone <repository-url>
   cd my-java-webapp
   ```

2. **Build the project:**
   ```
   mvn clean install
   ```

3. **Run the application:**
   ```
   mvn spring-boot:run
   ```

4. **Access the application:**
   Open your web browser and navigate to `http://localhost:8080`.

## Database Configuration

Configure your database connection in `src/main/resources/application.properties`. Ensure that the database is running and accessible.

## Deployment on Azure

1. **Build the Docker image:**
   ```
   docker build -t my-java-webapp .
   ```

2. **Push the Docker image to Azure Container Registry (ACR):**
   ```
   az acr login --name <your-acr-name>
   docker tag my-java-webapp <your-acr-name>.azurecr.io/my-java-webapp
   docker push <your-acr-name>.azurecr.io/my-java-webapp
   ```

3. **Deploy the application using Azure App Service or Azure Kubernetes Service (AKS).**

## Testing

Run the unit tests using:
```
mvn test
```

## License

This project is licensed under the MIT License.