package ru.practicum.explore_with_me.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ViewStats {
    String app;
    String uri;
    long hits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewStats viewStats = (ViewStats) o;
        return hits == viewStats.hits && app.equals(viewStats.app) && uri.equals(viewStats.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri, hits);
    }
}
