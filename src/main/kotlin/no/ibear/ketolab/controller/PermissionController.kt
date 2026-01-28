package no.ibear.ketolab.controller

import no.ibear.ketolab.model.*
import no.ibear.ketolab.model.Task
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
        permissionService.assignUserToRole(request.userId, request.companyId, request.role)
        return ResponseEntity.ok(mapOf(
            "status" to "assigned",
            "userId" to request.userId,
            "companyId" to request.companyId,
            "role" to request.role.name
        ))
    }

    @GetMapping("/check")
    fun checkRole(
        @RequestParam userId: String,
        @RequestParam companyId: String,
        @RequestParam role: Role
    ): ResponseEntity<RoleCheckResult> {
        val hasRole = permissionService.checkRole(userId, companyId, role)
        return ResponseEntity.ok(RoleCheckResult(hasRole, userId, companyId, role))
    }

    @GetMapping("/check-task")
    fun checkTask(
        @RequestParam userId: String,
        @RequestParam companyId: String,
        @RequestParam task: Task
    ): ResponseEntity<TaskCheckResult> {
        val allowed = permissionService.checkTask(userId, companyId, task)
        return ResponseEntity.ok(TaskCheckResult(allowed, userId, companyId, task))
    }

    @GetMapping("/user/{userId}")
    fun getUserRoles(@PathVariable userId: String): ResponseEntity<UserRoles> {
        val roles = permissionService.listUserRoles(userId)
        return ResponseEntity.ok(roles)
    }

    @DeleteMapping("/user/{userId}/company/{companyId}/role/{role}")
    fun removeRole(
        @PathVariable userId: String,
        @PathVariable companyId: String,
        @PathVariable role: Role
    ): ResponseEntity<Map<String, String>> {
        permissionService.removeRoleFromCompany(userId, companyId, role)
        return ResponseEntity.ok(mapOf(
            "status" to "removed",
            "userId" to userId,
            "companyId" to companyId,
            "role" to role.name
        ))
    }

    @DeleteMapping("/user/{userId}/company/{companyId}")
    fun removeAllRolesFromCompany(
        @PathVariable userId: String,
        @PathVariable companyId: String
    ): ResponseEntity<Map<String, String>> {
        permissionService.removeAllRolesFromCompany(userId, companyId)
        return ResponseEntity.ok(mapOf(
            "status" to "removed",
            "userId" to userId,
            "companyId" to companyId
        ))
    }

    @DeleteMapping("/user/{userId}")
    fun removeAllRoles(@PathVariable userId: String): ResponseEntity<Map<String, String>> {
        permissionService.removeAllRoles(userId)
        return ResponseEntity.ok(mapOf(
            "status" to "removed",
            "userId" to userId
        ))
    }
}
