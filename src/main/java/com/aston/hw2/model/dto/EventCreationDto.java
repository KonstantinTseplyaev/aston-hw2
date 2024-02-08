package com.aston.hw2.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EventCreationDto {
    private String title;
    private String description;
    private LocalDateTime eventDate;
    @Setter
    private Long initiatorId;
    private LocationDto location;
}
