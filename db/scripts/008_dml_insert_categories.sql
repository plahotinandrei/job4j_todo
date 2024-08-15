--liquibase formatted sql
--changeset plahotinandrei:8
insert into categories (name) values ('проект');
insert into categories (name) values ('поручение');
insert into categories (name) values ('инициатива');
insert into categories (name) values ('инцидент');
insert into categories (name) values ('другое');