# Microservices Application using Clean Architecture, DDD, and Event-Driven Design
This project demonstrates the implementation of *microservices architecture* using *Clean Architecture*, *Hexagonal Architecture*,
and *Domain-Driven Design (DDD)* principles. The services communicate via *event-driven architecture* using *Apache Kafka*,
and the project also includes various architectural patterns like *CQRS*, *SAGA*, and *Outbox*.
The services are deployed using *Kubernetes* locally (via Docker) and on *Google Kubernetes Engine (GKE)*.

## Overview
The purpose of this project is to create a set of microservices that adhere to modern architectural best practices.
The system implements several patterns to improve scalability, maintainability, and fault tolerance:
- *Clean Architecture*: Enforces separation of concerns to maintain high code quality.
- *Hexagonal Architecture*: Isolates core business logic from external dependencies.
- Domain-Driven Design (DDD): Ensures the system is aligned with business needs by modeling the domain effectively.
- *Event-Driven Architecture*: Microservices communicate asynchronously via Apache Kafka to decouple them.
- *CQRS*: Separates the commands (write) and queries (read) to optimize performance.
- *SAGA Pattern*: Manages distributed transactions and ensures consistency across microservices.
- *Outbox Pattern*: Guarantees reliability in event-driven communication.
- *Kubernetes*: Services are deployed using Docker for local development and GKE for cloud deployment.

