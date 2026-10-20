# trixi-task

Java backend task implemented in two versions.

This repository contains:

- `trixi-jpa` – original solution using JPA, Hibernate and PostgreSQL
- `trixi-spring-docker` – extended Spring Boot solution with:
  - Spring Data JPA
  - PostgreSQL running in Docker
  - StAX XML parsing
  - REST API for `Obec` and `CastObce`
  - DTO-based API communication
  - Hazelcast caching via Spring Cache
  - manual API and cache testing with Postman

The extended version downloads and parses XML data, stores selected records in PostgreSQL and exposes CRUD operations through a REST API.
