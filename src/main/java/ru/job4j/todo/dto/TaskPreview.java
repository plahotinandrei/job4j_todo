package ru.job4j.todo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TaskPreview {

    private int id;

    private String title;

    private String created;

    private String author;

    private boolean done;
}
