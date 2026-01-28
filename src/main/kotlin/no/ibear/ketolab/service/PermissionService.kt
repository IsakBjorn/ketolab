package no.ibear.ketolab.service

import no.ibear.ketolab.model.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import sh.ory.keto.api.PermissionApi
import sh.ory.keto.api.RelationshipApi
import sh.ory.keto.model.CreateRelationshipBody

@Service
class PermissionService(
    private val permissionApi: PermissionApi,
    @Qualifier("relationshipReadApi") private val relationshipReadApi: RelationshipApi,
    @Qualifier("relationshipWriteApi") private val relationshipWriteApi: RelationshipApi
) {
    private val logger = LoggerFactory.getLogger(PermissionService::class.java)

    fun assignUserToRole(userId: String, companyId: String, role: Role) {
        val relation = role.name.lowercase()
        logger.info("Assigning user $userId to role $relation for company $companyId")
        createRelationship("Company", companyId, relation, userId)
    }

    fun checkRole(userId: String, companyId: String, role: Role): Boolean {
        val permission = "is${role.name.lowercase().replaceFirstChar { it.uppercase() }}"
        logger.info("Checking role: user=$userId, company=$companyId, permission=$permission")

        return try {
            val result = permissionApi.checkPermission(
                "Company",
                companyId,
                permission,
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

    fun checkTask(userId: String, companyId: String, task: Task): Boolean {
        val permission = "can${task.name.split("_").joinToString("") {
            it.lowercase().replaceFirstChar { c -> c.uppercase() }
        }}"
        logger.info("Checking task: user=$userId, company=$companyId, permission=$permission")

        return try {
            val result = permissionApi.checkPermission(
                "Company",
                companyId,
                permission,
                userId,
                null,
                null,
                null,
                null
            )
            result.allowed
        } catch (e: Exception) {
            logger.error("Error checking task", e)
            false
        }
    }

    fun listUserRoles(userId: String): UserRoles {
        val companyRolesMap = mutableMapOf<String, MutableList<Role>>()

        try {
            logger.info("Fetching relationships for user $userId")
            val relationships = relationshipReadApi.getRelationships(
                null,       // pageSize
                null,       // pageToken
                "Company",  // namespace
                null,       // object
                null,       // relation
                userId,     // subjectId
                null,       // subjectSetNamespace
                null,       // subjectSetObject
                null        // subjectSetRelation
            )

            val tuples = relationships.relationTuples ?: emptyList()
            logger.info("Found ${tuples.size} relationships for user $userId")

            for (tuple in tuples) {
                val companyId = tuple.`object`
                logger.info("Processing tuple: company=$companyId, relation=${tuple.relation}")
                companyRolesMap.getOrPut(companyId) { mutableListOf() }
            }

            // For each company the user has any relationship with, check all roles
            for (companyId in companyRolesMap.keys) {
                for (role in Role.entries) {
                    if (checkRole(userId, companyId, role)) {
                        if (role !in companyRolesMap[companyId]!!) {
                            companyRolesMap[companyId]!!.add(role)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("Error listing user roles", e)
        }

        val companies = companyRolesMap.map { (companyId, roles) ->
            CompanyRoles(companyId, roles)
        }

        return UserRoles(userId, companies)
    }

    fun removeRoleFromCompany(userId: String, companyId: String, role: Role) {
        val relation = role.name.lowercase()
        logger.info("Removing role $relation from user $userId for company $companyId")
        deleteRelationship("Company", companyId, relation, userId)
    }

    fun removeAllRolesFromCompany(userId: String, companyId: String) {
        logger.info("Removing all roles from user $userId for company $companyId")
        for (role in Role.entries) {
            deleteRelationship("Company", companyId, role.name.lowercase(), userId)
        }
    }

    fun removeAllRoles(userId: String) {
        logger.info("Removing all roles from user $userId")
        try {
            val relationships = relationshipReadApi.getRelationships(
                null,       // pageSize
                null,       // pageToken
                "Company",  // namespace
                null,       // object
                null,       // relation
                userId,     // subjectId
                null,       // subjectSetNamespace
                null,       // subjectSetObject
                null        // subjectSetRelation
            )

            for (tuple in relationships.relationTuples ?: emptyList()) {
                deleteRelationship("Company", tuple.`object`, tuple.relation, userId)
            }
        } catch (e: Exception) {
            logger.error("Error removing all roles for user $userId", e)
        }
    }

    private fun createRelationship(namespace: String, objectId: String, relation: String, subjectId: String) {
        val body = CreateRelationshipBody().apply {
            this.namespace = namespace
            this.`object` = objectId
            this.relation = relation
            this.subjectId = subjectId
        }

        try {
            relationshipWriteApi.createRelationship(body)
            logger.debug("Created relationship: $namespace:$objectId#$relation@$subjectId")
        } catch (e: Exception) {
            logger.warn("Failed to create relationship (may already exist): $namespace:$objectId#$relation@$subjectId")
        }
    }

    private fun deleteRelationship(namespace: String, objectId: String, relation: String, subjectId: String) {
        try {
            relationshipWriteApi.deleteRelationships(
                namespace,  // namespace
                objectId,   // object
                relation,   // relation
                subjectId,  // subjectId
                null,       // subjectSetNamespace
                null,       // subjectSetObject
                null        // subjectSetRelation
            )
            logger.debug("Deleted relationship: $namespace:$objectId#$relation@$subjectId")
        } catch (e: Exception) {
            logger.warn("Failed to delete relationship: $namespace:$objectId#$relation@$subjectId", e)
        }
    }
}
