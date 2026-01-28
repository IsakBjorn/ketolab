# Ketolab - Ory Keto POC

A Spring Boot application demonstrating RBAC (Role-Based Access Control) using [Ory Keto](https://www.ory.sh/keto/) as the permission server.

## Permission Model

### Roles
- **ADMIN** - Inherits all permissions from other roles
- **REPORTING** - Can view analytics reports
- **ECONOMY** - Can view and edit financial reports
- **HR** - Can view and edit HR reports

### Resources
- `ANALYTICS_REPORT` - Analytics reports
- `FINANCIAL_REPORT` - Financial reports
- `HR_REPORT` - HR reports

### Actions
- `VIEW` - Read access
- `EDIT` - Write access

## Prerequisites

- Java 25
- Docker and Docker Compose
- Maven (or use the included wrapper)

## Getting Started

### 1. Start Ory Keto

```bash
docker compose up -d
```

This starts Keto with:
- Read API on port 4466
- Write API on port 4467

### 2. Start the Application

```bash
./mvnw spring-boot:run
```

The application runs on `http://localhost:8080`.

### 3. Initialize Roles

Before using the permission system, initialize the role hierarchy:

```bash
curl -X POST http://localhost:8080/api/permissions/init
```

## API Endpoints

### Initialize Role Hierarchy

```
POST /api/permissions/init
```

Creates the role hierarchy and permission assignments in Keto.

**Response:**
```json
{"status": "initialized"}
```

### Assign User to Role

```
POST /api/permissions/assign
Content-Type: application/json

{"userId": "alice", "role": "REPORTING"}
```

**Roles:** `ADMIN`, `REPORTING`, `ECONOMY`, `HR`

**Response:**
```json
{"status": "assigned", "userId": "alice", "role": "REPORTING"}
```

### Check Permission

```
GET /api/permissions/check?userId={userId}&action={action}&resource={resource}
```

**Parameters:**
- `userId` - The user identifier
- `action` - `VIEW` or `EDIT`
- `resource` - `FINANCIAL_REPORT`, `HR_REPORT`, or `ANALYTICS_REPORT`

**Response:**
```json
{"allowed": true, "userId": "alice", "action": "VIEW", "resource": "ANALYTICS_REPORT"}
```

### Get User Permissions

```
GET /api/permissions/user/{userId}
```

Returns all roles and effective permissions for a user.

**Response:**
```json
{
  "userId": "alice",
  "roles": ["REPORTING"],
  "permissions": [
    {"action": "VIEW", "resource": "ANALYTICS_REPORT"}
  ]
}
```

## Testing with curl

```bash
# 1. Initialize the role hierarchy
curl -X POST http://localhost:8080/api/permissions/init

# 2. Assign alice to the REPORTING role
curl -X POST http://localhost:8080/api/permissions/assign \
  -H "Content-Type: application/json" \
  -d '{"userId":"alice","role":"REPORTING"}'

# 3. Check if alice can view analytics reports (should be true)
curl "http://localhost:8080/api/permissions/check?userId=alice&action=VIEW&resource=ANALYTICS_REPORT"

# 4. Check if alice can edit financial reports (should be false)
curl "http://localhost:8080/api/permissions/check?userId=alice&action=EDIT&resource=FINANCIAL_REPORT"

# 5. Assign bob to the ADMIN role
curl -X POST http://localhost:8080/api/permissions/assign \
  -H "Content-Type: application/json" \
  -d '{"userId":"bob","role":"ADMIN"}'

# 6. Check bob's permissions (admin has all permissions)
curl http://localhost:8080/api/permissions/user/bob

# 7. Get alice's effective permissions
curl http://localhost:8080/api/permissions/user/alice
```

## Project Structure

```
src/main/kotlin/no/ibear/ketolab/
├── KetolabApplication.kt          # Application entry point
├── config/
│   └── KetoConfig.kt              # Keto client configuration
├── controller/
│   └── PermissionController.kt    # REST endpoints
├── model/
│   └── Permission.kt              # Enums and DTOs
└── service/
    └── PermissionService.kt       # Permission logic
```

## Build Commands

```bash
./mvnw compile              # Compile the project
./mvnw test                 # Run all tests
./mvnw spring-boot:run      # Run the application
./mvnw clean package        # Build JAR artifact
```
