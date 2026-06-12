```mermaid
sequenceDiagram
    participant Client as Client/Requester
    participant Controller as API/Interface Layer
    participant Service as Business Logic Layer
    participant Repository as Data Access Layer
    participant Data Storage

    Client->>Controller: [Create request to /api/auth/login]
    Controller->>Service: [Pass username and password]

    Note over Client, Data Storage: Account info is Valid
    Service->>Repository: [Check exists]
    Repository->>Data Storage: [Query by username]
    Data Storage-->>Repository: [Return null]
    Service->>Repository: [Persist new Account]
    Repository->>Data Storage: [Insert Account]
    Data Storage-->>Repository: [Return new Account Object]
    Repository-->>Service: [Return Account Object]
    Service-->>Controller: [Return Success/Token]
    Controller-->>Client: [200 Success Response]
    
    Note over Client, Data Storage: Account username overlap
    Service->>Repository: [Request Account Data]
    Repository->>Data Storage: [Query by username]
    Data Storage-->>Repository: [Return Null]
    Service-->>Controller: [Return Fail/Error]
    Controller-->>Client: [409 Error Response]

    Note over Client, Data Storage: Account info malformed/password invalid
    Service-->>Controller: [Return Failure/Error]
    Controller-->>Client: [Return 422 response]
```