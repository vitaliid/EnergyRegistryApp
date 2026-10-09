package org.example.entity;

import lombok.Getter;

import java.util.Set;

/**
 * Role of a user within a scope. Mirrors {@code org.example.model.Role} by name, but is owned
 * by the persistence layer - the API model is generated from the specification and must not dictate
 * the database contract.
 */
@Getter
public enum Role {

    ESR_EDITOR("ESR_EDITOR"),
    ESR_APPROVER("ESR_APPROVER"),
    ESR_VIEWER("ESR_VIEWER"),
    GIR_EDITOR("GIR_EDITOR"),
    GIR_APPROVER("GIR_APPROVER"),
    GIR_VIEWER("GIR_VIEWER"),
    ADMIN("ADMIN");

    public static final Set<Role> ESR_ROLES = Set.of(ESR_EDITOR, ESR_APPROVER, ESR_VIEWER);

    private final String dbValue;

    Role(String dbValue) {
        this.dbValue = dbValue;
    }
}
