package ru.job4j.todo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.TaskService;
import java.util.Objects;
import java.util.TimeZone;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexController {

    private final TaskService taskService;

    @GetMapping
    public String getAll(Model model, @SessionAttribute User user) {
        TimeZone tz = user.getTimezone();
        tz = Objects.isNull(tz) ? TimeZone.getDefault() : tz;
        model.addAttribute("tasks", taskService.findAll(tz));
        return "tasks/list";
    }
}
