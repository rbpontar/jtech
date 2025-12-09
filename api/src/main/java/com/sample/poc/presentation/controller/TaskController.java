package com.sample.poc.presentation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sample.poc.application.dto.CreateTaskRequest;
import com.sample.poc.application.dto.TaskResponse;
import com.sample.poc.application.dto.UpdateTaskRequest;
import com.sample.poc.application.service.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TaskController {

  @Autowired
  private TaskService taskService;

  @PostMapping
  public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
    TaskResponse taskResponse = taskService.createTask(createTaskRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
  }

  // @GetMapping
  // public ResponseEntity<List<TaskResponse>> getUserTasks() {
  // List<TaskResponse> tasks = taskService.getUserTasks();
  // return ResponseEntity.ok(tasks);
  // }

  @GetMapping("/{id}")
  public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
    TaskResponse taskResponse = taskService.getTaskById(id);
    return ResponseEntity.ok(taskResponse);
  }

  @GetMapping("/tasklist/{id}")
  public ResponseEntity<List<TaskResponse>> getTaskBytaskListId(@PathVariable Long id) {
    List<TaskResponse> taskResponse = taskService.findByTaskListId(id);
    return ResponseEntity.ok(taskResponse);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,
      @Valid @RequestBody UpdateTaskRequest updateTaskRequest) {
    TaskResponse taskResponse = taskService.updateTask(id, updateTaskRequest);
    return ResponseEntity.ok(taskResponse);
  }

  @PutMapping("/{id}/{status}")
  public ResponseEntity<TaskResponse> toggleTask(@PathVariable Long id, @PathVariable boolean status) {
    TaskResponse taskResponse = taskService.toggleTask(id, status);
    return ResponseEntity.ok(taskResponse);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();

  }
}
