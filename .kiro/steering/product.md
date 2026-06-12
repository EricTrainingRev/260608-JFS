**File 3: `product.md`**
```md
# Product: Todo App

A full-stack, secure Todo Management application built by team "The Blind" (group ID: `com.theblind`) utilizing Agile methodologies.

## User Stories

1.  **Registration:** As a new user, I can register an account.
2.  **Authentication:** As a new user, I can log in and out securely.
3.  **Task Management:** As a user, I can CRUD primary Todo items.
4.  **Organization:** As a user, I can CRUD nested subtasks.

## Core Functionality

* User authentication via session tokens (JWT Bearer tokens).
* Data isolation: Users can only access and manipulate their own tasks.
* Hierarchical tasks: Tasks support recursive nesting via an optional `parent_task_id`.
* Cascading deletes: Deleting a parent task automatically removes all associated subtasks from the SQLite database.

## API Specification

All routes are prefixed with `/api`. Protected routes require `Authorization: Bearer <token>`.

| Method | Path | Payload | Description |
| :--- | :--- | :--- | :--- |
| POST | `/api/auth/register` | `{username, password}` | Create new account |
| POST | `/api/auth/login` | `{username, password}` | Login, returns JWT |
| POST | `/api/task` | `{task_content, parent_task_id?}` | Create task |
| GET | `/api/task` | None | Get all user tasks |
| GET | `/api/task/{id}` | None | Get a specific task |
| PATCH | `/api/task/{id}` | `{task_content?, is_complete?}`| Update task state |
| DELETE | `/api/task/{id}` | None | Delete task and subtasks |

## Database Schema (Task Object Shape)

```json
{
  "id": 1,
  "task_content": "string",
  "parent_task_id": null,
  "is_complete": false
}