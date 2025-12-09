# 📋 TaskList Feature Documentation

## Overview

The **TaskList** feature allows users to organize their tasks into categorized lists. Each user can create multiple task lists (e.g., "Work", "Estudos", "Pessoal") and manage tasks within them.

## Key Features

✅ **Multiple Task Lists** - Create unlimited categorized task lists  
✅ **CRUD Operations** - Full Create, Read, Update, Delete for lists  
✅ **Task Organization** - Move tasks between lists  
✅ **Name Validation** - Unique list names per user  
✅ **Descriptions** - Optional descriptions for each list  
✅ **Timestamps** - Auto-tracked creation and update times  
✅ **Cascade Delete** - Deleting a list removes all its tasks  

---

## Domain Model

### TaskList Entity
```java
@Entity
@Table(name = "task_lists")
public class TaskList {
    Long id;              // Primary key
    String name;          // List name (unique per user)
    String description;   // Optional description
    User user;            // Owner (ManyToOne)
    List<Task> tasks;     // Tasks in this list (OneToMany)
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
```

### Relationships
```
User (1) ──────-> (N) TaskList
  ↓
  └─> (1) ──────-> (N) Task (optional association)
```

**Important**: Tasks have an **optional** relationship to TaskList. A task can exist without being in any list (uncategorized).

---

## REST API Endpoints

### Create a Task List
```http
POST /api/tasklists
Content-Type: application/json

{
  "name": "Work",
  "description": "Work-related tasks"
}

Response: 201 Created
{
  "id": 1,
  "name": "Work",
  "description": "Work-related tasks",
  "taskCount": 0,
  "createdAt": "2025-12-03T19:40:00",
  "updatedAt": "2025-12-03T19:40:00"
}
```

### Get All Task Lists
```http
GET /api/tasklists
Authorization: Bearer <token>

Response: 200 OK
[
  {
    "id": 1,
    "name": "Work",
    "description": "Work-related tasks",
    "taskCount": 5,
    "createdAt": "2025-12-03T19:40:00",
    "updatedAt": "2025-12-03T19:40:00"
  },
  {
    "id": 2,
    "name": "Estudos",
    "description": "Study tasks",
    "taskCount": 3,
    "createdAt": "2025-12-03T19:41:00",
    "updatedAt": "2025-12-03T19:41:00"
  }
]
```

### Get Specific Task List
```http
GET /api/tasklists/{id}
Authorization: Bearer <token>

Response: 200 OK
{
  "id": 1,
  "name": "Work",
  "description": "Work-related tasks",
  "taskCount": 5,
  "createdAt": "2025-12-03T19:40:00",
  "updatedAt": "2025-12-03T19:40:00"
}
```

### Update Task List (Rename)
```http
PUT /api/tasklists/{id}
Content-Type: application/json
Authorization: Bearer <token>

{
  "name": "Work Projects",
  "description": "Updated description"
}

Response: 200 OK
{
  "id": 1,
  "name": "Work Projects",
  "description": "Updated description",
  "taskCount": 5,
  "createdAt": "2025-12-03T19:40:00",
  "updatedAt": "2025-12-03T19:42:00"
}
```

### Delete Task List
```http
DELETE /api/tasklists/{id}
Authorization: Bearer <token>

Response: 204 No Content
```

**⚠️ Important**: Deleting a task list **cascade deletes all tasks** in that list.

---

## Task Management within Lists

### Move Task to Task List
```http
PUT /api/tasklists/{listId}/tasks/{taskId}
Authorization: Bearer <token>

Response: 204 No Content
```

### Remove Task from List (Make Uncategorized)
```http
DELETE /api/tasklists/{listId}/tasks/{taskId}
Authorization: Bearer <token>

Response: 204 No Content
```

---

## Data Transfer Objects (DTOs)

### CreateTaskListRequest
```java
{
  "name": "Work",              // Required, 1-100 chars
  "description": "Optional"    // Optional, max 500 chars
}
```

### UpdateTaskListRequest
```java
{
  "name": "Work Updated",      // Required, 1-100 chars
  "description": "New desc"    // Optional, max 500 chars
}
```

### TaskListResponse
```java
{
  "id": 1,
  "name": "Work",
  "description": "Work-related tasks",
  "taskCount": 5,              // Count of tasks in list
  "createdAt": "ISO 8601",
  "updatedAt": "ISO 8601"
}
```

---

## Validation Rules

| Field | Rules | Error |
|-------|-------|-------|
| **name** | Required, 1-100 chars, unique per user | "Task list name is required" / "already exists" |
| **description** | Optional, max 500 chars | "must not exceed 500 characters" |

---

## Service Layer (TaskListService)

### Key Methods

```java
// Create a new task list
TaskListResponse createTaskList(CreateTaskListRequest request)

// Get all lists for authenticated user
List<TaskListResponse> getUserTaskLists()

// Get specific list with ownership validation
TaskListResponse getTaskListById(Long id)

// Update list (rename with optional description)
TaskListResponse updateTaskList(Long id, UpdateTaskListRequest request)

// Delete list and cascade delete tasks
void deleteTaskList(Long id)

// Move task to this list
void moveTaskToList(Long taskId, Long listId)

// Remove task from list (set to null)
void removeTaskFromList(Long taskId)
```

---

## Examples with cURL

### Create a new task list
```bash
curl -X POST http://localhost:8080/api/tasklists \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Work",
    "description": "Work-related tasks"
  }'
```

### Get all task lists
```bash
curl -X GET http://localhost:8080/api/tasklists \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Update task list name
```bash
curl -X PUT http://localhost:8080/api/tasklists/1 \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Work Projects",
    "description": "Updated description"
  }'
```

### Move task to list
```bash
curl -X PUT http://localhost:8080/api/tasklists/1/tasks/5 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Delete task list
```bash
curl -X DELETE http://localhost:8080/api/tasklists/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## Security & Authorization

✅ All TaskList endpoints require **JWT authentication**  
✅ Users can only see/modify their own task lists  
✅ Unauthorized access returns **403 Forbidden**  
✅ Non-existent lists return **404 Not Found**  

---

## Database Schema

### task_lists table
```sql
CREATE TABLE task_lists (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    UNIQUE KEY unique_name_per_user (name, user_id)
);
```

### tasks table (updated)
```sql
ALTER TABLE tasks ADD COLUMN task_list_id BIGINT;
ALTER TABLE tasks ADD FOREIGN KEY (task_list_id) REFERENCES task_lists(id);
```

---

## Test Coverage

### TaskListServiceTest (8 tests)
✅ Create task list - success  
✅ Create task list - name already exists  
✅ Get user task lists - success  
✅ Get task list by ID - success  
✅ Get task list by ID - not found  
✅ Update task list - success  
✅ Delete task list - success  
✅ Delete task list - not found  

### TaskListControllerTest (7 tests)
✅ POST /api/tasklists - create  
✅ GET /api/tasklists - list all  
✅ GET /api/tasklists/{id} - get by ID  
✅ PUT /api/tasklists/{id} - update  
✅ DELETE /api/tasklists/{id} - delete  
✅ PUT /api/tasklists/{listId}/tasks/{taskId} - move task  
✅ DELETE /api/tasklists/{listId}/tasks/{taskId} - remove task  

---

## Common Use Cases

### Use Case 1: Organize Tasks by Category
```bash
# 1. Create work list
POST /api/tasklists { "name": "Work", "description": "..." }

# 2. Create study list
POST /api/tasklists { "name": "Estudos", "description": "..." }

# 3. Get all lists
GET /api/tasklists

# 4. Create task in work list
POST /api/tasks { "title": "...", "description": "..." }
PUT /api/tasklists/1/tasks/{newTaskId}
```

### Use Case 2: Reorganize Tasks
```bash
# Move task from Work to Personal
PUT /api/tasklists/3/tasks/5
```

### Use Case 3: Archive by Renaming
```bash
# Rename list to indicate it's archived
PUT /api/tasklists/1 { "name": "Work - Archive 2024" }
```

---

## Future Enhancements

💡 **Possible improvements** for the TaskList feature:

1. **List Sharing** - Share lists with other users
2. **List Colors** - Add color coding to lists
3. **List Ordering** - User-defined list order
4. **List Templates** - Pre-made list templates
5. **Bulk Operations** - Move multiple tasks at once
6. **List Archiving** - Soft delete for archived lists
7. **List Statistics** - Completed/total task stats per list
8. **Task Filtering** - Filter tasks by list, status, due date

---

## Related Documentation

- [Main API Documentation](IMPLEMENTATION.md)
- [Task Management](USAGE_GUIDE.md#tarefas)
- [Authentication](USAGE_GUIDE.md#autenticação)
- [Testing Guide](README_BACKEND.md#executar-testes)

---

## Support

For issues or questions:
1. Check [TROUBLESHOOTING.md](TROUBLESHOOTING.md)
2. Review test files for usage examples
3. Check error messages for validation details

---

**Status**: ✅ Complete & Tested  
**Test Results**: 15 tests, 100% passing  
**Lines of Code**: ~600 lines (entities, services, controllers, tests)  
**Documentation**: Complete with examples

