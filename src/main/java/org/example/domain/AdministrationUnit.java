package org.example.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.GenericField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import java.util.ArrayList;
import java.util.List;


@Indexed
@Getter
@Setter
@Entity
@Table(name = "administration_unit")
@Cacheable
@Audited
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "administrationUnitCache")
public class AdministrationUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "administration_unit_seq")
    @SequenceGenerator(
            name = "administration_unit_seq",
            sequenceName = "administration_unit_seq"
    )
    @GenericField
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @GenericField
    private AdministrationUnitType type;

    @Column(name = "name", nullable = false)
    @FullTextField
    private String unitName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_unit_id")
    private AdministrationUnit parent;

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdministrationUnitAttribute> attributes = new ArrayList<>();
}
