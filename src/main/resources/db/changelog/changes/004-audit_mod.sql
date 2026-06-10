--liquibase formatted sql

--changeset audit:06-add-modified-flags-to-administration-unit-aud

ALTER TABLE administration_unit_AUD
    ADD COLUMN type_MOD BOOLEAN;

ALTER TABLE administration_unit_AUD
    ADD COLUMN unit_name_MOD BOOLEAN;

ALTER TABLE administration_unit_AUD
    ADD COLUMN parent_MOD BOOLEAN;

ALTER TABLE administration_unit_AUD
    ADD COLUMN attributes_mod BOOLEAN;

--changeset audit:07-add-modified-flags-to-administration-unit-attribute-aud

ALTER TABLE administration_unit_attribute_AUD
    ADD COLUMN key_MOD BOOLEAN;

ALTER TABLE administration_unit_attribute_AUD
    ADD COLUMN value_MOD BOOLEAN;

ALTER TABLE administration_unit_attribute_AUD
ADD COLUMN unit_MOD BOOLEAN;