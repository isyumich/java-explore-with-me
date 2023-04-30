package ru.practicum.explore_with_me.event.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "locations")
public class Location {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "lat")
    float lat;
    @Column(name = "lon")
    float lon;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Float.compare(location.lat, lat) == 0 && Float.compare(location.lon, lon) == 0 && id.equals(location.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lat, lon);
    }
}
