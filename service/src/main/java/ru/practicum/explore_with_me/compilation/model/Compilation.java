package ru.practicum.explore_with_me.compilation.model;

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
@Table(name = "compilations")
public class Compilation {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "pinned", nullable = false)
    Boolean pinned;
    @Column(name = "title", nullable = false, unique = true)
    String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compilation that = (Compilation) o;
        return pinned == that.pinned && id.equals(that.id) && title.equals(that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pinned, title);
    }
}
