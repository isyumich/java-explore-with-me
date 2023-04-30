package ru.practicum.explore_with_me.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore_with_me.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select u from User u")
    List<User> findUsers(Pageable pageable);

    @Query(value = "select u from User u where u.id IN :ids")
    List<User> findUsersByIds(@Param("ids") List<Long> ids, Pageable pageable);
}
