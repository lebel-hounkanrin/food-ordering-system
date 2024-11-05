# Microservices Application using Clean Architecture, DDD, and Event-Driven Design
## Overview
This repository contains a microservices-based application built using Spring Boot and following the principles of Clean Architecture, Hexagonal Architecture, and Domain-Driven Design (DDD). The application also integrates Event-Driven Architecture using Apache Kafka and implements several advanced architectural patterns like SAGA, CQRS, and Outbox.

The microservices are deployed using Kubernetes both locally (via Docker Desktop) and on Google Cloud's Google Kubernetes Engine (GKE).

## Technologies Used

- Spring Boot – Framework for building Java-based microservices.
- Clean Architecture – Separation of concerns into different layers to achieve maintainability and flexibility.
- Hexagonal Architecture – Also known as Ports and Adapters, focuses on decoupling the core business logic from external systems.
- Domain-Driven Design (DDD) – Strategic design to ensure alignment between business and technical models.

## Architecture Overview

The architecture follows the principles of Clean Architecture, ensuring that the core business logic is separated from infrastructure concerns. The application is designed with the following key layers:
 1. Domain Layer (Core Business Logic) – Contains the business logic, entities, aggregates, and domain services
 2. Application Layer – Coordinates the application's use cases and orchestrates the communication between domain services.
 3. Infrastructure Layer – Manages external concerns like databases, messaging systems (Kafka), and external APIs.
 4. Adapters (Ports) – Provide the entry and exit points for interacting with external systems like databases, REST APIs, or message brokers.

