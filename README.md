# Employee Management API

## Description
Employee Management API is RESTful API built using Spring Boot.

This project provides employee management features with JWT Authentication, Role-Based Authorization

## Tech Stack
- Java 25
- Spring Boot
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT (JJWT)
- Swagger OpenAPI
- Maven

## Features
### Authentication

- Register User
- Login User
- JWT Access Token
- JWT Refresh Token

### Authorization

- USER Role
- ADMIN Role

### Employee Management

- Create Employee
- Get Employee By ID
- Get All Employee
- Update Employee
- Delete Employee

### Additional Features

- Pagination
- Sorting
- Search Employee
- Validation
- Global Exception Handling 
- Swagger Documentation

## API Endpoints
### Authentication

| Method | Endpoint |
|----------|----------|
| POST | /api/auth/register |
| POST | /api/auth/login |
| POST | /api/auth/refresh-token |


### Employee
| Method | Endpoint | Role |
|----------|----------|----------|
| GET | /api/employee | USER, ADMIN |
| GET | /api/employee/{id} | USER, ADMIN |
| POST | /api/employee | ADMIN |
| PATCH | /api/employee/{id} | ADMIN |
| DELETE | /api/employee/{id} | ADMIN |


## Database Configuration

Create PostgreSQL database;

```
CREATE DATABASE employee;

```

Update application.properties;

```
spring.datasource.url=jdbc:postgresql://localhost:5432/employee
spring.datasource.username=agussupriyanto
spring.datasource.password=
```


## How To Run
Clone repository:
```
git clone 
```

Move to project directory:
```
cd employee-api
```

Run application:
```
mvn spring-boot:run
```


## Docker Setup

This project can be run using Docker to simplify setup without installing Java and PostgreSQL locally.

### 1. Build Docker Image

```
docker build -t employee-api .
```
Run Docker Container
docker run -p 8080:8080 employee-api

Run with Docker Compose
docker compose up --build


## Swagger Documentation

Access Swagger UI :
http://localhost:8080/swagger-ui/index.html

## Login Example

Request:

```json
{
  "username": "agus123",
  "password": "password123"
}
```

Response:

```json
{
  "status": 200,
  "message": "Login success",
  "data": {
    "accessToken": "...",
    "refreshToken": "..."
  }
}
```

## Project Structure

src
├── config
├── controller
├── dto
├── entity
├── exception
├── filter
├── repository
├── service
└── validation



