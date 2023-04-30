package ru.practicum.explore_with_me.event.repository;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.explore_with_me.constant.EventParamsType;
import ru.practicum.explore_with_me.constant.EventSortValue;
import ru.practicum.explore_with_me.constant.EventState;
import ru.practicum.explore_with_me.event.model.Event;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventRepositoryCustomImpl implements EventRepositoryCustom {
    @Autowired
    EntityManager entityManager;

    @Override
    public List<Event> findEventsByParams(String text, List<Long> userIds, EventState states, List<Long> categoryIds, Boolean paid, LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd, Boolean onlyAvailable, EventSortValue eventSortValue,
                                          Integer from, Integer size, EventParamsType typeParams) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Event> query = cb.createQuery(Event.class);
        Root<Event> root = query.from(Event.class);
        Predicate criteria = cb.conjunction();

        if (categoryIds != null && !categoryIds.isEmpty()) {
            Predicate containCategories = root.get("category").in(categoryIds);
            criteria = cb.and(criteria, containCategories);
        }
        if (rangeStart != null) {
            Predicate greaterRangeStart = cb.greaterThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), rangeStart);
            criteria = cb.and(criteria, greaterRangeStart);
        }
        if (rangeEnd != null) {
            Predicate lessRangeEnd = cb.lessThanOrEqualTo(root.get("eventDate").as(LocalDateTime.class), rangeEnd);
            criteria = cb.and(criteria, lessRangeEnd);
        }

        if (typeParams.equals(EventParamsType.PUBLIC)) {
            if (text != null) {
                Predicate containAnnotation = cb.like(cb.lower(root.get("annotation")),
                        "%" + text.toLowerCase() + "%");
                Predicate containDescription = cb.like(cb.lower(root.get("description")),
                        "%" + text.toLowerCase() + "%");
                Predicate containText = cb.or(containAnnotation, containDescription);

                criteria = cb.and(criteria, containText);
            }
            if (paid != null) {
                Predicate isPaid;
                if (paid) {
                    isPaid = cb.isTrue(root.get("paid"));
                } else {
                    isPaid = cb.isFalse(root.get("paid"));
                }
                criteria = cb.and(criteria, isPaid);
            }
            if (onlyAvailable != null) {
                Predicate isAvailable;
                isAvailable = root.get("state").in(EventState.PUBLISHED);
                criteria = cb.and(criteria, isAvailable);
            }
            if (EventSortValue.EVENT_DATE.equals(eventSortValue)) {
                query.select(root).where(criteria).orderBy(cb.desc(root.get("eventDate")));
            }
        }

        if (typeParams.equals(EventParamsType.ADMIN)) {
            if (userIds != null && !userIds.isEmpty()) {
                Predicate containUsers = root.get("initiator").in(userIds);
                criteria = cb.and(criteria, containUsers);
            }
            if (states != null) {
                Predicate containState = root.get("state").in(states);
                criteria = cb.and(criteria, containState);
            }
            query.select(root).where(criteria);
        }

        List<Event> events = entityManager.createQuery(query)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList();
        if (events.isEmpty()) {
            return new ArrayList<>();
        }
        return events;
    }
}
