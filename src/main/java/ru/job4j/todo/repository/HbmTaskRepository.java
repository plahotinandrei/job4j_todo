package ru.job4j.todo.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HbmTaskRepository implements TaskRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<Task> create(Task task) {
        Optional<Task> taskOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(task));
            taskOptional = Optional.of(task);
        } catch (Exception ignored) {

        }
        return taskOptional;
    }

    @Override
    public boolean update(int id, Task task) {
        return crudRepository.run(
                "UPDATE Task SET title = :title, description = :description, done=:done WHERE id = :id",
                Map.of("id", id, "title", task.getTitle(),
                        "description", task.getDescription(), "done", task.isDone())
        );
    }

    @Override
    public Optional<Task> toDone(int id) {
        Optional<Task> taskOptional = crudRepository.optional("from Task where id=:id", Task.class, Map.of("id", id));
        if (taskOptional.isPresent()) {
            crudRepository.run("UPDATE Task SET done=true WHERE id = :id", Map.of("id", id));
        }
        return taskOptional;
    }

    @Override
    public boolean delete(int id) {
        return crudRepository.run("DELETE Task WHERE id = :id", Map.of("id", id));
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional("from Task where id=:id", Task.class, Map.of("id", id));
    }

    @Override
    public List<Task> findAll() {
        return crudRepository.query("from Task", Task.class);
    }

    @Override
    public List<Task> findAllByDone(boolean isDone) {
        return crudRepository.query("from Task where done=:done", Task.class, Map.of("done", isDone));
    }
}
