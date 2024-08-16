package ru.job4j.todo.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.job4j.todo.dto.TaskDetails;
import ru.job4j.todo.dto.TaskPreview;
import ru.job4j.todo.mapper.TaskMapper;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.CategoryRepository;
import ru.job4j.todo.repository.PriorityRepository;
import ru.job4j.todo.repository.TaskRepository;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimpleTaskService implements TaskService {

    private final TaskRepository taskRepository;

    private final PriorityRepository priorityRepository;

    private final CategoryRepository categoryRepository;

    private final TaskMapper taskMapper = Mappers.getMapper(TaskMapper.class);

    @Override
    public Optional<Task> create(Task task, int priorityId, List<Integer> categoriesId) {
        return taskRepository.create(buildTask(task, priorityId, categoriesId));
    }

    @Override
    public boolean update(Task task, int priorityId, List<Integer> categoriesId) {
        return taskRepository.update(buildTask(task, priorityId, categoriesId));
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
    public Optional<TaskDetails> findById(int id, TimeZone tz) {
        return taskRepository.findById(id).stream()
                .peek(task -> task.setCreated(
                        task.getCreated().atZone(ZoneId.of("UTC"))
                                .withZoneSameInstant(tz.toZoneId()).toLocalDateTime())
                ).map(taskMapper::getTaskDetails)
                .findFirst();
    }

    @Override
    public List<TaskPreview> findAll(TimeZone tz) {
        return taskRepository.findAll().stream()
                .peek(task -> task.setCreated(
                        task.getCreated().atZone(ZoneId.of("UTC"))
                                .withZoneSameInstant(tz.toZoneId()).toLocalDateTime())
                ).map(taskMapper::getTaskPreview)
                .toList();
    }

    @Override
    public List<TaskPreview> findAllByDone(boolean isDone, TimeZone tz) {
        return taskRepository.findAllByDone(isDone).stream()
                .peek(task -> task.setCreated(
                        task.getCreated().atZone(ZoneId.of("UTC"))
                                .withZoneSameInstant(tz.toZoneId()).toLocalDateTime())
                ).map(taskMapper::getTaskPreview)
                .toList();
    }

    private Task buildTask(Task task, int priorityId, List<Integer> categoriesId) {
        Optional<Priority> priority = priorityRepository.findById(priorityId);
        priority.ifPresent(task::setPriority);
        Set<Category> categories =  categoriesId.stream()
                .map(categoryRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        task.setCategories(categories);
        return task;
    }
}
