create database if not exists interview
use interview

create table receipt
(
    receipt_id    binary(16)     not null
        primary key,
    retailer      varchar(255)   null,
    purchase_date date           null,
    purchase_time time           null,
    total         decimal(10, 2) null
);

create table item
(
    item_id           binary(16)     not null
        primary key,
    receipt_id        binary(16)     null,
    short_description varchar(255)   null,
    price             decimal(10, 2) null,
    constraint item_ibfk_1
        foreign key (receipt_id) references receipt (receipt_id)
);

create index receipt_id
    on item (receipt_id);


