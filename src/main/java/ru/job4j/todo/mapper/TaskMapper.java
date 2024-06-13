package ru.job4j.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.job4j.todo.dto.TaskDetails;
import ru.job4j.todo.dto.TaskPreview;
import ru.job4j.todo.model.Task;

@Mapper
public interface TaskMapper {

    @Mapping(source = "task.created", target = "created", dateFormat = "dd.MM.yyyy HH:mm")
    TaskPreview getTaskPreview(Task task);

    @Mapping(source = "task.created", target = "created", dateFormat = "dd.MM.yyyy HH:mm")
    TaskDetails getTaskDetails(Task task);
}