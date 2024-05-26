# Wallets Service
In Playtomic, we have a service to manage our wallets. Our players can top-up their wallets using a credit card and spend that money on our platform (bookings, racket rentals, ...)

That service has the following operations:
- You can query your balance.
- You can top-up your wallet. In this case, we charge the amount using a third-party payments platform (stripe, paypal, redsys).
- You can spend your balance on purchases in Playtomic. 
- You can return these purchases, and your money is refunded.
- You can check your history of transactions.

This exercise consists of building a proof of concept of that wallet service.
You have to code endpoints for these operations:
1. Get a wallet using its identifier.
1. Top-up money in that wallet using a credit card number. It has to charge that amount internally using a third-party platform.

You don't have to write the following operations, but we will discuss possible solutions during the interview:
1. How to spend money from the wallet.
1. How to refund that money.

The basic structure of a wallet is its identifier and its current balance. If you think you need extra fields, add them. We will discuss it in the interview. 

So you can focus on these problems, you have here a maven project with a Spring Boot application. It already contains
the basic dependencies and an H2 database. There are development and test profiles.

You can also find an implementation of the service that would call to the real payments platform (StripePaymentService).
This implementation is calling to a simulator deployed in one of our environments. Take into account
that this simulator will return 422 http error codes under certain conditions.

Consider that this service must work in a microservices environment in high availability. You should care about concurrency too.

You can spend as much time as you need but we think that 4 hours is enough to show [the requirements of this job.](OFFER.md)
You don't have to document your code, but you can write down anything you want to explain or anything you have skipped.
You don't need to write tests for everything, but we would like to see different types of tests.


# Technical Test Backend

This is a Spring Boot project designed to demonstrate a wallet service as part of a technical test. The project includes functionalities such as wallet management and integration with a payment provider (Stripe).

## Table of Contents

- [Features](#features)
- [Runtime](#runtime)
- [Installation](#installation)
- [Usage](#usage)
- [Configuration](#configuration)
- [Dependencies](#dependencies)

## Features

- **Wallet Management**: Get wallet details and top-up wallet balance.
- **Payment Integration**: Integration with Stripe for handling payments.
- **Exception Handling**: Custom exception handling for various error scenarios.
- **DTO Mapping**: Use of Data Transfer Objects (DTOs) for API requests and responses.
- **Retry Mechanism**: Implemented retry logic for transient failures during payment processing to ensure robustness and reliability.
- **Pessimistic Locking**: Utilized pessimistic locking to prevent concurrent updates on the same wallet, ensuring data consistency and integrity.

## Runtime

- **Java 17**
- **Spring Boot 3**
- **Jakarta Persistence**
- **H2 Embedded Database**
- **JUnit / Mockito (Unit, Integration)**
- **MapStruct**
- **Lombok**
- **Maven**

## Installation

1. **Clone the repository**:
    ```sh
    git clone
    ```

2. **Build the project using Maven**:
    ```sh
    mvn clean install
    ```

3. **Run the application**:
    ```sh
    mvn spring-boot:run
    ```

## Usage

After running the application, you can interact with the wallet service through the exposed API endpoints.

### Example Endpoints

- **Get Wallet**: `GET /api/wallets/{id}`
- **Top-Up Wallet**: `POST /api/wallets/top-up`

Use tools like Postman or Curl to send HTTP requests to these endpoints.

## Dependencies

- **Spring Boot Starters**:
    - `spring-boot-starter-data-rest`
    - `spring-boot-starter-data-jpa`
    - `spring-boot-starter-logging`
    - `spring-boot-starter-web`
    - `spring-boot-starter-validation`
    - `spring-boot-starter-test` (test scope)

- **Jakarta (old javax) Persistence and Validation**:
    - `jakarta.persistence-api`

- **Spring Retry**:
    - `spring-retry`
    - `spring-aspects`

- **Lombok**:
    - `lombok`

- **Database**:
    - `h2`

- **MapStruct**:
    - `mapstruct`
    - `mapstruct-processor` (provided scope)