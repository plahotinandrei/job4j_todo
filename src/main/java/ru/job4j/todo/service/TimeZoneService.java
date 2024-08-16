package ru.job4j.todo.service;

import ru.job4j.todo.dto.TimeZoneDto;
import java.util.List;

public interface TimeZoneService {

    List<TimeZoneDto> findAll();
}
