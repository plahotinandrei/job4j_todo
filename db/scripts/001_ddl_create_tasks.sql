--liquibase formatted sql
--changeset plahotinandrei:1
CREATE TABLE tasks (
   id SERIAL PRIMARY KEY,
   title VARCHAR,
   description VARCHAR,
   created TIMESTAMP,
   done BOOLEAN
);