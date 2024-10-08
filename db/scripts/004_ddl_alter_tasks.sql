--liquibase formatted sql
--changeset plahotinandrei:4
alter table tasks add column user_id int;

alter table tasks
add constraint todo_user_user_id_fkey
foreign key (user_id)
references todo_user (id);

alter table tasks add column priority_id int;

alter table tasks
add constraint tasks_priority_id_fkey
foreign key (priority_id)
references priorities (id);