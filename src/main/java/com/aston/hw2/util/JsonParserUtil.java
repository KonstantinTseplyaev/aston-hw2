package com.aston.hw2.util;

import com.aston.hw2.model.dto.EventCreationDto;
import com.aston.hw2.model.dto.EventForUpdate;
import com.aston.hw2.model.dto.LocationDto;
import com.aston.hw2.model.dto.LocationForUpdate;
import com.aston.hw2.model.dto.UserLoginDto;
import com.aston.hw2.model.dto.UserRegistrationDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonParserUtil {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static UserRegistrationDto toUserRegistrationDtoFromJson(HttpServletRequest req) {
        JSONObject jsonUser = getJsonObject(req);

        String name = jsonUser.get("name").toString();
        String email = jsonUser.get("email").toString();
        String password = CryptoTool.hashOf(jsonUser.get("password").toString());

        return UserRegistrationDto.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
    }

    public static UserLoginDto toUserLoginDtoFromJson(HttpServletRequest req) {
        JSONObject jsonUser = getJsonObject(req);

        String email = jsonUser.get("email").toString();
        String password = jsonUser.get("password").toString();

        return UserLoginDto.builder()
                .email(email)
                .password(password)
                .build();
    }

    public static EventCreationDto toEventCreationDtoFromJson(HttpServletRequest req) {
        JSONObject jsonEvent = getJsonObject(req);

        String title = jsonEvent.get("title").toString();
        String description = jsonEvent.get("description").toString();
        LocalDateTime eventDate = LocalDateTime.parse(jsonEvent.get("eventDate").toString(), formatter);

        JSONObject jsonLocation = jsonEvent.getJSONObject("location");
        Double lat = Double.parseDouble(jsonLocation.get("lat").toString());
        Double lon = Double.parseDouble(jsonLocation.get("lon").toString());
        Double radius = null;

        if (jsonLocation.has("radius")) {
            radius = Double.parseDouble(jsonLocation.get("radius").toString());
        }

        LocationDto location = LocationDto.builder()
                .lat(lat)
                .lon(lon)
                .radius(radius)
                .build();

        return EventCreationDto.builder()
                .title(title)
                .description(description)
                .eventDate(eventDate)
                .location(location)
                .build();
    }

    public static JSONObject getUpdateEventJson(HttpServletRequest req) {
        return getJsonObject(req);
    }

    public static LocationForUpdate toLocationForUpdateFromJson(JSONObject jsonEvent, ObjectMapper mapper) {
        if (jsonEvent.has("location")) {
            LocationForUpdate locForUp = LocationForUpdate.builder().build();
            JSONObject jsonLocation = jsonEvent.getJSONObject("location");
            jsonEvent.remove("location");
            try {
                mapper.readerForUpdating(locForUp).readValue(jsonLocation.toString());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return locForUp;
        }
        return null;
    }

    public static EventForUpdate toEventForUpdateFromJson(JSONObject jsonEvent, ObjectMapper mapper) {
        EventForUpdate eventForUpdate = EventForUpdate.builder().build();
        try {
            mapper.readerForUpdating(eventForUpdate).readValue(jsonEvent.toString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return eventForUpdate;
    }

    private static JSONObject getJsonObject(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader reader = req.getReader();
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String reqBody = sb.toString();
        return new JSONObject(reqBody);
    }
}
