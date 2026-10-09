--liquibase formatted sql

--changeset vitalii:001-create-building-zone-table
--comment: Building zones (floors), reference data every home administrative area and outside public area is tagged with.
--comment: Ids follow the same HOMEnnn code format as home_administrative_area.code.
CREATE TABLE building_zone
(
    id   VARCHAR(16) NOT NULL,
    name VARCHAR(32) NOT NULL,

    CONSTRAINT pk_building_zone PRIMARY KEY (id)
);

INSERT INTO building_zone (id, name)
VALUES ('HOME001', 'Basement'),
       ('HOME002', 'Ground floor'),
       ('HOME003', 'First floor'),
       ('HOME004', 'Second floor'),
       ('HOME005', 'Attic');

--rollback DROP TABLE building_zone;
