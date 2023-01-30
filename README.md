# Retail REST API 

This project was developed for ABN AMRO. API contains particular Retail operations.

# Technologies

The project is built on Spring Boot architecture. Maven used as build automation tool.

# Project Setup

### Project Dependencies

| Dependency Name | Minimum Version |
|-----------------|-----------------|
| JDK             | 11              |
| maven           | 3.8.4           |
| docker engine   | optional        |



## Spring Boot Setup

- Make sure to be in the root directory
- First build project

```
mvn clean install
```

- Run the command to run the project locally:

```
mvn spring-boot:run
```

- Open demo page url to test API

``
http://localhost:8080/client
``

- Run the command to run the tests:

```
mvn test
```

- Run the command to create runnable jar file:

```
mvn package
```

## Docker Image Setup

- First build project

```
mvn clean install
```

- Create Docker Image

```
docker build -t retail-rest-api .
```

- Run Docker Image

```
docker run -dp 8080:8080 retail-rest-api
```

- Open demo page url to test API

``
http://localhost:8080/client
``

# REST API Endpoints

## Product API `/api/v1/product`

### Product Search `/api/v1/product/{productName}` ``HTTP.GET`` `Authorization: ROLE_CUSTOMER`

With this API user can search product(s) by a keyword.

* This ``{productName}`` key is case-sensitive
* This ``{productName}`` path variable is optional variable. If you don't send it, it will return all products.

#### Request

```http request
curl --location --request GET 'http://localhost:8080/api/v1/product/Apple' \
--header 'Authorization: Basic Qm9iOnBhc3N3b3JkMQ==' \
```

#### Response

```json
[
  {
    "id": 6,
    "name": "Apple AirPods Max",
    "availableStock": 45,
    "price": 590.00,
    "lastUpdate": "2023-01-01T00:00:00"
  },
  {
    "id": 7,
    "name": "Apple AirPods Pro",
    "availableStock": 18,
    "price": 239.99,
    "lastUpdate": "2023-01-01T00:00:00"
  },
  {
    "id": 8,
    "name": "Apple iPhone SE (64GB)",
    "availableStock": 52,
    "price": 539.00,
    "lastUpdate": "2023-01-01T00:00:00"
  },
  {
    "id": 9,
    "name": "2022 Apple TV 4K",
    "availableStock": 18,
    "price": 139.99,
    "lastUpdate": "2023-01-01T00:00:00"
  },
  {
    "id": 10,
    "name": "Apple Watch SE ",
    "availableStock": 35,
    "price": 439.99,
    "lastUpdate": "2023-01-01T00:00:00"
  }
]
```

## Order API `/api/v1/order`

### Add To Cart `/api/v1/order/addToCart` ``HTTP.POST`` `Authorization: ROLE_CUSTOMER`

* With this endpoint, user can add product to cart.
* Endpoint always will return an ``id`` field in response. If user wants to add more product(s) in same cart, he/she
  needs to add this id in his/her next request(s)
* API creates a temporary order for all add to cart requests accordingly. 
* **As an improvement**; ane background thread/job could clear idle carts according to user defined configuration.

#### Request

##### Add To Cart For New Cart

```http request
curl --location --request POST 'http://localhost:8080/api/v1/order/addToCart' \
--header 'Authorization: Basic Qm9iOnBhc3N3b3JkMQ==' \
--header 'Content-Type: application/json' \
--data-raw '{
    "productId" : 5,
    "quantity" : 2
}'
```

##### Add To Cart For Existing Cart

```http request
curl --location --request POST 'http://localhost:8080/api/v1/order/addToCart' \
--header 'Authorization: Basic Qm9iOnBhc3N3b3JkMQ==' \
--header 'Content-Type: application/json' \
--data-raw '{
    "productId" : 2,
    "quantity" : 1,
    "orderId" : "c90a6a3c-bd7f-4e8b-aa37-e3f643d2fba6"
}'
```

#### Response

##### Success Response

```json
{
  "id": "1a1b739e-5706-4b2d-91e6-3bc71d027610",
  "products": [
    {
      "productId": 5,
      "productName": "HP Laserjet M110we Laserprinter",
      "productQuantity": 2,
      "productAmount": 139.99
    }
  ],
  "orderTotalAmount": 279.98,
  "totalItemCount": 2,
  "lastUpdate": "2023-01-27T19:31:21.970963"
}
```

##### Product Not Found Exception Response

```json
{
  "errorMessage": "Product Id : 14 not found",
  "errorCode": "NOT_FOUND",
  "timestamp": "27-01-2023 10:22:20"
}
```

##### Existing Cart Not Found Exception Response

```json
{
  "errorMessage": "Shopping Cart not found with cart id : c90a6a3c-bd7f-4e8b-aa37-e3f643d2fba6",
  "errorCode": "NOT_FOUND",
  "timestamp": "29-01-2023 04:26:54"
}
```

##### Access Violation Response

```json
{
  "errorMessage": "You can not make any action to the other customer's cart",
  "errorCode": "FORBIDDEN",
  "timestamp": "29-01-2023 04:34:46"
}
```

### Complete Order `/api/v1/order/{orderId}` ``HTTP.PATCH`` `Authorization: ROLE_CUSTOMER`

* With this endpoint, user can complete their waiting shopping cart.
* After completion **notification service** async thread will send an email the user that contains order details.
* **As an improvement**; SMS Notification function can be implemented to send SMS to user when order completed.
* If you want to get order details to your email address, you can update email address that is defined in user insert query in the ``data.sql``
* Before application is installed, you need to update SMTP mail credential in the ``application.yml``. Credential is shared via email.

``
INSERT INTO USERS (user_name, password, name, phone_number, email, role)
VALUES ('Bob', '0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e', 'Bob Dylan', '31698749658',
'email_to_change', 'ROLE_CUSTOMER'),
``

```yaml
spring:
  mail:
    username: will_be_provided_by_email
    password: will_be_provided_by_email
```

#### Request

```http request
curl --location --request PATCH 'http://localhost:8080/api/v1/order/c09cb414-4762-4bb1-8339-2a87637c6b97'
--header 'Authorization: Basic Qm9iOnBhc3N3b3JkMQ=='
```

#### Response

##### Success Response

```json
{
  "id": "c09cb414-4762-4bb1-8339-2a87637c6b97",
  "products": [
    {
      "productId": 1,
      "productName": "Fire TV Stick with Alexa Voice Remote",
      "productQuantity": 2,
      "productAmount": 39.99
    }
  ],
  "orderTotalAmount": 79.98,
  "totalItemCount": 2,
  "lastUpdate": "2023-01-29T16:31:11.678627"
}
```

##### Access Violation Response

```json
{
  "errorMessage": "You can not make any action to the other customer's cart",
  "errorCode": "FORBIDDEN",
  "timestamp": "29-01-2023 04:34:46"
}
```

##### Order Not Found Response

```json
{
  "errorMessage": "Order Id : edd20746-05ad-42b4-b006-0b97e0e525e12 not found",
  "errorCode": "NOT_FOUND",
  "timestamp": "29-01-2023 04:36:06"
}
```

### Get Order `/api/v1/order/{orderId}` ``HTTP.GET`` `Authorization: ROLE_EMPLOYEE`

* With this endpoint, user can get an order with it's orderId.

#### Request

```http request
curl --location --request GET 'http://localhost:8080/api/v1/order/944d4787-7fa3-492a-b7b6-3e761c9c7e02' \
--header 'Authorization: Basic TmVpbDpwYXNzd29yZDE=' 
```

#### Response

##### Success Response

```json
{
  "id": "f4daadf5-9e28-43e5-a2f9-21623d29583f",
  "products": [
    {
      "productId": 1,
      "productName": "Fire TV Stick with Alexa Voice Remote",
      "productQuantity": 2,
      "productAmount": 39.99
    }
  ],
  "orderTotalAmount": 79.98,
  "totalItemCount": 2,
  "lastUpdate": "2023-01-29T16:44:04.771777"
}
```

##### Access Denied Response

```json
{
  "errorMessage": "Access is denied",
  "errorCode": "INTERNAL_SERVER_ERROR",
  "timestamp": "29-01-2023 04:42:59"
}
```

##### Order Not Found Response

```json
{
  "errorMessage": "Order Id : 944d4787-7fa3-492a-b7b6-3e761c9c7e02 not found",
  "errorCode": "NOT_FOUND",
  "timestamp": "29-01-2023 04:42:05"
}
```

## Reporting API `/api/v1/reporting`

### Daily Top Products `/api/v1/reporting/dailyTopProducts` ``HTTP.GET`` `Authorization: ROLE_EMPLOYEE`

This API returns today's most selling five products

#### Request

```http request
curl --location --request GET 'http://localhost:8080/api/v1/reporting/dailyTopProducts' \
--header 'Authorization: Basic TmVpbDpwYXNzd29yZDE=' 
```

#### Response

#### Success Response

```json
[
  {
    "name": "Apple AirPods Max",
    "availableStock": 33,
    "price": 590.00,
    "lowQuantity": false
  },
  {
    "name": "HP Laserjet M110we Laserprinter",
    "availableStock": 4,
    "price": 139.99,
    "lowQuantity": true
  }
]
```

#### Unauthorized Response

```json
{
  "errorMessage": "Access is denied",
  "errorCode": "INTERNAL_SERVER_ERROR",
  "timestamp": "29-01-2023 04:59:56"
}
```

### Monthly Least Selling Product `/api/v1/reporting/leastSellingProductMonthly` ``HTTP.GET`` `Authorization: ROLE_EMPLOYEE`

This API returns least selling product for this month

#### Request

```http request
curl --location --request GET 'http://localhost:8080/api/v1/reporting/leastSellingProductMonthly' \
--header 'Authorization: Basic TmVpbDpwYXNzd29yZDE=' \
```

#### Response

#### Success Response

```json
{
  "name": "Fire TV Stick with Alexa Voice Remote",
  "availableStock": 22,
  "price": 39.99,
  "lowQuantity": false
}
```

#### Unauthorized Response

```json
{
  "errorMessage": "Access is denied",
  "errorCode": "INTERNAL_SERVER_ERROR",
  "timestamp": "29-01-2023 04:59:56"
}
```

### Daily Gained Amount `api/v1/reporting/dailyGainedAmount?startDate=2023-01-01&endDate=2023-01-31` ``HTTP.GET`` `Authorization: ROLE_EMPLOYEE`

This API returns daily gained amount within defined date interval

#### Request

```http request
curl --location --request GET 'http://localhost:8080/api/v1/reporting/dailyGainedAmount?startDate=2023-01-01&endDate=2023-01-31' \
--header 'Authorization: Basic TmVpbDpwYXNzd29yZDE=' \
```

#### Response

#### Success Response

```json
[
  {
    "saleAmount": 979.93,
    "day": "2023-01-28"
  },
  {
    "saleAmount": 1323.00,
    "day": "2023-01-29"
  }
]
```

#### Unauthorized Response

```json
{
  "errorMessage": "Access is denied",
  "errorCode": "INTERNAL_SERVER_ERROR",
  "timestamp": "29-01-2023 04:59:56"
}
```

# Authentication Details

* API uses `Basic Authentication` for endpoints security.
* For this authentication flow, user data (username, password) stored at in-memory database.
* Database Configurations, Database Dashboard Access detail given below.
* **User credentials** will share via email.

## Credentials

|      User Name      | UserRole  |
|---------------------|-----------|
|          Bob         | ROLE_CUSTOMER |
|          Elton         | ROLE_CUSTOMER |
|          Neil         | ROLE_EMPLOYEE |
|          Diego         | ROLE_EMPLOYEE |

## Database Configurations (application.yml)

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driverClassName: org.h2.Driver
    username: sa
    password: password
  h2:
    console:
      enabled: true
      settings:
        web-allow-others: true
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      show-sql: true
      ddl-auto: update
```

## Database Dashboard Access

```
Url : http://localhost:8080/h2-console/
```

![H2 Inmemory Database Dashboard](/assets/in_memory_database.png)

## SMTP Mail Sending Configurations (application.yml)

* **username and passoword** will be shared via email
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: will_be_provided_by_email
    password: will_be_provided_by_email
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          ssl:
            trust: smtp.gmail.com
```

# Open API Documentations

Open API documentations dashboard url given below

```
Url : http://localhost:8080/swagger-ui/#/
```

![Swagger UI Dashboard](/assets/swagger_ui.png)

# Demo Page For Trying REST API

You can use below URL to test API

```
  Url : http://localhost:8080/client
```

## Product Search

![Product Search](/assets/demo_product_search.png)

## Add To Cart

![Add To Cart](/assets/demo_add_to_cart.png)

## Complete Order

![Complete Order](/assets/demo_complete_order.png)

## Search Order

![Update Stock](/assets/demo_search_order.png)

## Daily Top Selling Product

![Daily Top Selling Product](/assets/demo_daily_top_product.png)

## Daily Least Selling Product Monthly


![Daily Least Selling Product Monthly](/assets/demo_least_selling_product.png)

## Daily Gained Amount

![Daily Gained Amount](/assets/demo_daily_gained_amount.png)

## Order Complete Email

![Order Complete Email](/assets/demo_order_complete_email.png)

## Demo Video







