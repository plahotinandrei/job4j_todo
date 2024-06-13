package ru.job4j.todo.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.job4j.todo.dto.TaskDetails;
import ru.job4j.todo.dto.TaskPreview;
import ru.job4j.todo.mapper.TaskMapper;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SimpleTaskService implements TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    @Override
    public Optional<Task> create(Task task) {
        return taskRepository.create(task);
    }

    @Override
    public boolean update(int id, Task task) {
        return taskRepository.update(id, task);
    }

    @Override
    public Optional<TaskDetails> toDone(int id) {
        return taskRepository.toDone(id).map(taskMapper::getTaskDetails);
    }

    @Override
    public boolean delete(int id) {
        return taskRepository.delete(id);
    }

    @Override
    public Optional<TaskDetails> findById(int id) {
        return taskRepository.findById(id).map(taskMapper::getTaskDetails);
    }

    @Override
    public List<TaskPreview> findAll() {
        return taskRepository.findAll().stream()
                .map(taskMapper::getTaskPreview)
                .toList();
    }

    @Override
    public List<TaskPreview> findAllByDone(boolean isDone) {
        return taskRepository.findAllByDone(isDone).stream()
                .map(taskMapper::getTaskPreview)
                .toList();
    }
}
