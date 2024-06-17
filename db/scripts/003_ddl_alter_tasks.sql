ALTER TABLE tasks ADD COLUMN user_id int;

ALTER TABLE tasks
ADD CONSTRAINT todo_user_user_id_fkey
FOREIGN KEY (user_id)
REFERENCES todo_user (id);