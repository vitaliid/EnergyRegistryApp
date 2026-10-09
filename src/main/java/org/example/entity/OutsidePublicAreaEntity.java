package org.example.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

/**
 * An outside public area (institution) belonging to a home administrative area.
 */
@Getter
@Setter
@Entity
@Table(name = "outside_public_area")
public class OutsidePublicAreaEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Column(name = "zone")
    private String zone;

    @ManyToOne(optional = false)
    @JoinColumn(name = "home_administrative_area_id", nullable = false)
    private HomeAdministrativeAreaReferenceEntity homeAdministrativeArea;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutsidePublicAreaEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OutsidePublicAreaEntity{id=%s, name='%s', zone='%s'}".formatted(id, name, zone);
    }
}
