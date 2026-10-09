package org.example.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Stores a {@link Role} by its explicit {@code dbValue} rather than by {@code name()}.
 */
@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, String> {

    private static final Map<String, Role> BY_DB_VALUE = Arrays.stream(Role.values())
            .collect(Collectors.toUnmodifiableMap(Role::getDbValue, Function.identity()));

    @Override
    public @Nullable String convertToDatabaseColumn(@Nullable Role role) {
        return role == null ? null : role.getDbValue();
    }

    @Override
    public @Nullable Role convertToEntityAttribute(@Nullable String dbValue) {
        if (dbValue == null) {
            return null;
        }

        Role role = BY_DB_VALUE.get(dbValue);
        if (role == null) {
            // ck_user_role_role makes this unreachable
            throw new IllegalStateException("Unknown role in the database: " + dbValue);
        }
        return role;
    }
}
