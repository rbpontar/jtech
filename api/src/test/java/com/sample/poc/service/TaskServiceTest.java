package com.sample.poc.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sample.poc.application.dto.CreateTaskRequest;
import com.sample.poc.application.dto.TaskResponse;
import com.sample.poc.application.dto.UpdateTaskRequest;
import com.sample.poc.application.service.TaskService;
import com.sample.poc.domain.model.Task;
import com.sample.poc.domain.model.User;
import com.sample.poc.domain.repository.TaskRepository;
import com.sample.poc.domain.repository.UserRepository;
import com.sample.poc.presentation.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User testUser;
    private Task testTask;
    private CreateTaskRequest createTaskRequest;
    private UpdateTaskRequest updateTaskRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .build();

        testTask = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .completed(false)
                // .user(testUser)
                .build();

        createTaskRequest = CreateTaskRequest.builder()
                .title("New Task")
                .description("New Description")
                .build();

        updateTaskRequest = UpdateTaskRequest.builder()
                .title("Updated Task")
                .description("Updated Description")
                .completed(true)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                testUser, null, List.of());
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testCreateTask_Success() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        TaskResponse response = taskService.createTask(createTaskRequest);

        assertNotNull(response);
        assertEquals(testTask.getTitle(), response.getTitle());
        assertEquals(testTask.getDescription(), response.getDescription());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testGetTaskById_Success() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        TaskResponse response = taskService.getTaskById(1L);

        assertNotNull(response);
        assertEquals(testTask.getTitle(), response.getTitle());
    }

    @Test
    void testGetTaskById_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void testUpdateTask_Success() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        TaskResponse response = taskService.updateTask(1L, updateTaskRequest);

        assertNotNull(response);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testDeleteTask_Success() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTask_NotFound() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> taskService.deleteTask(1L));
    }

    @Test
    void testCreateTaskWithNoName_Success() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        CreateTaskRequest nullNameRequest = CreateTaskRequest.builder()
                .title(null)
                .description("Sample Description")
                .taskListId(1L)
                .build();

        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(nullNameRequest));
        verify(taskRepository, times(0)).save(any(Task.class));
    }
}
