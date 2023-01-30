--Create Tables

CREATE TABLE PRODUCTS
(
    id              INT SERIAL PRIMARY KEY,
    name            varchar(80)    NOT NULL,
    available_stock INT            NOT NULL,
    price           DECIMAL(20, 2) NOT NULL,
    last_update     TIMESTAMP      NOT NULL
);

CREATE TABLE USERS
(
    id           INT SERIAL PRIMARY KEY,
    user_name    VARCHAR(250) NOT NULL,
    password     VARCHAR(250) NOT NULL,
    name         VARCHAR(250) NOT NULL,
    phone_number VARCHAR(250) NOT NULL,
    email        VARCHAR(250) NOT NULL,
    role         VARCHAR(250) NOT NULL
);

CREATE TABLE ORDERS
(
    id                 varchar(80) PRIMARY KEY,
    user_id            INT,
    order_total_amount DECIMAL(20, 2) NOT NULL,
    total_item_count   INT            NOT NULL,
    status             INT            NOT NULL,
    last_update        TIMESTAMP      NOT NULL
);

CREATE TABLE ORDER_PRODUCTS
(
    id               INT SERIAL PRIMARY KEY,
    order_id         varchar(80)    NOT NULL,
    product_id       INT            NOT NULL,
    product_quantity INT            NOT NULL,
    product_amount   DECIMAL(20, 2) NOT NULL,
    last_update      TIMESTAMP      NOT NULL
);
--Create Tables

--Insert Rows

--Products
INSERT INTO PRODUCTS (name, available_stock, price, last_update)
VALUES ('Fire TV Stick with Alexa Voice Remote', 22, 39.99, {ts '2023-01-01 00:00:00.00'}),
       ('Kindle Paperwhite Signature Edition', 14, 189, {ts '2023-01-01 00:00:00.00'}),
       ('Echo (4th generation) International version', 17, 99.99, {ts '2023-01-01 00:00:00.00'}),
       ('Echo Show 8 (2nd Generation)', 22, 129.99, {ts '2023-01-01 00:00:00.00'}),
       ('HP Laserjet M110we Laserprinter', 11, 139.99, {ts '2023-01-01 00:00:00.00'}),
       ('Apple AirPods Max', 45, 590, {ts '2023-01-01 00:00:00.00'}),
       ('Apple AirPods Pro', 18, 239.99, {ts '2023-01-01 00:00:00.00'}),
       ('Apple iPhone SE (64GB)', 52, 539, {ts '2023-01-01 00:00:00.00'}),
       ('2022 Apple TV 4K', 18, 139.99, {ts '2023-01-01 00:00:00.00'}),
       ('Apple Watch SE ', 35, 439.99, {ts '2023-01-01 00:00:00.00'});
--Products

--users
INSERT INTO USERS (user_name, password, name, phone_number, email, role)
VALUES ('Bob', '0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e', 'Bob Dylan', '31698749658',
        'emrahboz@gmail.com', 'ROLE_CUSTOMER'),
       ('Elton', '0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e', 'Elton John', '31698749658',
        'emrahboz@gmail.com', 'ROLE_CUSTOMER'),
       ('Neil', '0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e', 'Neil Armstrong', '31687452369',
        'emrahboz@gmail.com', 'ROLE_EMPLOYEE'),
       ('Diego', '0b14d501a594442a01c6859541bcb3e8164d183d32937b851835442f69d5c94e', 'Diego Maradona', '31678124520',
        'emrahboz@gmail.com', 'ROLE_EMPLOYEE');
--users
--Insert Rows