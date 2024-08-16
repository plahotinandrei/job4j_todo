package ru.job4j.todo.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TimeZoneDto {

    String id;

    String name;
}
