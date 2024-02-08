package com.aston.hw2.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public class LocationDto {
    private Double lat;
    private Double lon;
    private Double radius;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationDto that)) return false;
        return Objects.equals(lat, that.lat) &&
                Objects.equals(lon, that.lon) &&
                Objects.equals(radius, that.radius);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon, radius);
    }

    @Override
    public String toString() {
        return "LocationDto{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", radius=" + radius +
                '}';
    }
}
