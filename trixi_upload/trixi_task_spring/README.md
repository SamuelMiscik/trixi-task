# Trixi Task

Spring Boot application that parses data from `kopidlno.xml.zip`
and stores selected data into a PostgreSQL database inside a Docker container.

## Technologies

- Java 25
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL 18
- Docker / Docker Compose
- StAX XML parser
- Maven

## Database

PostgreSQL runs inside a Docker container.

The Docker configuration creates:

- database: `trixi`
- user: `postgres`
- password: `trixi`
- PostgreSQL version: 18

## Database schema

The database schema is defined in:

```text
schema.sql
```

Docker mounts this file into PostgreSQL's initialization directory:

```text
/docker-entrypoint-initdb.d/
```

When the PostgreSQL container is initialized for the first time,
the script automatically creates the required tables.

## Running the application

### 1. Start PostgreSQL

Docker Desktop must be installed and running.

From the project root run:

```bash
docker compose up -d
```

This starts the PostgreSQL container.

### 2. Run the Spring Boot application

The application will:

1. open the ZIP archive
2. parse the XML using StAX
3. create Java entities
4. save the data using Spring Data JPA
5. store the records in PostgreSQL

## Stopping the database

To stop the Docker container:

```bash
docker compose down
```
