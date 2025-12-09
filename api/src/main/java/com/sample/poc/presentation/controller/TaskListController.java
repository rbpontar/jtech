package com.sample.poc.presentation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.poc.application.dto.CreateTaskListRequest;
import com.sample.poc.application.dto.TaskListResponse;
import com.sample.poc.application.dto.UpdateTaskListRequest;
import com.sample.poc.application.service.TaskListService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasklists")
public class TaskListController {

    @Autowired
    private TaskListService taskListService;

    /**
     * Create a new task list.
     * POST /api/tasklists
     */
    @PostMapping
    public ResponseEntity<TaskListResponse> createTaskList(@Valid @RequestBody CreateTaskListRequest request) {
        TaskListResponse response = taskListService.createTaskList(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get all task lists for the authenticated user.
     * GET /api/tasklists
     */
    @GetMapping
    public ResponseEntity<List<TaskListResponse>> getUserTaskLists() {
        List<TaskListResponse> response = taskListService.getUserTaskLists();
        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific task list by ID.
     * GET /api/tasklists/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskListResponse> getTaskListById(@PathVariable Long id) {
        TaskListResponse response = taskListService.getTaskListById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Update a task list (rename with optional description update).
     * PUT /api/tasklists/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskListResponse> updateTaskList(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskListRequest request) {
        TaskListResponse response = taskListService.updateTaskList(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete a task list and all its tasks.
     * DELETE /api/tasklists/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskList(@PathVariable Long id) {
        taskListService.deleteTaskList(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Move a task to a specific task list.
     * PUT /api/tasklists/{listId}/tasks/{taskId}
     */
    @PutMapping("/{listId}/tasks/{taskId}")
    public ResponseEntity<Void> moveTaskToList(
            @PathVariable Long listId,
            @PathVariable Long taskId) {
        taskListService.moveTaskToList(taskId, listId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove a task from its task list (set to uncategorized).
     * DELETE /api/tasklists/{listId}/tasks/{taskId}
     */
    @DeleteMapping("/{listId}/tasks/{taskId}")
    public ResponseEntity<Void> removeTaskFromList(@PathVariable Long taskId) {
        taskListService.removeTaskFromList(taskId);
        return ResponseEntity.noContent().build();
    }
}
