package ru.job4j.todo.service;

import ru.job4j.todo.dto.TaskDetails;
import ru.job4j.todo.dto.TaskPreview;
import ru.job4j.todo.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    Optional<Task> create(Task task);

    boolean update(int id, Task task);

    Optional<TaskDetails> toDone(int id);

    boolean delete(int id);

    Optional<TaskDetails> findById(int id);

    List<TaskPreview> findAll();

    List<TaskPreview> findAllByDone(boolean isDone);
}
