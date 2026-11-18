# Customer Account Management

A Java 8, Spring Boot 2.6.15 REST API for creating customers and accounts and
retrieving a customer's account information. Data is stored in an in-memory H2
database using Spring Data JPA.

## Requirements

- Java 8
- Maven, or use the included Maven Wrapper

## Run the application

```bash
./mvnw spring-boot:run
```

The API is available at `http://localhost:8080`. On Windows, use
`mvnw.cmd spring-boot:run`.

The H2 database is in memory and is recreated when the application restarts.
The account-number sequence starts at `10000` and increments by one.

H2 console:

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:customermanagementdb`
- User: `user`
- Password: (blank)

## API

All request bodies use `Content-Type: application/json`.

### Create a customer

`POST /api/v1/customers`

```json
{
  "customerName": "Jane Doe",
  "customerMobile": "09123456789",
  "customerEmail": "jane.doe@example.com",
  "address1": "123 Main Street",
  "address2": "Unit 4"
}
```

The mobile number must match `09XXXXXXXXX` or `+639XXXXXXXXX`. Name, mobile,
email, and address1 are required.

Successful response: `201 Created`

```json
{
  "customerNumber": 1,
  "transactionStatusCode": 201,
  "transactionDescriptionMessage": "Customer account created"
}
```

### Create an account for a customer

`POST /api/v1/accounts/create-account`

Create the customer first and use its returned `customerNumber` as `customerId`.

```json
{
  "customerId": 1,
  "accountType": "SAVINGS",
  "balance": 500.00
}
```

`accountType` must be `SAVINGS` or `CHECKING`. `customerId` must be positive;
balance must be zero or greater. The server assigns the account number.

Successful response: `201 Created`

```json
{
  "customerId": 1,
  "accountNumber": 10000,
  "accountType": "SAVINGS",
  "balance": 500.00
}
```

### Retrieve a customer's account information

`GET /api/v1/customers/{customerNumber}`

Example: `GET /api/v1/customers/1`

Successful response: `302 Found` (as required by this project)

```json
{
  "customerNumber": 1,
  "customerName": "Jane Doe",
  "customerMobile": "09123456789",
  "customerEmail": "jane.doe@example.com",
  "address1": "123 Main Street",
  "address2": "Unit 4",
  "savings": [
    {
      "customerId": 1,
      "accountNumber": 10000,
      "accountType": "SAVINGS",
      "balance": 500.00
    }
  ],
  "current": [],
  "transactionStatusCode": 302,
  "transactionDescriptionMessage": "Customer account found"
}
```

### Error responses

Invalid request fields return `400 Bad Request`. Missing customer IDs return
`404 Not Found`.

```json
{
  "transactionStatusCode": 400,
  "transactionStatusDescription": "Email is required field"
}
```

```json
{
  "transactionStatusCode": 404,
  "transactionStatusDescription": "Customer not found"
}
```

## Tests

Run the full test suite:

```bash
./mvnw clean test
```

Run a specific test class:

```bash
./mvnw -Dtest=CustomerServiceImplTest test
```

`AccountCreationIntegrationTest` runs the account creation flow against H2 and
checks that accounts persist with sequential account numbers beginning at
`10000`. The other unit tests use JUnit 5 and Mockito; controller exception
tests use MockMvc.

Run the H2 integration test by itself with:

```bash
./mvnw -Dtest=AccountCreationIntegrationTest test
```
