package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Immutable;

import java.util.Objects;
import java.util.UUID;

/**
 * Read-only view of {@code home_administrative_area} without the parent association, used as the
 * target of references (parent, assignment, role scope) so that loading a reference never
 * cascades up the hierarchy.
 */
@Getter
@Entity
@Immutable
@Table(name = "home_administrative_area")
public class HomeAdministrativeAreaReferenceEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", length = 16)
    private String code;

    @Column(name = "level_type")
    private Integer levelType;

    @Column(name = "zone", nullable = false)
    private String zone;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HomeAdministrativeAreaReferenceEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "HomeAdministrativeAreaReferenceEntity{id=%s, name='%s', code='%s'}".formatted(id, name, code);
    }
}
