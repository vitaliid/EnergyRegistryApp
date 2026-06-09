package org.example.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "administration_unit_attribute")
public class AdministrationUnitAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private AdministrationUnit unit;

    @Column(name = "attr_key", nullable = false, length = 100)
    private String key;

    @Column(name = "attr_value", nullable = false, length = 255)
    private String value;
}
