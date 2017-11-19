# REST API for NerdBar using Spring Boot and MySQL

A REST application using Spring Boot with the following options:

- Spring JPA and MySQL for data persistence
- Swagger 2 UI to view the APIs.

To build and run the sample from a fresh clone of this repo:

## Configure MySQL

1. Create a database in your MySQL instance.
2. Update the application.properties file in the `src/main/resources` folder with the URL, username and password for your MySQL instance. 
The table schema with some sample data will be created for you in the database.


## Build and run the sample

1. `mvnw package`
3. `java -jar target/NerdBar-0.0.1-SNAPSHOT.jar`
3. Open the swagger to view API: http://localhost:8080/swagger-ui.html