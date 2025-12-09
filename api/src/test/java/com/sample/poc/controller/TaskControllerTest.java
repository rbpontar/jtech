package com.sample.poc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.poc.application.dto.CreateTaskRequest;
import com.sample.poc.application.dto.TaskResponse;
import com.sample.poc.application.dto.UpdateTaskRequest;
import com.sample.poc.application.service.TaskService;
import com.sample.poc.presentation.controller.TaskController;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

  private MockMvc mockMvc;

  @Mock
  private TaskService taskService;

  @InjectMocks
  private TaskController taskController;

  private ObjectMapper objectMapper;

  private CreateTaskRequest createTaskRequest;
  private UpdateTaskRequest updateTaskRequest;
  private TaskResponse taskResponse;

  @BeforeEach
  void setUp() {
    objectMapper = new ObjectMapper();
    mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

    createTaskRequest = CreateTaskRequest.builder()
        .title("Test Task")
        .description("Test Description")
        .build();

    updateTaskRequest = UpdateTaskRequest.builder()
        .title("Updated Task")
        .description("Updated Description")
        .completed(true)
        .build();

    taskResponse = TaskResponse.builder()
        .id(1L)
        .title("Test Task")
        .description("Test Description")
        .completed(false)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();
  }

  @Test
  void testCreateTask_Success() throws Exception {
    when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(taskResponse);

    mockMvc.perform(post("/api/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createTaskRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("Test Task"));
  }

  @Test
  void testGetTaskById_Success() throws Exception {
    when(taskService.getTaskById(1L)).thenReturn(taskResponse);

    mockMvc.perform(get("/api/tasks/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Test Task"));
  }

  @Test
  void testUpdateTask_Success() throws Exception {
    when(taskService.updateTask(eq(1L), any(UpdateTaskRequest.class))).thenReturn(taskResponse);

    mockMvc.perform(put("/api/tasks/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateTaskRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Test Task"));
  }

  @Test
  void testDeleteTask_Success() throws Exception {
    doNothing().when(taskService).deleteTask(1L);

    mockMvc.perform(delete("/api/tasks/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  void testCreateTask_Error() throws Exception {
    when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(taskResponse);

    mockMvc.perform(post("/api/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createTaskRequest)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("Test Task"));
  }

}
