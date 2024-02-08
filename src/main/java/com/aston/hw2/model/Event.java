package com.aston.hw2.model;

import com.aston.hw2.util.LocalDateTimeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class Event {
    private Long id;
    private String title;
    private String description;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime eventDate;
    private Long initiatorId;
    private Long locationId;

    @Override
    public int hashCode() {
        return Objects.hash(title, description, eventDate, initiatorId, locationId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Event event)) return false;
        return Objects.equals(this.title, event.title) &&
                Objects.equals(this.description, event.description) &&
                this.eventDate.isEqual(event.eventDate) &&
                Objects.equals(this.initiatorId, event.initiatorId) &&
                Objects.equals(this.locationId, event.locationId);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", initiatorId=" + initiatorId +
                ", locationId=" + locationId +
                '}';
    }
}
