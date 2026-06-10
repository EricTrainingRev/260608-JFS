# Account Endpoints
Authentication
Register - create new user
POST /api/auth/register
Request
Body: JSON: {username, password}
Response: 201

Login - authorize user
POST /api/auth/login
Request
Body: JSON: {username, password}
Response: 200
Body: JSON {user_id, username, password}


# Task Endpoints
Create a task
POST /api/task
Request
Body: JSON: {task_content, parent_id?}
Response: 200
Body: {task_id, task_content, parent_id?, is_complete}

Get all tasks
GET /api/task
Response: 200
Body: {task_id, task_content, parent_id, is_complete}

Get task by id
GET /api/task/{task_id}
Response: 200
Body: {task_id, task_content, parent_id, is_complete}
Update task

PATCH /api/task/{task_id}
Request
Body: JSON: {task_content?, is_complete?}
Response: 200
Body: {task_id, task_content, task_parent?, is_complete}

Delete tasks
DELETE /api/task/{task_id}
Response: 204
If a task is deleted and has subtasks that have NOT been deleted, query for subtasks to delete