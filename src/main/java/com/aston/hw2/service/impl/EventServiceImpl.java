package com.aston.hw2.service.impl;

import com.aston.hw2.dao.LocationDao;
import com.aston.hw2.dao.UserDao;
import com.aston.hw2.model.Event;
import com.aston.hw2.dao.EventDao;
import com.aston.hw2.model.Location;
import com.aston.hw2.model.User;
import com.aston.hw2.model.dto.EventCreationDto;
import com.aston.hw2.model.dto.EventDto;
import com.aston.hw2.model.dto.EventForUpdate;
import com.aston.hw2.model.dto.EventFullDto;
import com.aston.hw2.model.dto.LocationDto;
import com.aston.hw2.model.dto.LocationForUpdate;
import com.aston.hw2.service.EventService;
import com.aston.hw2.util.MapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.aston.hw2.util.ResponseAnswers.EVENT_ADDED;
import static com.aston.hw2.util.ResponseAnswers.EVENT_ERROR;
import static com.aston.hw2.util.ResponseAnswers.INCORRECT_DATE;
import static com.aston.hw2.util.ResponseAnswers.INCORRECT_LOC;
import static com.aston.hw2.util.ResponseAnswers.LOCATION_ERROR;

public class EventServiceImpl implements EventService {
    private final EventDao eventDao;
    private final LocationDao locationDao;
    private final UserDao userDao;
    private final ObjectMapper objectMapper;

    public EventServiceImpl(EventDao eventDao, LocationDao locationDao, UserDao userDao) {
        this.eventDao = eventDao;
        this.locationDao = locationDao;
        this.userDao = userDao;
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public String addNewEvent(EventCreationDto eventDto) {
        if (isIncorrectDate(eventDto.getEventDate())) return INCORRECT_DATE; // Эх, про скобки забыл!
        if (isIncorrectLocation(eventDto.getLocation())) return INCORRECT_LOC;// и тут

        Location newLocation = MapperUtil.toLocationFromLocationDto(eventDto.getLocation());
        long locId = locationDao.addLocation(newLocation);

        if (locId < 0) return LOCATION_ERROR; // и тут

        Event newEvent = MapperUtil.toEventFromEventCreationDto(eventDto, locId);

        if (eventDao.add(newEvent)) return EVENT_ADDED; // и тут

        return EVENT_ERROR;
    }

    @Override
    public List<EventDto> getAllEvents() {
        return eventDao.getAll()
                .stream()
                 .map(MapperUtil::toEventDtoFromEvent)
                .collect(Collectors.toList()); // каждый вызов метода на новой строке
    }

    @Override
    public EventFullDto getEventById(long id) {
        Optional<Event> eventOpt = eventDao.findById(id);

        if (eventOpt.isEmpty()) {
            return null;
        }
        Event event = eventOpt.get();
        return mapEvent(// это желательно делать в маппере
                event,
                locationDao.findById(event.getLocationId()).orElseThrow(RuntimeException::new),
                userDao.findById(event.getInitiatorId()).orElseThrow(RuntimeException::new)
        );
    }

    private EventFullDto mapEvent(Event event, Location location, User initiator) {
        return MapperUtil.toEventFullDtoFromEvent(
                event,
                MapperUtil.toLocationDtoFromLocation(location),
                initiator.getName()
        );
    }

    @Override
    public boolean updateEvent(EventForUpdate upEvent, LocationForUpdate locForUpdate, Long userId) {
        if (upEvent.getEventDate() != null && isIncorrectDate(upEvent.getEventDate())) { // зачем вкладывать if?
            return false;
        }

        Optional<Event> targetEvent = eventDao.findByIdAndInitiator(upEvent.getId(), userId);

        if (targetEvent.isPresent()) {
            Event event = targetEvent.get();
            boolean isLocUpdated = updateLocation(locForUpdate, event.getLocationId());

            try { // трай всегда выноси в отдельный метод, чтобы не загрязнять код
                String jsonUpEvent = objectMapper.writeValueAsString(upEvent);
                objectMapper.readerForUpdating(event).readValue(jsonUpEvent);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return eventDao.update(event) && isLocUpdated;
        }
        return false;
    }

    @Override
    public boolean deleteEventById(long userId, long eventId) {
        Optional<Event> targetEvent = eventDao.findByIdAndInitiator(eventId, userId);

        if (targetEvent.isEmpty()) return false;

        long locId = targetEvent.get().getLocationId();
        boolean locWasDeleted = locationDao.deleteById(locId);
        boolean eventWasDeleted = eventDao.deleteById(eventId);
        return locWasDeleted && eventWasDeleted;
    }

    private boolean updateLocation(LocationForUpdate locForUpdate, Long locId) {
        if (locForUpdate == null) return true;

        Optional<Location> targetLoc = locationDao.findById(locId);

        if (targetLoc.isPresent()) {
            Location location = targetLoc.get();
            Double newLat = locForUpdate.getLat();
            Double newLon = locForUpdate.getLon();
            Double newRadius = locForUpdate.getRadius();

            if (newLat != null) {
                location.setLat(newLat);
            }
            if (newLon != null) {
                location.setLon(newLon);
            }
            if (newRadius != null) {
                location.setRadius(newRadius);
            }
            return locationDao.updateLocation(location);
        }
        return false;
    }

    private boolean isIncorrectDate(LocalDateTime eventDate) {
        return eventDate.isBefore(LocalDateTime.now().plusHours(2));
    }

    private boolean isIncorrectLocation(LocationDto location) {
        return location.getLat() == null || location.getLon() == null;
    }
}
