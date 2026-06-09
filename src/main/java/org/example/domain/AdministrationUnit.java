package org.example.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "administration_unit")
@Cacheable
@Audited
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

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdministrationUnitAttribute> attributes = new ArrayList<>();
}
