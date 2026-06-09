--liquibase formatted sql

--changeset audit:003-audit

CREATE TABLE revinfo (
                         id SERIAL PRIMARY KEY,
                         timestamp BIGINT NOT NULL
);

CREATE INDEX idx_revinfo_timestamp
    ON revinfo(timestamp);


--changeset audit:004-create-administration-unit-aud

CREATE TABLE administration_unit_AUD (
                                         id INTEGER NOT NULL,

                                         REV INTEGER NOT NULL,
                                         REVTYPE SMALLINT,

                                         type VARCHAR(50),
                                         name VARCHAR(255),
                                         parent_unit_id INTEGER,

                                         PRIMARY KEY (id, REV),

                                         CONSTRAINT fk_unit_aud_rev
                                             FOREIGN KEY (REV)
                                                 REFERENCES revinfo(id)
);


--changeset audit:005-create-administration-unit-attribute-aud

CREATE TABLE administration_unit_attribute_AUD (
                                                   id BIGINT NOT NULL,

                                                   REV INTEGER NOT NULL,
                                                   REVTYPE SMALLINT,

                                                   unit_id INTEGER,
                                                   attr_key VARCHAR(100),
                                                   attr_value VARCHAR(255),

                                                   PRIMARY KEY (id, REV),

                                                   CONSTRAINT fk_attr_aud_rev
                                                       FOREIGN KEY (REV)
                                                           REFERENCES revinfo(id)
);