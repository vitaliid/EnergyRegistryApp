package org.example.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "administration_unit")
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
}
