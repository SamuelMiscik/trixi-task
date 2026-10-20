# Trixi Task

Spring Boot application that parses data from `kopidlno.xml.zip`
and stores selected data into a PostgreSQL database inside a Docker container.

The application also exposes a REST API for working with municipalities
and their parts.

## Technologies

- Java 25
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL 18
- Docker / Docker Compose
- StAX XML parser
- Hazelcast
- Spring Cache
- Maven
- Postman for API testing

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

1. download and open the ZIP archive
2. parse the XML using StAX
3. create Java entities
4. save the data using Spring Data JPA
5. store the records in PostgreSQL
6. start the REST API on port `8080`

## REST API

The REST API is available under:

```text
/api/obce
```

Main endpoints include:

```text
GET    /api/obce
GET    /api/obce/{kod}
GET    /api/obce/{kod}/casti
GET    /api/obce/{kod}/casti/{castKod}

POST   /api/obce
POST   /api/obce/{kod}/casti

PUT    /api/obce/{kod}
PUT    /api/obce/{kod}/casti/{castKod}

DELETE /api/obce/{kod}
DELETE /api/obce/{kod}/casti/{castKod}
```

The REST API was manually tested using Postman.

## Caching

Hazelcast is used as the cache provider through Spring Cache.

The application uses separate caches for:

- individual municipalities
- all municipalities
- individual municipality parts
- all parts belonging to a municipality

Cached entries use a maximum idle time of 300 seconds.

Caching was manually tested by repeating API requests and verifying that
cached requests do not execute unnecessary database queries.

## Stopping the database

To stop the Docker container:

```bash
docker compose down
```