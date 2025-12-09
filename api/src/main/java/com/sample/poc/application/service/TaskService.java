package com.sample.poc.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sample.poc.application.dto.CreateTaskRequest;
import com.sample.poc.application.dto.TaskResponse;
import com.sample.poc.application.dto.UpdateTaskRequest;
import com.sample.poc.domain.model.Task;
import com.sample.poc.domain.model.TaskList;
import com.sample.poc.domain.model.User;
import com.sample.poc.domain.repository.TaskListRepository;
import com.sample.poc.domain.repository.TaskRepository;
import com.sample.poc.presentation.exception.ResourceNotFoundException;

@Service
@Transactional
public class TaskService extends BaseService {

  @Autowired
  private TaskRepository taskRepository;

  @Autowired
  private TaskListRepository taskListRepository;

  public TaskResponse createTask(CreateTaskRequest createTaskRequest) {
    User currentUser = getCurrentUser();

    Task task = Task.builder()
        .title(createTaskRequest.getTitle())
        .description(createTaskRequest.getDescription())
        .completed(false)
        .build();

    if (createTaskRequest.getTitle() == null || createTaskRequest.getTitle().isBlank()) {
      throw new IllegalArgumentException("O título é obrigatório");
    }

    if (createTaskRequest.getTaskListId() != null) {
      TaskList taskList = taskListRepository.findByIdAndUser(createTaskRequest.getTaskListId(), currentUser)
          .orElseThrow(
              () -> new ResourceNotFoundException(
                  "Lista de tarefas não encontrada com id: " + createTaskRequest.getTaskListId()));

      task.setTaskList(taskList);
    }

    Task savedTask = taskRepository.save(task);
    return TaskResponse.fromTask(savedTask);
  }

  public TaskResponse getTaskById(Long taskId) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com id: " + taskId));
    return TaskResponse.fromTask(task);
  }

  public List<TaskResponse> findByTaskListId(Long taskListId) {
    List<Task> tasks = taskRepository.findByTaskListId(taskListId);

    return tasks.stream()
        .map(TaskResponse::fromTask)
        .collect(Collectors.toList());
  }

  public TaskResponse toggleTask(Long taskId, Boolean status) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com id: " + taskId));

    task.setCompleted(status);
    Task updatedTask = taskRepository.save(task);
    return TaskResponse.fromTask(updatedTask);
  }

  public TaskResponse updateTask(Long taskId, UpdateTaskRequest updateTaskRequest) {
    User currentUser = getCurrentUser();

    Task task = taskRepository.findById(taskId)
        .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com id: " + taskId));

    if (updateTaskRequest.getTitle() != null) {
      task.setTitle(updateTaskRequest.getTitle());
    }
    if (updateTaskRequest.getDescription() != null) {
      task.setDescription(updateTaskRequest.getDescription());
    }
    if (updateTaskRequest.getCompleted() != null) {
      task.setCompleted(updateTaskRequest.getCompleted());
    }
    if (updateTaskRequest.getTaskListId() != null) {
      // Move task to a different task list
      TaskList taskList = taskListRepository.findByIdAndUser(updateTaskRequest.getTaskListId(), currentUser)
          .orElseThrow(
              () -> new ResourceNotFoundException(
                  "Lista de tarefas não encontrada com id: " + updateTaskRequest.getTaskListId()));

      task.setTaskList(taskList);
    }

    Task updatedTask = taskRepository.save(task);
    return TaskResponse.fromTask(updatedTask);
  }

  public void deleteTask(Long taskId) {
    if (!taskRepository.existsById(taskId)) {
      throw new ResourceNotFoundException("Tarefa não encontrada com id: " + taskId);
    }

    taskRepository.deleteById(taskId);
  }
}
