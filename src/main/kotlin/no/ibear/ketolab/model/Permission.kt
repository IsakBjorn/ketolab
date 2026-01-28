package no.ibear.ketolab.model

enum class Role {
    ADMIN,
    ECONOMY,
    HR
}

data class AssignRoleRequest(
    val userId: String,
    val companyId: String,
    val role: Role
)

data class RoleCheckResult(
    val hasRole: Boolean,
    val userId: String,
    val companyId: String,
    val role: Role
)

data class CompanyRoles(
    val companyId: String,
    val roles: List<Role>
)

data class UserRoles(
    val userId: String,
    val companies: List<CompanyRoles>
)
