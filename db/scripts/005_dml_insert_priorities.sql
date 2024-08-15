--liquibase formatted sql
--changeset plahotinandrei:5
insert into priorities (name, position) values ('urgently', 1);
insert into priorities (name, position) values ('normal', 2);