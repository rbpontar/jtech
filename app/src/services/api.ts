import type { Task } from "@/stores/todos";

const API_BASE_URL =
  import.meta.env.VITE_API_URL || "http://localhost:8082/api";

export interface LoginRequest {
  email: string;
  password: string;
  name?: string;
}

export interface LoginResponse {
  email: string;
  name?: string;
  accessToken: string;
}

export interface TaskListRequest {
  name: string;
}

export interface TaskListResponse {
  id: string;
  name: string;
  description: string;
  createdAt: string;
  updatedAt: string;
  taskCount: number;
}

export class ApiError extends Error {
  constructor(public status: number, message: string) {
    super(message);
    this.name = "ApiError";
  }
}

async function request<T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> {
  const url = `${API_BASE_URL}${endpoint}`;

  const token = localStorage.getItem("user_v1")
    ? JSON.parse(localStorage.getItem("user_v1") || "{}").token
    : null;

  const headers: Record<string, string> = {
    "Content-Type": "application/json",
    Accept: "*/*",
  };

  if (options.headers) {
    Object.assign(headers, options.headers);
  }

  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }

  const response = await fetch(url, {
    ...options,
    headers,
  });

  if (!response.ok) {
    const error = await response
      .json()
      .catch(() => ({ message: response.statusText }));
    throw new ApiError(response.status, error.message || "Request failed");
  }
  const text = await response.text();
  if (!text) {
    return {} as T;
  } else {
    return JSON.parse(text) as Promise<T>;
  }
}

export const api = {
  auth: {
    login: (payload: LoginRequest) =>
      request<LoginResponse>("/auth/login", {
        method: "POST",
        body: JSON.stringify(payload),
      }),
    logout: () =>
      request<void>("/auth/logout", {
        method: "POST",
      }),
    register: (payload: LoginRequest) =>
      request<string>("/auth/register", {
        method: "POST",
        body: JSON.stringify(payload),
      }),
  },

  list: {
    create: (payload: { name: string }) =>
      request<TaskListResponse>("/tasklists", {
        method: "POST",
        body: JSON.stringify(payload),
      }),
    update: (id: string, payload: TaskListRequest) =>
      request<TaskListResponse>("/tasklists/" + id, {
        method: "PUT",
        body: JSON.stringify(payload),
      }),
    delete: (id: string) =>
      request("/tasklists/" + id, {
        method: "DELETE",
      }),
    getAll: () =>
      request<[]>("/tasklists", {
        method: "GET",
      }),
    getById: (id: string) =>
      request<{ id: string }>("/tasklists/" + id, {
        method: "GET",
      }),
  },
  task: {
    getByTaskListId: (id: string) =>
      request<Task[]>("/tasks/tasklist/" + id, {
        method: "GET",
      }),
    create: (task: Task) =>
      request<Task>("/tasks", {
        method: "POST",
        body: JSON.stringify(task),
      }),
    update: (id: string, task: Task) =>
      request<Task[]>("/tasks/" + id, {
        method: "PUT",
        body: JSON.stringify(task),
      }),
    delete: (id: string) =>
      request("/tasks/" + id, {
        method: "DELETE",
      }),
    toggle: (id: string, status: boolean) =>
      request("/tasks/" + id + "/" + status, {
        method: "PUT",
      }),
  },
};
