package ru.job4j.todo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ToString
public class TaskDetails {

    private int id;

    private String title;

    private String description;

    private String created;

    private String author;

    private int priorityId;

    private String priorityName;

    private List<String> categoryNames;

    private boolean done;
}
