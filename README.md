# oleksandraspetitions

A simple Spring Boot web application for creating, viewing, and signing petitions.

## Project Summary

This project is a university DevOps assignment. The application is intentionally simple:

- Java 21
- Spring Boot
- Maven
- Thymeleaf
- WAR packaging for later deployment to external Tomcat
- In-memory Java collections instead of a database

The goal is to keep the code easy to explain in a report, demo, and CI/CD pipeline.

## Current Features

### 1. View all petitions

- `GET /petitions`
- Shows seeded example petitions
- Displays title, short description, author, created date, and signature count

### 2. Create a petition

- `GET /petitions/new`
- `POST /petitions`
- Validates title, description, and author name
- Saves the new petition in memory

### 3. View petition details and sign a petition

- `GET /petitions/{id}`
- `POST /petitions/{id}/sign`
- Shows full petition details
- Shows all current signatures
- Validates signer name and email
- Saves signatures in memory and updates the signature count

## Project Structure

The code follows a simple MVC structure:

- `model` for domain classes
- `repository` for in-memory data storage
- `service` for application logic
- `controller` for Spring MVC endpoints
- `templates` for Thymeleaf views
- `static/css` for minimal styling

## In-Memory Data

There is no database in this version.

- Example petitions are seeded at application startup
- New petitions are stored in memory
- New signatures are stored in memory
- Data resets when the application restarts

## Run Locally

Run tests:

```bash
./mvnw test
```

Start the application:

```bash
./mvnw spring-boot:run
```

Open in the browser:

- `http://localhost:8080/`
- `http://localhost:8080/petitions`

## Build WAR File

```bash
./mvnw clean package
```

The generated file is:

- `target/oleksandraspetitions.war`

## Notes

- No database
- No REST API
- No security
- No Docker
- No JavaScript framework

This keeps the project focused on the assignment requirements and future DevOps automation steps.
