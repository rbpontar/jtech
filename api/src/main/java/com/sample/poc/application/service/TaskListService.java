package com.sample.poc.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sample.poc.application.dto.CreateTaskListRequest;
import com.sample.poc.application.dto.TaskListResponse;
import com.sample.poc.application.dto.UpdateTaskListRequest;
import com.sample.poc.domain.model.Task;
import com.sample.poc.domain.model.TaskList;
import com.sample.poc.domain.model.User;
import com.sample.poc.domain.repository.TaskListRepository;
import com.sample.poc.domain.repository.TaskRepository;
import com.sample.poc.presentation.exception.EmailAlreadyExistsException;
import com.sample.poc.presentation.exception.ResourceNotFoundException;

@Service
@Transactional
public class TaskListService extends BaseService {

    @Autowired
    private TaskListRepository taskListRepository;

    @Autowired
    private TaskRepository taskRepository;

    public TaskListResponse createTaskList(CreateTaskListRequest request) {
        User currentUser = getCurrentUser();

        // Check if task list with same name already exists for this user
        if (taskListRepository.existsByNameAndUser(request.getName(), currentUser)) {
            throw new EmailAlreadyExistsException(
                    "Já existe uma lista de tarefas com o nome '" + request.getName() + "'");
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("O nome da lista de tarefas é obrigatório");
        }

        TaskList taskList = TaskList.builder()
                .name(request.getName())
                .user(currentUser)
                .build();

        TaskList savedTaskList = taskListRepository.save(taskList);
        return TaskListResponse.fromTaskList(savedTaskList);
    }

    public List<TaskListResponse> getUserTaskLists() {
        User currentUser = getCurrentUser();
        List<TaskList> taskList = taskListRepository.findByUser(currentUser);
        return taskList.stream()
                .map(TaskListResponse::fromTaskList)
                .collect(Collectors.toList());
    }

    public TaskListResponse getTaskListById(Long id) {
        User currentUser = getCurrentUser();

        TaskList taskList = taskListRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Lista de tarefas não encontrada com id: " + id));
        return TaskListResponse.fromTaskList(taskList);
    }

    public TaskListResponse updateTaskList(Long id, UpdateTaskListRequest request) {
        User currentUser = getCurrentUser();

        TaskList taskList = taskListRepository.findByIdAndUser(id, currentUser)
            .orElseThrow(() -> new ResourceNotFoundException("Lista de tarefas não encontrada com id: " + id));

        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("O nome da lista de tarefas é obrigatório");
        }

        if (!taskList.getName().equals(request.getName()) &&
                taskListRepository.existsByNameAndUser(request.getName(), currentUser)) {
            throw new EmailAlreadyExistsException(
                    "Já existe uma lista de tarefas com o nome '" + request.getName() + "'");
        }

        taskList.setName(request.getName());
        TaskList updatedTaskList = taskListRepository.save(taskList);
        return TaskListResponse.fromTaskList(updatedTaskList);
    }

    public void deleteTaskList(Long id) {
        taskListRepository.deleteById(id);
    }

    public void moveTaskToList(Long taskId, Long listId) {
        User currentUser = getCurrentUser();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com id: " + taskId));

        TaskList taskList = taskListRepository.findByIdAndUser(listId, currentUser)
                .orElseThrow(() -> new ResourceNotFoundException("Lista de tarefas não encontrada com id: " + listId));

        task.setTaskList(taskList);
        taskRepository.save(task);
    }

    public void removeTaskFromList(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com id: " + taskId));

        taskRepository.save(task);
    }

}
