package com.aston.hw2.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LocationForUpdate {
    private Double lat;
    private Double lon;
    private Double radius;
}
