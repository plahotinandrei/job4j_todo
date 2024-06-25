package ru.job4j.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.dto.TaskDetails;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
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
        model.addAttribute("task", new Task());
        model.addAttribute("priorities", priorities);
        return "tasks/create";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Task task, @RequestParam int priorityId, Model model, @SessionAttribute User user) {
        Optional<Task> taskOptional = Optional.empty();
        if (user != null) {
            task.setUser(user);
            taskOptional = taskService.create(task, priorityId);
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
        model.addAttribute("task", taskOptional.get());
        return "tasks/one";
    }

    @GetMapping("/update/{id}")
    public String update(Model model, @PathVariable int id) {
        Optional<TaskDetails> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "Задание с указанным идентификатором не найдено");
            return "errors/404";
        }
        List<Priority> priorities = priorityService.findAll();
        model.addAttribute("task", taskOptional.get());
        model.addAttribute("priorities", priorities);
        return "tasks/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Task task, @RequestParam int priorityId, Model model) {
        boolean isUpdated = taskService.update(task.getId(), task, priorityId);
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
