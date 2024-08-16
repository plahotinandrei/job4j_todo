--liquibase formatted sql
--changeset plahotinandrei:11
alter table tasks drop column created;
alter table tasks add column created timestamp without time zone default now();