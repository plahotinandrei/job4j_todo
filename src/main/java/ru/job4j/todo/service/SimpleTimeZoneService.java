package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.dto.TimeZoneDto;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

@Service
public class SimpleTimeZoneService implements TimeZoneService {

    @Override
    public List<TimeZoneDto> findAll() {
        return Arrays.stream(TimeZone.getAvailableIDs())
                .map((timeId) -> {
                    TimeZone tz = TimeZone.getTimeZone(timeId);
                    return TimeZoneDto.builder()
                            .id(tz.getID()).name(tz.getDisplayName())
                            .build();
                }).toList();
    }
}
