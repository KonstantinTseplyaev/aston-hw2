package com.aston.hw2.model.dto;

import com.aston.hw2.util.LocalDateTimeDeserializer;
import com.aston.hw2.util.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
public class EventFullDto {
    private long id;
    private String title;
    private String description;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime eventDate;
    private String initiatorName;
    private LocationDto location;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventFullDto that)) return false;
        return id == that.id &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(eventDate, that.eventDate) &&
                Objects.equals(initiatorName, that.initiatorName) &&
                Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, eventDate, initiatorName, location);
    }

    @Override
    public String toString() {
        return "EventFullDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", initiatorName='" + initiatorName + '\'' +
                ", location=" + location +
                '}';
    }
}
