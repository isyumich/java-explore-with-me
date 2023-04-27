package ru.practicum.explore_with_me.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore_with_me.event.model.Event;
import ru.practicum.explore_with_me.request.model.Request;
import ru.practicum.explore_with_me.user.model.User;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query(value = "select r from Request r where r.requester = :requester")
    List<Request> findRequestByRequester(@Param("requester") User requester);

    @Query(value = "select r from Request r where r.event = :event")
    List<Request> findRequestsByEventAndRequester(@Param("event") Event event);

    @Query(value = "select r from Request r where r.id in :requestIds")
    List<Request> findRequestsByRequestIds(@Param("requestIds") List<Long> requestIds);

    @Query(value = "select count(*) from requests where requester_id = :requester_id and event_id = :event_id", nativeQuery = true)
    Integer getCountRequestsForEventAndUser(@Param("requester_id") Long requesterId, @Param("event_id") Long eventId);
}
