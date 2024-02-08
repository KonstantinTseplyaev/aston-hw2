package com.aston.hw2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Builder
@AllArgsConstructor
public class Location {
    private Long id;
    @Setter
    private Double lat;
    @Setter
    private Double lon;
    @Setter
    private Double radius;

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon, radius);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Location loc)) return false;
        return Objects.equals(this.lat, loc.lat) &&
                Objects.equals(this.lon, loc.lon) &&
                Objects.equals(this.radius, loc.radius);
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", radius=" + radius +
                '}';
    }
}
