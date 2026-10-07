# Trixi task

Java application that downloads and parses XML data
and stores selected data into PostgreSQL.

## Technologies

- Java
- Maven
- PostgreSQL
- JPA
- Hibernate
- StAX

## Database setup

1. Create PostgreSQL database:

   CREATE DATABASE trixi;

2. Run `trixi_databaza.sql`.

3. Configure PostgreSQL credentials in:
   `src/main/resources/META-INF/persistence.xml`

## Run

Run `cz.miscik.Main`.
