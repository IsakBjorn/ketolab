package no.ibear.ketolab.model

enum class Role {
    ADMIN,
    ECONOMY,
    HR
}

data class AssignRoleRequest(
    val userId: String,
    val role: Role
)

data class CheckRoleRequest(
    val userId: String,
    val role: Role
)

data class RoleCheckResult(
    val hasRole: Boolean,
    val userId: String,
    val role: Role
)

data class UserRoles(
    val userId: String,
    val roles: List<Role>
)
