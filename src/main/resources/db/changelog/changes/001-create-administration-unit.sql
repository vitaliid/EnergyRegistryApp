--liquibase formatted sql

--changeset vitalii:001-create-administration-unit

CREATE SEQUENCE administration_unit_seq
    START WITH 1
    INCREMENT BY 50;

CREATE TABLE administration_unit
(
    id             BIGINT       NOT NULL,
    name           VARCHAR(255) NOT NULL,
    type           VARCHAR(255) NOT NULL,
    parent_unit_id BIGINT,
    CONSTRAINT pk_administration_unit PRIMARY KEY (id)
);

ALTER TABLE administration_unit
    ADD CONSTRAINT fk_administration_unit_parent
        FOREIGN KEY (parent_unit_id)
            REFERENCES administration_unit (id);

CREATE INDEX idx_administration_unit_parent
    ON administration_unit (parent_unit_id);