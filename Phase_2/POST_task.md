sequenceDiagram
    participant Requester as Client/Requester
    participant API as API/Interface Layer
    participant Logic as Business Logic Layer
    participant DataAccess as Data Access Layer
    participant Storage as Data Storage

    Requester->>API: POST /api/task {task_content, parent_task_id?}
    API->>Logic: Process Task Creation
    
    Note over Requester,Storage: SCENARIO A: Valid Request (Parent Exists or Null)
    Logic->>DataAccess: Check Parent Existence (If ID provided)
    DataAccess->>Storage: Query
    Storage-->>DataAccess: Result (Found or Null)
    Logic->>DataAccess: Persist New Task Resource
    DataAccess->>Storage: Insert/Write Task
    Storage-->>DataAccess: New Task Object
    DataAccess-->>Logic: Task Object
    Logic-->>API: Success Data/Object
    API-->>Requester: 201 Created Response

    Note over Requester,Storage: SCENARIO B: Conflict (Invalid Parent Task ID)
    Logic->>DataAccess: Check Parent Existence
    DataAccess->>Storage: Query
    Storage-->>DataAccess: Result (Not Found)
    Logic-->>API: Conflict/Error Signal
    API-->>Requester: 400 Bad Request Response