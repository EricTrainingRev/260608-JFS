# Account Endpoints

## Authentication

When a user wants to make a new account:

- Request:
  - Method: POST 
  - Url: /api/auth/register
  - Body: username, password
- Success:
  - Response: 201
  - Body: empty
- Fail:
  - Status: 400
  - body: error message

When a user want to log in:

- Request:
  - Method: POST 
  - Url: /api/auth/login
  - Body: username, password
- Success:
  - Response: 200
  - Body: session token
- Fail:
  - Status: 400
  - body: error message


# Task Endpoints

When a user wants to make a new task

- Request:
  - Method: POST 
  - Url: /api/task
  - Body: task_content, parent_id?
- Success:
  - Response: 201
  - Body: The created task object {task_id, task_content, parent_id?, is_complete}
- Fail:
  - Status: 400
  - body: error message

When a user wants to view all tasks

- Request:
  - Method: GET
  - Url: /api/task
  - Body: None
- Success:
  - Response: 200
  - Body: Array of task objects: [{task_id, task_content, parent_id?, is_complete}]
- Fail:
  - Status: 400/500
  - body: error message

- - 

When a user wants to a specific tasks

- Request:
  - Method: GET
  - Url: /api/task/{task_id}
  - Body: None
- Success:
  - Response: 200
  - Body: Task objects: {task_id, task_content, parent_id?, is_complete}
- Fail:
  - Status: 400/500
  - body: error message

The user wants to update an existing task

- Request:
  - Method: PATCH
  - Url: /api/task/{task_id}
  - Body: {task_content?, is_complete?}
- Success:
  - Response: 200
  - Body: The updated task object: {task_id, task_content, parent_id?, is_complete}
- Fail:
  - Status: 400
  - body: error message

- - 

The user wants to delete an existing task and it's subtasks

- Request:
  - Method: DELETE
  - Url: /api/task/{task_id}
  - Body: None
- Success:
  - Response: 204
  - Body: None
- Fail:
  - Status: 400
  - body: error message