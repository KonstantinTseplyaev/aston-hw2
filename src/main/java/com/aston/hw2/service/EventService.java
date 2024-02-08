package com.aston.hw2.service;

import com.aston.hw2.model.dto.EventCreationDto;
import com.aston.hw2.model.dto.EventDto;
import com.aston.hw2.model.dto.EventForUpdate;
import com.aston.hw2.model.dto.EventFullDto;
import com.aston.hw2.model.dto.LocationForUpdate;

import java.util.List;

public interface EventService {
    String addNewEvent(EventCreationDto event);

    List<EventDto> getAllEvents();

    EventFullDto getEventById(long id);

    boolean updateEvent(EventForUpdate eventForUpdate, LocationForUpdate locForUpdate, Long userId);

    boolean deleteEventById(long userId, long eventId);
}
