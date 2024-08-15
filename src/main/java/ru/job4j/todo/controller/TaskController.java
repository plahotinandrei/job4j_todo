package ru.job4j.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.dto.TaskDetails;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final PriorityService priorityService;

    private final CategoryService categoryService;

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/done")
    public String getAllDone(Model model) {
        model.addAttribute("tasks", taskService.findAllByDone(true));
        return "tasks/list";
    }

    @GetMapping("/new")
    public String getAllNew(Model model) {
        model.addAttribute("tasks", taskService.findAllByDone(false));
        return "tasks/list";
    }

    @GetMapping("/create")
    public String getCreationPage(Model model) {
        List<Priority> priorities = priorityService.findAll();
        List<Category> categories = categoryService.findAll();
        model.addAttribute("task", new Task());
        model.addAttribute("priorities", priorities);
        model.addAttribute("categories", categories);
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(
            @ModelAttribute Task task,
            @RequestParam int priorityId,
            @RequestParam List<Integer> categoriesId,
            Model model,
            @SessionAttribute User user
    ) {
        Optional<Task> taskOptional = Optional.empty();
        if (user != null) {
            task.setUser(user);
            taskOptional = taskService.create(task, priorityId, categoriesId);
        }
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "При создании задания произошла ошибка");
            return "errors/409";
        }
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable int id) {
        Optional<TaskDetails> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено");
            return "errors/404";
        }
        TaskDetails taskDetails = taskOptional.get();
        model.addAttribute("task", taskDetails);
        model.addAttribute("categories", String.join(", ", taskDetails.getCategoryNames()));
        return "tasks/one";
    }

    @GetMapping("/update/{id}")
    public String update(Model model, @PathVariable int id) {
        Optional<TaskDetails> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено");
            return "errors/404";
        }
        TaskDetails taskDetails = taskOptional.get();
        List<Priority> priorities = priorityService.findAll();
        List<Category> categories = categoryService.findAll();
        model.addAttribute("task", taskDetails);
        model.addAttribute("categoryNames", taskDetails.getCategoryNames());
        model.addAttribute("priorities", priorities);
        model.addAttribute("categories", categories);
        return "tasks/update";
    }

    @PostMapping("/update")
    public String update(
            @ModelAttribute Task task,
            @RequestParam int priorityId,
            @RequestParam List<Integer> categoriesId,
            Model model
    ) {
        boolean isUpdated = taskService.update(task, priorityId, categoriesId);
        if (!isUpdated) {
            model.addAttribute("message", "При обновлении произошла ошибка");
            return "errors/409";
        }
        return String.format("redirect:/tasks/%d", task.getId());
    }

    @GetMapping("/done/{id}")
    public String toDone(Model model, @PathVariable int id) {
        Optional<TaskDetails> taskOptional = taskService.toDone(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено");
            return "errors/404";
        }
        TaskDetails task = taskOptional.get();
        model.addAttribute("task", task);
        return String.format("redirect:/tasks/%d", task.getId());
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable int id) {
        var isDeleted = taskService.delete(id);
        if (!isDeleted) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено");
            return "errors/404";
        }
        return "redirect:/";
    }
}
