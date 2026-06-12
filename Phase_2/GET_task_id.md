sequenceDiagram
    participant Requester as Client/Requester
    participant API as API/Interface Layer
    participant Logic as Business Logic Layer
    participant DataAccess as Data Access Layer
    participant Storage as Data Storage

    Requester->>API: GET /api/task/{id}
    API->>Logic: Process Fetch Request
    
    Note over Requester,Storage: SCENARIO A: Task Found
    Logic->>DataAccess: Check Existence
    DataAccess->>Storage: Query (SELECT WHERE id = ?)
    Storage-->>DataAccess: Result (Found)
    DataAccess-->>Logic: Task Object
    Logic-->>API: Success Data/Object
    API-->>Requester: 200 OK Response

    Note over Requester,Storage: SCENARIO B: Task Not Found
    Logic->>DataAccess: Check Existence
    DataAccess->>Storage: Query
    Storage-->>DataAccess: Result (Not Found)
    Logic-->>API: Error Signal
    API-->>Requester: 404 Not Found Response