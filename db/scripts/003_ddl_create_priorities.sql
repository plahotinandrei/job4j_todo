--liquibase formatted sql
--changeset plahotinandrei:3
create table priorities (
   id serial primary key,
   "name" varchar unique not null ,
   position int
);