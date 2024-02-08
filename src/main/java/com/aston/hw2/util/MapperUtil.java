package com.aston.hw2.util;

import com.aston.hw2.model.Event;
import com.aston.hw2.model.Location;
import com.aston.hw2.model.User;
import com.aston.hw2.model.dto.EventCreationDto;
import com.aston.hw2.model.dto.EventDto;
import com.aston.hw2.model.dto.EventFullDto;
import com.aston.hw2.model.dto.LocationDto;
import com.aston.hw2.model.dto.UserRegistrationDto;

public class MapperUtil {
    public static User toUserFromUserRegistrationDto(UserRegistrationDto userRegistrationDto) {
        return User.builder()
                .name(userRegistrationDto.getName())
                .email(userRegistrationDto.getEmail())
                .password(userRegistrationDto.getPassword())
                .build();
    }

    public static Event toEventFromEventCreationDto(EventCreationDto eventCreationDto, long locId) {
        return Event.builder()
                .title(eventCreationDto.getTitle())
                .eventDate(eventCreationDto.getEventDate())
                .description(eventCreationDto.getDescription())
                .initiatorId(eventCreationDto.getInitiatorId())
                .locationId(locId)
                .build();
    }

    public static Location toLocationFromLocationDto(LocationDto locationDto) {
        return Location.builder()
                .lat(locationDto.getLat())
                .lon(locationDto.getLon())
                .radius(locationDto.getRadius())
                .build();
    }

    public static EventDto toEventDtoFromEvent(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .build();
    }

    public static LocationDto toLocationDtoFromLocation(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .radius(location.getRadius())
                .build();
    }

    public static EventFullDto toEventFullDtoFromEvent(Event event, LocationDto locationDto, String initiatorName) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(locationDto)
                .initiatorName(initiatorName)
                .build();
    }
}
