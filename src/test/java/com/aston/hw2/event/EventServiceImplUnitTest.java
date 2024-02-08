package com.aston.hw2.event;

import com.aston.hw2.dao.EventDao;
import com.aston.hw2.dao.LocationDao;
import com.aston.hw2.dao.UserDao;
import com.aston.hw2.model.Event;
import com.aston.hw2.model.Location;
import com.aston.hw2.model.User;
import com.aston.hw2.model.dto.EventCreationDto;
import com.aston.hw2.model.dto.EventDto;
import com.aston.hw2.model.dto.EventForUpdate;
import com.aston.hw2.model.dto.EventFullDto;
import com.aston.hw2.model.dto.LocationDto;
import com.aston.hw2.model.dto.LocationForUpdate;
import com.aston.hw2.service.impl.EventServiceImpl;
import com.aston.hw2.util.MapperUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.aston.hw2.util.ResponseAnswers.EVENT_ADDED;
import static com.aston.hw2.util.ResponseAnswers.INCORRECT_DATE;
import static com.aston.hw2.util.ResponseAnswers.INCORRECT_LOC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class EventServiceImplUnitTest {
    @Mock
    private EventDao eventDao;
    @Mock
    private LocationDao locationDao;
    @Mock
    private UserDao userDao;
    @InjectMocks
    private EventServiceImpl eventService;
    private EventCreationDto firstEvent;
    private EventCreationDto secondEvent;
    private LocationDto firstLoc;
    private LocationDto secondLoc;
    private EventForUpdate eventForUpdate;
    private LocationForUpdate locForUpdate;
    private Event eventBeforeUpdate;
    private Location locBeforeUpdate;

    @BeforeEach
    void setUp() {
        firstLoc = LocationDto.builder()
                .lat(11.11)
                .lon(11.11)
                .radius(11.11)
                .build();

        secondLoc = LocationDto.builder()
                .lat(33.33)
                .lon(33.33)
                .build();

        firstEvent = EventCreationDto
                .builder()
                .title("event1")
                .description("descEvent1")
                .eventDate(LocalDateTime.of(2024, 10, 12, 22, 45))
                .initiatorId(1L)
                .location(firstLoc)
                .build();

        eventForUpdate = EventForUpdate
                .builder()
                .id(1L)
                .title("title")
                .description("desc")
                .eventDate(LocalDateTime.now().plusHours(1))
                .build();

        locForUpdate = LocationForUpdate.builder()
                .lat(33.33)
                .lon(33.33)
                .radius(32.23)
                .build();

        eventBeforeUpdate = Event.builder()
                .id(1L)
                .locationId(1L)
                .initiatorId(1L)
                .title("title")
                .description("desc")
                .eventDate(LocalDateTime.of(2024, 12, 12, 12, 12))
                .build();

        locBeforeUpdate = Location.builder()
                .lat(33.33)
                .lon(33.33)
                .radius(32.23)
                .build();
    }

    public EventServiceImplUnitTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("addNewEvent(EventCreationDto eventDto)")
    public class MethodAddEvent {
        @Test
        public void addEvent_withCorrectData() {
            when(locationDao.addLocation(MapperUtil.toLocationFromLocationDto(firstLoc)))
                    .thenReturn(1L);

            when(eventDao.add(MapperUtil.toEventFromEventCreationDto(firstEvent, 1L)))
                    .thenReturn(true);

            String result = eventService.addNewEvent(firstEvent);
            assertEquals(EVENT_ADDED, result);
        }

        @Test
        public void addEvent_withIncorrectEventDate() {
            secondEvent = EventCreationDto
                    .builder()
                    .title("newEvent")
                    .description("desc")
                    .initiatorId(1L)
                    .location(secondLoc)
                    .eventDate(LocalDateTime.now().plusHours(2).minusSeconds(1))
                    .build();

            String result = eventService.addNewEvent(secondEvent);
            assertEquals(INCORRECT_DATE, result);
        }

        @Test
        public void addEvent_withCorrectEventDate() {
            secondEvent = EventCreationDto
                    .builder()
                    .title("newEvent")
                    .description("desc")
                    .initiatorId(1L)
                    .location(secondLoc)
                    .eventDate(LocalDateTime.now().plusHours(2).plusSeconds(1))
                    .build();

            when(locationDao.addLocation(MapperUtil.toLocationFromLocationDto(secondLoc)))
                    .thenReturn(1L);

            when(eventDao.add(MapperUtil.toEventFromEventCreationDto(secondEvent, 1L)))
                    .thenReturn(true);

            String result = eventService.addNewEvent(secondEvent);
            assertEquals(EVENT_ADDED, result);
        }

        @Test
        public void addEvent_withIncorrectLocation() {
            LocationDto wrongLoc = LocationDto
                    .builder()
                    .lat(23.11)
                    .radius(32.23)
                    .build();

            secondEvent = EventCreationDto
                    .builder()
                    .title("newEvent")
                    .description("desc")
                    .initiatorId(1L)
                    .location(wrongLoc)
                    .eventDate(LocalDateTime.now().plusHours(3))
                    .build();

            String result = eventService.addNewEvent(secondEvent);
            assertEquals(INCORRECT_LOC, result);
        }
    }

    @Nested
    @DisplayName("getAllEvents()")
    public class MethodGetAllEvents {
        @Test
        public void getAllEvents_whenEventsAreEmpty() {
            when(eventDao.getAll())
                    .thenReturn(new ArrayList<>());

            List<EventDto> result = eventService.getAllEvents();

            assertEquals(new ArrayList<>(), result);
        }

        @Test
        public void getAllEvents_whenEventsArePresent() {
            Event event = Event.builder()
                    .id(1L)
                    .locationId(1L)
                    .title("title")
                    .description("desc")
                    .eventDate(LocalDateTime.of(2024, 12, 12, 12, 12))
                    .build();

            when(eventDao.getAll())
                    .thenReturn(List.of(event));

            List<EventDto> result = eventService.getAllEvents();
            List<EventDto> expected = List.of(MapperUtil.toEventDtoFromEvent(event));

            assertEquals(expected, result);
        }
    }

    @Nested
    @DisplayName("getEventById(long id)")
    public class MethodGetEventById {
        @Test
        public void getEventById_whenIncorrectId() {
            when(eventDao.findById(5))
                    .thenReturn(Optional.empty());

            EventFullDto result = eventService.getEventById(5);

            assertNull(result);
        }

        @Test
        public void getEventById_whenCorrectId() {
            Location loc = MapperUtil.toLocationFromLocationDto(firstLoc);

            User user = User
                    .builder()
                    .name("Max").build();

            Event event = Event.builder()
                    .id(1L)
                    .locationId(1L)
                    .initiatorId(1L)
                    .title("title")
                    .description("desc")
                    .eventDate(LocalDateTime.of(2024, 12, 12, 12, 12))
                    .build();

            when(eventDao.findById(1))
                    .thenReturn(Optional.of(event));

            when(locationDao.findById(1))
                    .thenReturn(Optional.of(loc));

            when(userDao.findById(1))
                    .thenReturn(Optional.of(user));

            EventFullDto result = eventService.getEventById(1);

            assertEquals(MapperUtil.toEventFullDtoFromEvent(event, firstLoc, "Max"), result);
        }
    }

    @Nested
    @DisplayName("updateEvent(EventForUpdate upEvent, LocationForUpdate locForUpdate, Long userId)")
    public class MethodUpdateEvent {
        @Test
        public void updateEvent_withCorrectData() {
            eventForUpdate = EventForUpdate
                    .builder()
                    .id(1L)
                    .title("title")
                    .description("desc")
                    .eventDate(LocalDateTime.now().plusHours(3))
                    .build();

            when(eventDao.findByIdAndInitiator(1L, 1L))
                    .thenReturn(Optional.of(eventBeforeUpdate));

            when(locationDao.findById(1L))
                    .thenReturn(Optional.of(locBeforeUpdate));

            when(locationDao.updateLocation(locBeforeUpdate))
                    .thenReturn(true);

            when(eventDao.update(eventBeforeUpdate))
                    .thenReturn(true);

            assertTrue(eventService.updateEvent(eventForUpdate, locForUpdate, 1L));
        }

        @Test
        public void updateEvent_withIncorrectDate() {
            when(eventDao.findByIdAndInitiator(1L, 1L))
                    .thenReturn(Optional.of(eventBeforeUpdate));

            when(locationDao.findById(1L))
                    .thenReturn(Optional.of(locBeforeUpdate));

            when(locationDao.updateLocation(locBeforeUpdate))
                    .thenReturn(true);

            when(eventDao.update(eventBeforeUpdate))
                    .thenReturn(true);

            assertFalse(eventService.updateEvent(eventForUpdate, locForUpdate, 1L));
        }
    }
}

