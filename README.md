# Wallet Service

## Requirements
In Playtomic, we have a service to manage our wallets. Our players can top-up their wallets using a credit card and spend that money on the platform (bookings, racket rentals, ...)

This service has the following operations:
- You can query your balance.
- You can top-up your wallet. In this case, we charge the amount using a third-party payments platform (stripe, paypal, redsys).
- You can spend your balance on purchases in Playtomic.
- You can return these purchases, and your money is refunded.
- You can check your history of transactions.

This exercise consists of building a proof of concept of this wallet service.
You have to code endpoints for these operations:
1. Get a wallet using its identifier.
1. Top-up money in that wallet using a credit card number. It has to charge that amount internally using a third-party platform.

The basic structure of a wallet is its identifier and its current balance. If you think you need extra fields, add them. We will discuss it in the interview.

So you can focus on these problems, you have here a maven project with a Spring Boot application. It already contains
the basic dependencies and an H2 database. There are development and test profiles.

You can also find an implementation of the service that would call to the real payments platform (StripePaymentService).
This implementation is calling to a simulator deployed in one of our environments. Take into account
that this simulator will return 422 http error codes under certain conditions.

Consider that this service must work in a microservices environment in high availability. You should care about concurrency.

You can spend as much time as you need but we think that 4 hours is enough to show [the requirements of this job.](OFFER.md)
You don't have to document your code, but you can write down anything you want to explain or anything you have skipped.
You don't need to write tests for everything, but we would like to see different types of tests.

## Environment
* Java 11
* Springboot
* H2 Embedded
* Embedded Redis/Jedis
* JUnit
* Docker

## Details

* Embedded redis is used for lock on charging money to provide consistency and concurrency.
* JUnit tests are available to check functionality of services(get wallet by id and charge money for wallet).
* Dockerfile is added for generating image exposing 8090.

_____________________________________________________________________________________

## API documentation
### Data Types
Wallet
```java
{
    "id": 1, <Long>
    "version": 1, <Long>
    "currency": "EUR", <Currency<String>>
    "balance": 100, <BigInteger>
    "iban": "ES0521005134909988165288", <String>
    "createdAt": "2022-02-08T17:52:49.909+00:00", <Date>
    "updatedAt": "2022-02-08T17:53:16.216+00:00" <Date>
}
```
_____________________________________________________________________________________
WalletDto
```java
{
    "id": 1, <Long>
    "version": 1, <Long>
    "currency": "EUR", <Currency<String>>
    "balance": 100, <BigInteger>
    "iban": "ES0521005134909988165288", <String>
    "createdAt": "2022-02-08T17:52:49.909+00:00", <Date>
    "updatedAt": "2022-02-08T17:53:16.216+00:00", <Date>
    "paymentIds": ["402bc740-860d-462b-b5eb-0fe3bda40c1b"] List<String>
}
```
_____________________________________________________________________________________
Payment/PaymentDto
```java
{
    "id": 1, <Long>
    "walletId": 6, <Long>
    "amount": 100, <BigInteger>
    "createdAt": "2022-02-08T17:52:49.909+00:00", <Date>
    "isRefunded": false <Boolean>
}
```
_____________________________________________________________________________________
ChargeRequestDto
```java
{
    "currency": "EUR", <String>
    "amount": 10, <BigInteger>
    "creditCardNumber": "4242424242424242" <String>
}
```
_____________________________________________________________________________________
ResponseDto
```java
{
    "code": 200, <Integer>
    "message": "Successful Operation" <String>
    "body":{
        "key": "value",
        "key": "value"
    }
}
```
_____________________________________________________________________________________
ExceptionResponseDto
```java
{
    "code": 422, <Integer>
    "status": "UNPROCESSABLE_ENTITY", <String>
    "message": "Amount is too small to charge", <String>
    "timestamp": "2022-02-08T18:50:21.268+00:00" <String>
}
```
_____________________________________________________________________________________
### Wallet API
#### GET Wallet

Definition: Returns wallet for given unique identifier.

URL: **/wallets/{id}**
Method: **GET**
Accept: **application/json**

**Sample**
```java
Request:
	Method: GET
	Headers:
    	Accept: application/json
```
```java
Response:
	Status: 200
	Headers:
     	Content-Type: application/json
	Body:
	{
            "id": 1,
            "currency": "EUR",
            "balance": 100,
            "iban": "ES0521005134909988165288",
            "createdAt": "2022-02-08T17:52:49.909+00:00",
            "updatedAt": "2022-02-08T17:53:16.216+00:00",
            "paymentIds": ["402bc740-860d-462b-b5eb-0fe3bda40c1b"]
	}
```
_____________________________________________________________________________________
#### Charge Wallet By Credit Card

Definition: Checks for amount, currency and credit card number and top up this amount for wallet via Stripe provider.

URL: **/wallets/charge/{id}**
Method: **PATCH**
Accept: **application/json**

```java
Request:
Method: PATCH
Headers:
    Accept: application/json
Body:
{
    "currency": "USD",
    "amount": 10,
    "creditCardNumber": "4242424242424242"
}
```
```java
Response:
Status: 200
Headers:
     Content-Type: application/json
Body:
{
    "code": 200,
    "message": "Successful Operation",
    "body": {
        "paymentId": "2099f778-4043-4db9-8561-00d54cb5274f"
    }
}
```
_____________________________________________________________________________________
#### Charge back Wallet

Definition: Check for wallet related payment and charge back amount of payment via Stripe provider.

URL: **/wallets/refund/{paymentId}**
Method: **GET**
Accept: **application/json**

```java
Request:
Method: PATCH
Headers:
    Accept: application/json
```
```java
Response:
Status: 200
Headers:
     Content-Type: application/json
Body:
{
    "code": 200,
    "message": "Successful Operation",
	"body": {
        "walletId": "2",
        "currentAmount": "130.00"
    }
}
```