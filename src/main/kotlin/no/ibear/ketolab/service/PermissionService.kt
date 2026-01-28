package no.ibear.ketolab.service

import no.ibear.ketolab.model.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import sh.ory.keto.api.PermissionApi
import sh.ory.keto.api.RelationshipApi
import sh.ory.keto.model.CreateRelationshipBody

@Service
class PermissionService(
    private val permissionApi: PermissionApi,
    private val relationshipApi: RelationshipApi
) {
    private val logger = LoggerFactory.getLogger(PermissionService::class.java)

    fun assignUserToRole(userId: String, role: Role) {
        val roleObject = role.name.lowercase()
        logger.info("Assigning user $userId to role $roleObject")
        createRelationship("Role", roleObject, "member", userId)
    }

    fun checkRole(userId: String, role: Role): Boolean {
        val roleObject = role.name.lowercase()
        logger.info("Checking role: user=$userId, role=$roleObject")

        return try {
            val result = permissionApi.checkPermission(
                "Role",
                roleObject,
                "hasRole",
                userId,
                null,
                null,
                null,
                null
            )
            result.allowed
        } catch (e: Exception) {
            logger.error("Error checking role", e)
            false
        }
    }

    fun listUserRoles(userId: String): UserRoles {
        val roles = mutableListOf<Role>()

        for (role in Role.entries) {
            if (checkRole(userId, role)) {
                roles.add(role)
            }
        }

        return UserRoles(userId, roles)
    }

    private fun createRelationship(namespace: String, objectId: String, relation: String, subjectId: String) {
        val body = CreateRelationshipBody().apply {
            this.namespace = namespace
            this.`object` = objectId
            this.relation = relation
            this.subjectId = subjectId
        }

        try {
            relationshipApi.createRelationship(body)
            logger.debug("Created relationship: $namespace:$objectId#$relation@$subjectId")
        } catch (e: Exception) {
            logger.warn("Failed to create relationship (may already exist): $namespace:$objectId#$relation@$subjectId")
        }
    }
}
