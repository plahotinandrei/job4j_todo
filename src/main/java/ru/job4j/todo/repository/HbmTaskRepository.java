package ru.job4j.todo.repository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HbmTaskRepository implements TaskRepository {

    private final CrudRepository crudRepository;

    private final static Logger LOG = LoggerFactory.getLogger(HbmTaskRepository.class.getName());

    @Override
    public Optional<Task> create(Task task) {
        Optional<Task> taskOptional = Optional.empty();
        try {
            crudRepository.run(session -> session.persist(task));
            taskOptional = Optional.of(task);
        } catch (Exception e) {
            LOG.error("Failed to create task", e);
        }
        return taskOptional;
    }

    @Override
    public boolean update(Task task) {
        return crudRepository.execute(session -> session.update(task));
    }

    @Override
    public Optional<Task> toDone(int id) {
        Optional<Task> taskOptional = crudRepository.optional(
                "from Task f JOIN FETCH f.priority LEFT JOIN FETCH f.categories where f.id=:id",
                Task.class, Map.of("id", id)
        );
        if (taskOptional.isPresent()) {
            crudRepository.run("UPDATE Task SET done=true WHERE id = :id", Map.of("id", id));
        }
        return taskOptional;
    }

    @Override
    public boolean delete(int id) {
        boolean rsl = false;
        Optional<Task> taskOptional = crudRepository.optional(
                "from Task f LEFT JOIN FETCH f.categories where f.id=:id",
                Task.class, Map.of("id", id)
        );
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.getCategories().clear();
            rsl = crudRepository.execute(session -> session.remove(task));
        }
        return rsl;
    }

    @Override
    public Optional<Task> findById(int id) {
        return crudRepository.optional(
                "from Task f JOIN FETCH f.priority LEFT JOIN FETCH f.categories where f.id=:id",
                Task.class, Map.of("id", id)
        );
    }

    @Override
    public List<Task> findAll() {
        return crudRepository.query("from Task f JOIN FETCH f.priority", Task.class);
    }

    @Override
    public List<Task> findAllByDone(boolean isDone) {
        return crudRepository.query(
                "from Task f JOIN FETCH f.priority where f.done=:done",
                Task.class, Map.of("done", isDone)
        );
    }
}
