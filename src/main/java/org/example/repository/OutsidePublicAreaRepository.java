package org.example.repository;

import org.example.entity.OutsidePublicAreaEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OutsidePublicAreaRepository extends JpaRepository<OutsidePublicAreaEntity, UUID> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"homeAdministrativeArea"})
    Optional<OutsidePublicAreaEntity> findById(@NonNull UUID id);

    List<OutsidePublicAreaEntity> findAllByHomeAdministrativeAreaId(@NonNull UUID homeAdministrativeAreaId);

    @EntityGraph(attributePaths = {"homeAdministrativeArea"})
    List<OutsidePublicAreaEntity> findAllByOrderByNameAscHomeAdministrativeAreaIdAsc();

    @EntityGraph(attributePaths = {"homeAdministrativeArea"})
    List<OutsidePublicAreaEntity> findAllByIdInOrderByNameAscHomeAdministrativeAreaIdAsc(Collection<UUID> ids);

    /**
     * Ids of every outside public area the user holds any role on directly, plus every outside public area
     * belonging to a unit the user holds any role on or to a unit below such a unit, at any depth.
     */
    @Query(value = """
            WITH RECURSIVE scope(id) AS (
                SELECT ur.home_administrative_area_id
                FROM user_role ur
                WHERE ur.user_id = :userId
                  AND ur.home_administrative_area_id IS NOT NULL

                UNION

                SELECT child.id
                FROM home_administrative_area child
                         JOIN scope ON child.parent_id = scope.id
            )
            SELECT pe.id
            FROM outside_public_area pe
            WHERE pe.home_administrative_area_id IN (SELECT scope.id FROM scope)
               OR pe.id IN (SELECT ur.outside_public_area_id
                            FROM user_role ur
                            WHERE ur.user_id = :userId
                              AND ur.outside_public_area_id IS NOT NULL)
            """, nativeQuery = true)
    List<UUID> findAllIdsVisibleToUser(@Param("userId") UUID userId);

    /**
     * Whether the user holds any role on the given outside public area directly, or on its unit or
     * on one of that unit's ancestors.
     */
    @Query(value = """
            WITH RECURSIVE ancestors(id, parent_id) AS (
                SELECT au.id, au.parent_id
                FROM home_administrative_area au
                         JOIN outside_public_area pe ON pe.home_administrative_area_id = au.id
                WHERE pe.id = :outsidePublicAreaId

                UNION

                SELECT parent.id, parent.parent_id
                FROM home_administrative_area parent
                         JOIN ancestors ON parent.id = ancestors.parent_id
            )
            SELECT EXISTS (
                SELECT 1
                FROM user_role ur
                WHERE ur.user_id = :userId
                  AND (ur.outside_public_area_id = :outsidePublicAreaId
                       OR ur.home_administrative_area_id IN (SELECT ancestors.id FROM ancestors))
            )
            """, nativeQuery = true)
    boolean isVisibleToUser(@Param("outsidePublicAreaId") UUID outsidePublicAreaId,
                            @Param("userId") UUID userId);

    /**
     * Whether the user holds at least one of the given roles on the given outside public area directly,
     * or on its unit or on one of that unit's ancestors.
     */
    @Query(value = """
            WITH RECURSIVE ancestors(id, parent_id) AS (
                SELECT au.id, au.parent_id
                FROM home_administrative_area au
                         JOIN outside_public_area pe ON pe.home_administrative_area_id = au.id
                WHERE pe.id = :outsidePublicAreaId

                UNION

                SELECT parent.id, parent.parent_id
                FROM home_administrative_area parent
                         JOIN ancestors ON parent.id = ancestors.parent_id
            )
            SELECT EXISTS (
                SELECT 1
                FROM user_role ur
                WHERE ur.user_id = :userId
                  AND ur.role IN (:roles)
                  AND (ur.outside_public_area_id = :outsidePublicAreaId
                       OR ur.home_administrative_area_id IN (SELECT ancestors.id FROM ancestors))
            )
            """, nativeQuery = true)
    boolean hasAnyRoleOnOutsidePublicArea(@Param("outsidePublicAreaId") UUID outsidePublicAreaId,
                                     @Param("userId") UUID userId,
                                     @Param("roles") Collection<String> roles);

    @Query("""
            SELECT pe.id
            FROM OutsidePublicAreaEntity pe
            WHERE pe.homeAdministrativeArea.id IN :homeAdministrativeAreaIds
            """)
    List<UUID> findAllIdsByHomeAdministrativeAreaIdIn(
            @Param("homeAdministrativeAreaIds") Collection<UUID> homeAdministrativeAreaIds);
}
