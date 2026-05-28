# Java Projects

This repository contains Java projects developed as part of my backend development learning path.

The projects are organized in a monorepo so their histories and progress can be viewed in one place.

## Projects

### banking-api

`banking-api` is a RESTful backend project that rebuilds the banking domain as a modern API.

The goal of this project is to practice how real backend applications are structured around HTTP endpoints, JSON request and response bodies, status codes, validation, persistence, and automated tests.

This project represents the next step after the terminal-based banking simulator. Instead of interacting with a menu in the console, clients interact with the application through endpoints such as `GET /health` and `POST /accounts`.

Main technologies:

- Java
- Spring Boot
- Maven
- PostgreSQL
- Spring Data JPA
- Bean Validation
- JUnit 5
- Layered architecture

Current focus:

- REST endpoints
- HTTP methods
- JSON request and response bodies
- HTTP status codes
- Controllers
- Services
- DTOs
- Validation
- Global error handling
- JPA entities
- PostgreSQL persistence

Main learning goal:

```text
Learn how to design and build a RESTful API using controllers, services, DTOs, repositories, validation, error handling, and database persistence.
```

Project folder:

```text
banking-api/
```

### simulador-conta-bancaria-java

`simulador-conta-bancaria-java` is an educational terminal-based banking simulator.

This was the first banking project in the learning path. It was created to practice Java fundamentals, object-oriented programming, persistence, testing, SQL, JDBC, and database transaction control before moving to Spring Boot and REST APIs.

The application models a basic banking system with accounts, deposits, withdrawals, transfers, transaction history, and persistence.

Main features:

- Account creation
- Deposits
- Withdrawals
- Transfers
- Transaction history
- CSV persistence
- SQLite persistence
- JDBC repositories
- SQL transactions with commit and rollback

Main topics covered:

- Java fundamentals
- Object-oriented programming
- Maven
- JUnit 5
- CSV persistence
- JDBC
- SQLite
- Repositories
- SQL transactions with commit and rollback

Main learning goal:

```text
Build a strong Java and persistence foundation before moving to backend API development with Spring Boot.
```

Project folder:

```text
simulador-conta-bancaria-java/
```

## Learning Path

This repository documents my progression from Java fundamentals to modern backend development.

The learning path starts with a terminal-based banking simulator and evolves into a RESTful banking API using Spring Boot and PostgreSQL.

## Repository Structure

```text
java-projects/
├── banking-api/
└── simulador-conta-bancaria-java/
```

## Commit Convention

Project-specific commits should start with the project name:

```text
banking-api: add account persistence
simulador-conta-bancaria-java: update documentation
```

Repository-level commits can use a general prefix:

```text
docs: update monorepo README
chore: organize project structure
```

## Author

Gabriel Bizerra de Araujo
