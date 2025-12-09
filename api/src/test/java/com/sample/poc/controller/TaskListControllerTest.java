package com.sample.poc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sample.poc.application.dto.CreateTaskListRequest;
import com.sample.poc.application.dto.TaskListResponse;
import com.sample.poc.application.dto.UpdateTaskListRequest;
import com.sample.poc.application.service.TaskListService;
import com.sample.poc.presentation.controller.TaskListController;

@ExtendWith(MockitoExtension.class)
public class TaskListControllerTest {

        @Mock
        private TaskListService taskListService;

        @InjectMocks
        private TaskListController taskListController;

        private MockMvc mockMvc;

        @BeforeEach
        void setUp() {
                mockMvc = MockMvcBuilders.standaloneSetup(taskListController).build();
        }

        @Test
        void testCreateTaskList_Success() throws Exception {
                TaskListResponse response = TaskListResponse.builder()
                                .id(1L)
                                .name("Work")
                                .taskCount(0)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                when(taskListService.createTaskList(any(CreateTaskListRequest.class))).thenReturn(response);

                mockMvc.perform(post("/api/tasklists")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"Work\",\"description\":\"Work tasks\"}"))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.name").value("Work"));

                verify(taskListService, times(1)).createTaskList(any(CreateTaskListRequest.class));
        }

        @Test
        void testGetUserTaskLists_Success() throws Exception {
                TaskListResponse response = TaskListResponse.builder()
                                .id(1L)
                                .name("Work")
                                .taskCount(0)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                when(taskListService.getUserTaskLists()).thenReturn(List.of(response));

                mockMvc.perform(get("/api/tasklists")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").value("Work"));

                verify(taskListService, times(1)).getUserTaskLists();
        }

        @Test
        void testGetTaskListById_Success() throws Exception {
                TaskListResponse response = TaskListResponse.builder()
                                .id(1L)
                                .name("Work")
                                .taskCount(0)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                when(taskListService.getTaskListById(1L)).thenReturn(response);

                mockMvc.perform(get("/api/tasklists/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Work"));

                verify(taskListService, times(1)).getTaskListById(1L);
        }

        @Test
        void testUpdateTaskList_Success() throws Exception {
                TaskListResponse response = TaskListResponse.builder()
                                .id(1L)
                                .name("Work Updated")
                                .taskCount(0)
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                when(taskListService.updateTaskList(eq(1L), any(UpdateTaskListRequest.class))).thenReturn(response);

                mockMvc.perform(put("/api/tasklists/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"Work Updated\",\"description\":\"Updated description\"}"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Work Updated"));

                verify(taskListService, times(1)).updateTaskList(eq(1L), any(UpdateTaskListRequest.class));
        }

        @Test
        void testDeleteTaskList_Success() throws Exception {
                doNothing().when(taskListService).deleteTaskList(1L);

                mockMvc.perform(delete("/api/tasklists/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNoContent());

                verify(taskListService, times(1)).deleteTaskList(1L);
        }

}
