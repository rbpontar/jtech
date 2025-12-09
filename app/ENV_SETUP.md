# Environment Variables

The application uses the following environment variables for backend configuration:

## `VITE_API_URL`

The base URL for the backend API.

- **Default:** `http://localhost:3000/api`
- **Type:** String
- **Example:** `https://api.example.com/api`

To use a custom backend URL, create a `.env` file in the project root:

```env
VITE_API_URL=https://your-backend-api.com/api
```

Vite will automatically load environment variables prefixed with `VITE_` and make them available via `import.meta.env`.

## Backend API Endpoints

The following endpoints are expected from the backend:

### POST `/auth/login`

Authenticates a user with email and password.

**Request:**

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response:**

```json
{
  "user": {
    "id": "12345",
    "email": "user@example.com",
    "name": "John Doe"
  },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Status Codes:**

- `200` - Login successful
- `401` - Invalid credentials
- `400` - Missing or invalid input

### POST `/auth/logout`

Logs out the current user. Requires a valid token in the `Authorization` header.

**Response:** Empty on success

**Status Codes:**

- `200` - Logout successful
- `401` - Unauthorized

### GET `/auth/me`

Retrieves the current authenticated user's information. Requires a valid token in the `Authorization` header.

**Response:**

```json
{
  "user": {
    "id": "12345",
    "email": "user@example.com",
    "name": "John Doe"
  }
}
```

**Status Codes:**

- `200` - User found
- `401` - Unauthorized

## Authentication

All authenticated requests should include the token in the `Authorization` header:

```
Authorization: Bearer <token>
```

The token is automatically included by the API service in all requests after login.
