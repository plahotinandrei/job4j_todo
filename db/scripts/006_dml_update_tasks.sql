--liquibase formatted sql
--changeset plahotinandrei:6
update tasks set priority_id = (select id from priorities where name = 'urgently');