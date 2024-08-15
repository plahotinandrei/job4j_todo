package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Optional<Task> create(Task task);

    boolean update(Task task);

    Optional<Task> toDone(int id);

    boolean delete(int id);

    Optional<Task> findById(int id);

    List<Task> findAll();

    List<Task> findAllByDone(boolean isDone);
}
