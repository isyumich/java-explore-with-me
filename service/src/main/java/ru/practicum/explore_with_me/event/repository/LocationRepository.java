package ru.practicum.explore_with_me.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore_with_me.event.model.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
