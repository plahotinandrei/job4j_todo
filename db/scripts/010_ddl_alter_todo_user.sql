--liquibase formatted sql
--changeset plahotinandrei:10
alter table todo_user add column timezone varchar not null default 'UTC';