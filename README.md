# payments-service

## Overview

This application is developed using Spring Boot and offers two main functionalities:
1. Saving webhooks.
2. Saving payments and notifying all saved webhooks via HTTP when a payment is created.

The application leverages PostgreSQL for data storage and RabbitMQ for message brokering. You can access Swagger to view and use the respective APIs.

## Prerequisites

- Docker
- Docker Compose

## Running the Application

### Docker Compose

To run the application using Docker Compose, follow these steps:

1. Clone the repository:

    ```bash
    git clone git@github.com:vallim/payments-service.git
    cd payments-service
    ```

2. Start the application:

    ```bash
    docker-compose up
    ```

This command will start the following services:
- **app**: The Spring Boot application.
- **db**: PostgreSQL database.
- **rabbitmq**: RabbitMQ message broker with management UI.

### Accessing the Services

- **Spring Boot Application**: `http://localhost:8080`
- **RabbitMQ Management UI**: `http://localhost:15672` (default credentials: guest/guest)
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`

## Testing APIs

1. Access Swagger UI: `http://localhost:8080/swagger-ui.html`
2. Save a webhook:
```json
{
  "callbackUrl": "http://localhost:8080/api-mock/success"
}
```
3. Save a payment:
```json
{
    "firstName": "Fabricio",
    "lastName": "Vallim",
    "zipCode": "16301352",
    "cardNumber": "4111111111111111"
}
```
