```mermaid
sequenceDiagram
    participant Requester as Client/Requester
    participant API as API/Interface Layer
    participant Logic as Business Logic Layer
    participant DataAccess as Data Access Layer
    participant Storage as Data Storage

    Requester->>API: DELETE /api/task/:id, Body = {user_id}

    Note over Requester,Storage: SCENARIO A: Task found, delete successful
    API->>Logic: Process Delete One Request
    Logic->>DataAccess: Request subtasks of task to be deleted
    DataAccess->>Storage: Query (DELETE FROM tasks WHERE parent_id = :id;)
    Storage-->>DataAccess: Subtasks deleted (if no subtasks found, no error occurs, but no rows affected)
    DataAccess->>Storage: Query (DELETE FROM tasks WHERE id = :id;)
    Storage-->>DataAccess: Main task deleted
    DataAccess-->>Logic: Empty Object
    Logic-->>API: Success Object
    API-->>Requester: 204 No Content Response, Body = {}

    Note over Requester,Storage: SCENARIO C: User is not logged in
    API->>Logic: Process Delete One Request
    Logic-->>API: User is not logged in and cannot be authenticated, Error Signal
    API-->>Requester: 401 Unauthorized Response, Body = {error}

    Note over Requester,Storage: SCENARIO D: User is not authorized to update task
    API->>Logic: Process Delete One Request
    Logic-->>API: User is not authorized (either not logged in or not owner of task), Error Signal
    API-->>Requester: 403 Forbidden Response, Body = {error}

    Note over Requester,Storage: SCENARIO D: Task not found
    API->>Logic: Process Delete One Request
    Logic->>DataAccess: Request subtasks of task to be deleted, then main task
    DataAccess->>Storage: Recursive Query to delete any descendant tasks of main task (DELETE FROM tasks WHERE parent_id = :id;)
    Storage-->>DataAccess: Subtasks deleted (if no subtasks found, no error occurs, but no rows affected)
    DataAccess->>Storage: Query (DELETE FROM tasks WHERE id = :id;)
    Storage-->>DataAccess: No ain task deleted
    DataAccess-->>Logic: Empty Object
    Logic-->>API: Success Object
    API-->>Requester: 404 Not Found Response, Body = {}
```