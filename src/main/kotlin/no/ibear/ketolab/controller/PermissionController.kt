package no.ibear.ketolab.controller

import no.ibear.ketolab.model.*
import no.ibear.ketolab.service.PermissionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/permissions")
class PermissionController(
    private val permissionService: PermissionService
) {

    @PostMapping("/assign")
    fun assignRole(@RequestBody request: AssignRoleRequest): ResponseEntity<Map<String, String>> {
        permissionService.assignUserToRole(request.userId, request.role)
        return ResponseEntity.ok(mapOf(
            "status" to "assigned",
            "userId" to request.userId,
            "role" to request.role.name
        ))
    }

    @GetMapping("/check")
    fun checkRole(
        @RequestParam userId: String,
        @RequestParam role: Role
    ): ResponseEntity<RoleCheckResult> {
        val hasRole = permissionService.checkRole(userId, role)
        return ResponseEntity.ok(RoleCheckResult(hasRole, userId, role))
    }

    @GetMapping("/user/{userId}")
    fun getUserRoles(@PathVariable userId: String): ResponseEntity<UserRoles> {
        val roles = permissionService.listUserRoles(userId)
        return ResponseEntity.ok(roles)
    }
}
