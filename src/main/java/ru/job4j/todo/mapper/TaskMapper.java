package ru.job4j.todo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.job4j.todo.dto.TaskDetails;
import ru.job4j.todo.dto.TaskPreview;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Task;
import java.util.List;
import java.util.Set;

@Mapper
public interface TaskMapper {

    @Mapping(source = "task.created", target = "created", dateFormat = "dd.MM.yyyy HH:mm")
    @Mapping(source = "task.user.name", target = "author")
    @Mapping(source = "task.priority.name", target = "priority")
    TaskPreview getTaskPreview(Task task);

    @Mapping(source = "task.created", target = "created", dateFormat = "dd.MM.yyyy HH:mm")
    @Mapping(source = "task.user.name", target = "author")
    @Mapping(source = "task.priority.id", target = "priorityId")
    @Mapping(source = "task.priority.name", target = "priorityName")
    @Mapping(source = "categories", target = "categoryNames", qualifiedByName = "categoryToString")
    TaskDetails getTaskDetails(Task task);

    @Named("categoryToString")
    default List<String> mapCategories(Set<Category> categories) {
        return categories.stream()
                .map(Category::getName)
                .toList();
    }
}
