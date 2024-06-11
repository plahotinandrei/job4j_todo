package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.List;

public interface TaskRepository {

    Task create(Task task);

    boolean update(int id, Task task);

    boolean delete(int id);

    List<Task> findAll();

    List<Task> findAllByDone(boolean isDone);
}
