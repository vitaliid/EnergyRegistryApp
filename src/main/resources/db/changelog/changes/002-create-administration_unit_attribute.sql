--liquibase formatted sql

--changeset admin:001-create-administration-unit-attribute

CREATE TABLE administration_unit_attribute
(
    id         BIGSERIAL PRIMARY KEY,

    unit_id    BIGINT       NOT NULL,

    attr_key   VARCHAR(100) NOT NULL,
    attr_value VARCHAR(255) NOT NULL,

    CONSTRAINT fk_attr_unit
        FOREIGN KEY (unit_id)
            REFERENCES administration_unit (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_attr_unit_id
    ON administration_unit_attribute (unit_id);

CREATE INDEX idx_attr_key
    ON administration_unit_attribute (attr_key);