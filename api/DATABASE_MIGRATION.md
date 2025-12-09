# 🔄 TaskList Feature - Database Migration

## Overview

This document describes the database changes required to support the TaskList feature.

## Prerequisites

- PostgreSQL 12+ 
- Access to your tasklist_db database
- Existing users and tasks tables

## Migration Steps

### Option 1: Automatic Migration (Recommended)

The application uses Hibernate with `spring.jpa.hibernate.ddl-auto=update`, which will automatically create the `task_lists` table and add the `task_list_id` column to `tasks` table on first run.

**Steps:**
1. Ensure `application.properties` has `spring.jpa.hibernate.ddl-auto=update`
2. Start the application: `bash mvnw spring-boot:run`
3. Hibernate will automatically create necessary tables and columns
4. Check console logs for "Hibernate: create table task_lists"

### Option 2: Manual SQL Migration

If you prefer manual control, execute these SQL statements:

#### 1. Create task_lists table
```sql
CREATE TABLE task_lists (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_task_lists_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT unique_name_per_user UNIQUE (name, user_id)
);
```

#### 2. Create index for faster queries
```sql
CREATE INDEX idx_task_lists_user_id ON task_lists(user_id);
```

#### 3. Add task_list_id column to tasks table
```sql
ALTER TABLE tasks ADD COLUMN task_list_id BIGINT;
```

#### 4. Add foreign key constraint
```sql
ALTER TABLE tasks 
ADD CONSTRAINT fk_tasks_task_list 
FOREIGN KEY (task_list_id) 
REFERENCES task_lists(id) ON DELETE SET NULL;
```

#### 5. Create index on tasks
```sql
CREATE INDEX idx_tasks_task_list_id ON tasks(task_list_id);
```

### Verification

After migration, verify the schema:

```sql
-- Check task_lists table exists
\dt task_lists

-- Check columns
\d task_lists

-- Check constraints
SELECT constraint_name, constraint_type 
FROM information_schema.table_constraints 
WHERE table_name = 'task_lists';

-- Verify foreign keys
SELECT constraint_name, table_name, column_name 
FROM information_schema.key_column_usage 
WHERE table_name = 'task_lists';
```

## Rollback (if needed)

To rollback the migration:

```sql
-- Drop foreign key from tasks
ALTER TABLE tasks DROP CONSTRAINT IF EXISTS fk_tasks_task_list;

-- Drop task_list_id column
ALTER TABLE tasks DROP COLUMN IF EXISTS task_list_id;

-- Drop task_lists table
DROP TABLE IF EXISTS task_lists CASCADE;
```

## Data Integrity Notes

✅ **Cascade Delete**: Deleting a user deletes all their task lists and tasks  
✅ **Orphan Removal**: Deleting a task list sets task_list_id to NULL (keeps tasks)  
✅ **Unique Names**: Each user cannot have duplicate list names  
✅ **Indexes**: Added for optimal query performance  

## Testing Migration

After migration, test with:

```bash
# 1. Create a user (if not exists)
POST /api/auth/register
{
  "name": "Test User",
  "email": "test@example.com",
  "password": "password123"
}

# 2. Login to get JWT token
POST /api/auth/login
{
  "email": "test@example.com",
  "password": "password123"
}

# 3. Create a task list
POST /api/tasklists
Authorization: Bearer <YOUR_TOKEN>
{
  "name": "Work",
  "description": "Work tasks"
}

# 4. Verify in database
SELECT * FROM task_lists;
```

## Docker Compose

If using Docker Compose, the migration happens automatically:

```bash
docker compose up --build
```

Postgres will initialize with the correct schema on first run.

## Troubleshooting

### Error: "relation 'task_lists' does not exist"

**Cause**: Migration hasn't run yet  
**Solution**: Start the application to trigger automatic migration

```bash
bash mvnw spring-boot:run
```

### Error: "duplicate key value violates unique constraint"

**Cause**: Trying to create task list with duplicate name for same user  
**Solution**: Use unique names per user

### Error: "foreign key constraint failed"

**Cause**: Referenced user_id doesn't exist  
**Solution**: Ensure user exists before creating task lists

## Performance Considerations

The schema includes:
- ✅ Foreign key indexes for join performance
- ✅ Unique constraint index for name lookups
- ✅ Timestamp columns for sorting

## Connection Strings

### Local PostgreSQL
```
jdbc:postgresql://localhost:5432/tasklist_db
```

### Docker Compose
```
jdbc:postgresql://db:5432/tasklist_db
```

## Additional Resources

- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Hibernate DDL Documentation](https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#configurations-hbmddl)
- [Spring Boot Data JPA](https://spring.io/projects/spring-data-jpa)

---

**Last Updated**: December 3, 2025  
**Compatibility**: Spring Boot 4.0.0, Hibernate 6.x, PostgreSQL 12+

