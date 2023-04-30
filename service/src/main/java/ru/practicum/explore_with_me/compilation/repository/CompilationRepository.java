package ru.practicum.explore_with_me.compilation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explore_with_me.compilation.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query(value = "select c from Compilation c")
    List<Compilation> findAllCompilations(Pageable pageable);

    @Query(value = "select c from Compilation c where c.pinned = :pinned")
    List<Compilation> findCompilationsByPinned(@Param("pinned") Boolean pinned, Pageable pageable);
}
