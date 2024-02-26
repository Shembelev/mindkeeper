# MindsKeeper Portfolio Project

Welcome to MindsKeeper, a personal portfolio project with a REST API built using Spring Boot. The project is accessible at [https://mindskeeper.mshembelev.site](https://mindskeeper.mshembelev.site), and the API documentation can be found at [https://mindskeeper.mshembelev.site:8080/swagger-ui/index.html#/](https://mindskeeper.mshembelev.site:8080/swagger-ui/index.html#/).

## Tasks

### Update Task Group
- **Method:** `POST`
- **Endpoint:** `/task/group/update`
- **Description:** Update all tasks in a group.

### Get All User Tasks
- **Method:** `GET`
- **Endpoint:** `/task/`
- **Description:** Retrieve all tasks for the user.

### Create Task
- **Method:** `POST`
- **Endpoint:** `/task/`
- **Description:** Create a new task.

### Get Tasks in Group
- **Method:** `GET`
- **Endpoint:** `/task/group/{groupId}`
- **Description:** Retrieve all tasks in a specified group.

### Delete Task Group
- **Method:** `DELETE`
- **Endpoint:** `/task/group/{groupId}`
- **Description:** Delete all tasks in a group.

## Notes

### Update Note
- **Method:** `POST`
- **Endpoint:** `/note/update`
- **Description:** Update a note.

### Get All User Notes
- **Method:** `GET`
- **Endpoint:** `/note/`
- **Description:** Retrieve all notes for the user.

### Create Note
- **Method:** `POST`
- **Endpoint:** `/note/`
- **Description:** Create a new note.

### Delete Note
- **Method:** `DELETE`
- **Endpoint:** `/note/{noteId}`
- **Description:** Delete a note.

## Files

### Get User Files
- **Method:** `GET`
- **Endpoint:** `/file/`
- **Description:** Retrieve a list of user files.

### Upload File
- **Method:** `POST`
- **Endpoint:** `/file/`
- **Description:** Upload a file to the server.

### Download File
- **Method:** `GET`
- **Endpoint:** `/file/download/{fileId}`
- **Description:** Download a file from the server.

### Delete File
- **Method:** `DELETE`
- **Endpoint:** `/file/{fileId}`
- **Description:** Delete a file.

## Authentication

### Update User
- **Method:** `POST`
- **Endpoint:** `/auth/update`
- **Description:** Update user information.

### Sign-Up
- **Method:** `POST`
- **Endpoint:** `/auth/sign-up`
- **Description:** Register a new user.

### Sign-In
- **Method:** `POST`
- **Endpoint:** `/auth/sign-in`
- **Description:** Authenticate a user.

## Testing

### Example
- **Method:** `GET`
- **Endpoint:** `/example`
- **Description:** Available only for authenticated users.

### Admin Example
- **Method:** `GET`
- **Endpoint:** `/example/admin`
- **Description:** Available only for authenticated users with ADMIN role.
