# 🔗 Task-TaskList Relationship Documentation

## Overview

Tasks can now be associated with TaskLists. This document explains how to manage this relationship through the API.

## DTOs Updated

### CreateTaskRequest (Enhanced)
```json
{
  "title": "Implement API",
  "description": "Create REST endpoints for tasks",
  "taskListId": 1  // Optional: ID of the task list to associate with
}
```

**Fields:**
- `title` (required): Task title (1-255 chars)
- `description` (optional): Task description (max 2000 chars)
- `taskListId` (optional): ID of the task list to organize the task into

### UpdateTaskRequest (Enhanced)
```json
{
  "title": "Updated task",
  "description": "Updated description",
  "completed": false,
  "taskListId": 2  // Optional: move task to a different list
}
```

**Fields:**
- `title` (required): Task title (1-255 chars)
- `description` (optional): Task description (max 2000 chars)
- `completed` (optional): Boolean flag for completion status
- `taskListId` (optional): Move task to a different task list

### TaskResponse (Enhanced)
```json
{
  "id": 5,
  "title": "Implement API",
  "description": "Create REST endpoints for tasks",
  "completed": false,
  "createdAt": "2025-12-03T19:40:00",
  "updatedAt": "2025-12-03T19:41:00",
  "taskListId": 1,
  "taskListName": "Work"
}
```

**Fields:**
- `id`: Task ID
- `title`: Task title
- `description`: Task description
- `completed`: Completion status
- `createdAt`: ISO 8601 timestamp
- `updatedAt`: ISO 8601 timestamp
- `taskListId`: Associated task list ID (null if uncategorized)
- `taskListName`: Associated task list name for convenience (null if uncategorized)

---

## API Usage Examples

### Create Task Without TaskList (Uncategorized)
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Buy groceries",
    "description": "Milk, eggs, bread"
  }'

# Response: 201 Created
{
  "id": 10,
  "title": "Buy groceries",
  "description": "Milk, eggs, bread",
  "completed": false,
  "createdAt": "2025-12-03T19:55:00",
  "updatedAt": "2025-12-03T19:55:00",
  "taskListId": null,
  "taskListName": null
}
```

### Create Task with TaskList Association
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Design database schema",
    "description": "Normalize and optimize",
    "taskListId": 1
  }'

# Response: 201 Created
{
  "id": 11,
  "title": "Design database schema",
  "description": "Normalize and optimize",
  "completed": false,
  "createdAt": "2025-12-03T19:55:10",
  "updatedAt": "2025-12-03T19:55:10",
  "taskListId": 1,
  "taskListName": "Work"
}
```

### Move Task to Different List
```bash
curl -X PUT http://localhost:8080/api/tasks/11 \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Design database schema",
    "description": "Normalize and optimize",
    "taskListId": 2
  }'

# Response: 200 OK
{
  "id": 11,
  "title": "Design database schema",
  "description": "Normalize and optimize",
  "completed": false,
  "createdAt": "2025-12-03T19:55:10",
  "updatedAt": "2025-12-03T19:55:15",
  "taskListId": 2,
  "taskListName": "Estudos"
}
```

### Remove Task from List (Make Uncategorized)
```bash
curl -X PUT http://localhost:8080/api/tasks/11 \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Design database schema",
    "description": "Normalize and optimize",
    "taskListId": null
  }'

# Response: 200 OK
{
  "id": 11,
  "title": "Design database schema",
  "description": "Normalize and optimize",
  "completed": false,
  "createdAt": "2025-12-03T19:55:10",
  "updatedAt": "2025-12-03T19:55:20",
  "taskListId": null,
  "taskListName": null
}
```

### Update Task (Keep Existing List)
```bash
curl -X PUT http://localhost:8080/api/tasks/11 \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Design database schema (Optimized)",
    "completed": true
  }'

# Response: 200 OK - taskListId remains unchanged
{
  "id": 11,
  "title": "Design database schema (Optimized)",
  "description": "Normalize and optimize",
  "completed": true,
  "createdAt": "2025-12-03T19:55:10",
  "updatedAt": "2025-12-03T19:55:25",
  "taskListId": 2,
  "taskListName": "Estudos"
}
```

### Get Task with List Details
```bash
curl -X GET http://localhost:8080/api/tasks/11 \
  -H "Authorization: Bearer YOUR_TOKEN"

# Response: 200 OK
{
  "id": 11,
  "title": "Design database schema (Optimized)",
  "description": "Normalize and optimize",
  "completed": true,
  "createdAt": "2025-12-03T19:55:10",
  "updatedAt": "2025-12-03T19:55:25",
  "taskListId": 2,
  "taskListName": "Estudos"
}
```

---

## Relationships Overview

```
User
  ├─> TaskList (1 to N)
  │   ├─ id: 1, name: "Work"
  │   └─ id: 2, name: "Estudos"
  │
  └─> Task (1 to N, optional TaskList association)
      ├─ id: 10, title: "Buy groceries", taskListId: null (uncategorized)
      ├─ id: 11, title: "Design schema", taskListId: 2 (associated with "Estudos")
      └─ id: 12, title: "Fix bug", taskListId: 1 (associated with "Work")
```

---

## Service Layer Behavior

### CreateTask Method
- If `taskListId` is provided, validates that the list exists and belongs to the user
- Associates the task with the provided task list
- If `taskListId` is null/missing, creates uncategorized task
- Throws `ResourceNotFoundException` if task list doesn't exist

### UpdateTask Method
- If `taskListId` is provided, moves task to that list
- Validates task list ownership
- If `taskListId` is not provided, preserves existing association
- Throws `ResourceNotFoundException` if task list doesn't exist

---

## Validation & Error Handling

| Scenario | HTTP Status | Error |
|----------|-------------|-------|
| Valid task creation without list | 201 | None |
| Valid task creation with list | 201 | None |
| Update task with valid list ID | 200 | None |
| Task list doesn't exist | 404 | "Task list not found with id: X" |
| Task list belongs to different user | 404 | "Task list not found with id: X" |
| Task not found | 404 | "Task not found with id: X" |
| Missing JWT token | 401 | Unauthorized |

---

## Advanced Scenarios

### Scenario 1: Bulk Create Tasks in List
```bash
# First create the task list
curl -X POST http://localhost:8080/api/tasklists \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name": "Q4 Goals", "description": "Quarter 4 objectives"}'

# Response includes: "id": 5

# Then create multiple tasks in that list
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Goal 1: Increase performance",
    "taskListId": 5
  }'

curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Goal 2: Improve UI/UX",
    "taskListId": 5
  }'
```

### Scenario 2: Reorganize Tasks
```bash
# Move all high-priority tasks to "Urgent" list
# Step 1: Get all tasks
GET /api/tasks

# Step 2: Update each with taskListId pointing to "Urgent" list
PUT /api/tasks/{id}
{
  "title": "...",
  "description": "...",
  "taskListId": 3  // Urgent list
}
```

### Scenario 3: Archive Old Tasks
```bash
# Create an "Archive" task list
POST /api/tasklists
{
  "name": "Archive 2024",
  "description": "Completed tasks from 2024"
}

# Move old completed tasks there
PUT /api/tasks/{id}
{
  "title": "...",
  "completed": true,
  "taskListId": 6  // Archive list
}
```

---

## Implementation Details

### Database
- `tasks.task_list_id` is a **foreign key** (nullable)
- Deleting a task list with `ON DELETE CASCADE` removes all associated tasks
- Deleting a task list doesn't require separate task updates

### Service Layer
- TaskListRepository is injected into TaskService
- Ownership validation on both Task and TaskList
- Proper error messages for all scenarios

### Testing
- All 36 tests pass with new relationship handling
- Tests include scenarios with and without taskListId

---

## Backward Compatibility

✅ **Fully backward compatible**
- Existing tasks without taskListId continue to work
- Old API calls without taskListId parameter work unchanged
- TaskResponse always includes taskListId (null if uncategorized)

---

## Status

✅ **Complete & Tested**
- All 36 tests passing
- Service layer implementation done
- DTOs updated with new fields
- Full documentation provided

