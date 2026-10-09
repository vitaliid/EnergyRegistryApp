--liquibase formatted sql

--changeset vitalii:003-create-outside-public-area-table
--comment: Outside public areas (institutions) belonging to a home administrative area
CREATE TABLE outside_public_area
(
    id                     UUID                     NOT NULL DEFAULT gen_random_uuid(),
    name                   VARCHAR(256)             NOT NULL,
    zone          VARCHAR(16)               NOT NULL,
    home_administrative_area_id UUID                     NOT NULL,

    created_at             TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_outside_public_area PRIMARY KEY (id),
    CONSTRAINT fk_outside_public_area_home_administrative_area
        FOREIGN KEY (home_administrative_area_id) REFERENCES home_administrative_area (id),
    CONSTRAINT fk_outside_public_area_building_zone FOREIGN KEY (zone) REFERENCES building_zone (id),
    CONSTRAINT uq_outside_public_area_home_administrative_area_name UNIQUE (home_administrative_area_id, name)
);

CREATE INDEX idx_outside_public_area_home_administrative_area_id ON outside_public_area (home_administrative_area_id);

--rollback DROP TABLE outside_public_area CASCADE;
