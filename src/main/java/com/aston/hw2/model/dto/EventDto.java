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
public class EventDto {
    private long id;
    private String title;
    private String description;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime eventDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventDto eventDto)) return false;
        return Objects.equals(title, eventDto.title) &&
                Objects.equals(description, eventDto.description) &&
                Objects.equals(eventDate, eventDto.eventDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, eventDate);
    }

    @Override
    public String toString() {
        return "EventDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                '}';
    }
}
