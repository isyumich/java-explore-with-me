package ru.practicum.explore_with_me.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore_with_me.category.model.Category;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.user.model.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, EventRepositoryCustom {
    @Query(value = "select * from events where id in (select event_id from compilations_events where compilation_id = :compilation_id)", nativeQuery = true)
    List<Event> findEventByCompilation(@Param("compilation_id") Long compilationId);

    @Modifying
    @Query(value = "insert into compilations_events values (:event_id, :compilation_id)", nativeQuery = true)
    void addEventToCompilation(@Param("event_id") Long eventId, @Param("compilation_id") Long compId);

    @Query(value = "select e from Event e where e.initiator = :initiator")
    List<Event> findEventsByInitiator(@Param("initiator") User initiator, Pageable pageable);

    @Query(value = "select count(user_id) from views_events where event_id = :eventId", nativeQuery = true)
    Integer getCountViews(@Param("eventId") Long eventId);

    @Query(value = "select count(id) from requests where event_id = :eventId and status = 'CONFIRMED'", nativeQuery = true)
    Integer getCountConfirmedRequests(@Param("eventId") Long eventId);

    @Query(value = "select e from Event e where e.category = :category")
    List<Event> findEventsByCategory(@Param("category") Category category);
}