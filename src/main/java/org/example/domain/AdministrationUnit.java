package org.example.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Getter
@Setter
@Entity
@Table(name = "administration_unit")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdministrationUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "administration_unit_seq")
    @SequenceGenerator(
            name = "administration_unit_seq",
            sequenceName = "administration_unit_seq"
    )
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AdministrationUnitType type;

    @Column(name = "name", nullable = false)
    private String unitName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_unit_id")
    private AdministrationUnit parent;
}
