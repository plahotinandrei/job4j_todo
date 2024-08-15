--liquibase formatted sql
--changeset plahotinandrei:7
create table categories (
   id serial primary key,
   "name" varchar unique not null
);