package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

/**
 * Home administrative area of the hierarchy (level 1 = top level, each level below increases by one).
 */
@Getter
@Setter
@Entity
@Table(name = "home_administrative_area")
public class HomeAdministrativeAreaEntity extends BaseAuditEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", length = 16)
    private String code;

    @Column(name = "population_count")
    private Integer populationCount;

    /**
     * Hierarchy level: 1 = top level, each level below increases by one.
     */
    @Column(name = "level_type")
    private Integer levelType;

    /**
     * Building zone (floor) the unit is tagged with.
     */
    @Column(name = "zone")
    private String zone;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private HomeAdministrativeAreaReferenceEntity parent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HomeAdministrativeAreaEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "HomeAdministrativeAreaEntity{id=%s, name='%s', code='%s', populationCount=%s, levelType=%s}"
                .formatted(id, name, code, populationCount, levelType);
    }
}
