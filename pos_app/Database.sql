create database java_pos_app;

use  java_pos_app;

create table if not exists java_pos_app.cashier
(
    cashier_id    varchar(12)              not null
        primary key,
    name          varchar(125)             not null,
    age           int                      not null,
    gender        varchar(10)              null,
    address       varchar(125)             null,
    phone         varchar(15)              null,
    email         varchar(125)             null,
    password      varchar(225)             null,
    date_of_birth date                     null,
    date_created  date default (curdate()) null
);

create table if not exists java_pos_app.category
(
    id         int auto_increment
        primary key,
    name       varchar(250)             not null,
    created_at date default (curdate()) not null
);

create table if not exists java_pos_app.reports
(
    report_name   varchar(255) null,
    report_jasper blob         null
);

create table if not exists java_pos_app.supplier
(
    id                 int auto_increment
        primary key,
    company_name       varchar(100) not null,
    address            varchar(255) null,
    phone              varchar(15)  null,
    email              varchar(100) null,
    last_supplied_date date         null
);

create table if not exists java_pos_app.product
(
    id           int auto_increment
        primary key,
    barcode      varchar(50)              not null,
    product_name varchar(255)             not null,
    category_id  int                      not null,
    price        decimal(10, 2)           not null,
    supplier_id  int                      not null,
    date_added   date default (curdate()) null,
    stock        int                      not null,
    expiry_date  date                     null,
    constraint product_ibfk_1
        foreign key (category_id) references java_pos_app.category (id)
            on update cascade,
    constraint product_ibfk_2
        foreign key (supplier_id) references java_pos_app.supplier (id)
            on update cascade
);

create index category_id
    on java_pos_app.product (category_id);

create index supplier_id
    on java_pos_app.product (supplier_id);

create table if not exists java_pos_app.transaction
(
    id         varchar(30)              not null
        primary key,
    cashier_id varchar(15)              not null,
    date       date default (curdate()) not null,
    time       time default (curtime()) not null,
    amount     decimal(10, 2)           not null,
    constraint transaction_ibfk_1
        foreign key (cashier_id) references java_pos_app.cashier (cashier_id)
            on update cascade
);

create table if not exists java_pos_app.purchase
(
    id            int auto_increment
        primary key,
    transactionID varchar(30)              not null,
    cashierId     varchar(15)              not null,
    date          date default (curdate()) not null,
    time          time default (curtime()) not null,
    product_id    int                      not null,
    quantity      int                      not null,
    totalAmount   decimal(10, 2)           not null,
    constraint purchase_ibfk_1
        foreign key (cashierId) references java_pos_app.cashier (cashier_id)
            on update cascade,
    constraint purchase_ibfk_2
        foreign key (product_id) references java_pos_app.product (id)
            on update cascade,
    constraint purchase_ibfk_3
        foreign key (transactionID) references java_pos_app.transaction (id)
            on update cascade
);

create index cashierId
    on java_pos_app.purchase (cashierId);

create index product_id
    on java_pos_app.purchase (product_id);

create index transactionID
    on java_pos_app.purchase (transactionID);

create index cashier_id
    on java_pos_app.transaction (cashier_id);

