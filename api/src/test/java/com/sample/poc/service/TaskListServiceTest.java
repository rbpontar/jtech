package com.sample.poc.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sample.poc.application.dto.CreateTaskListRequest;
import com.sample.poc.application.dto.TaskListResponse;
import com.sample.poc.application.dto.UpdateTaskListRequest;
import com.sample.poc.application.service.TaskListService;
import com.sample.poc.domain.model.TaskList;
import com.sample.poc.domain.model.User;
import com.sample.poc.domain.repository.TaskListRepository;
import com.sample.poc.domain.repository.TaskRepository;
import com.sample.poc.domain.repository.UserRepository;
import com.sample.poc.presentation.exception.EmailAlreadyExistsException;
import com.sample.poc.presentation.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TaskListServiceTest {

    @Mock
    private TaskListRepository taskListRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskListService taskListService;

    private User testUser;
    private TaskList testTaskList;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .password("hashedPassword")
                .taskLists(new ArrayList<>())
                .build();

        testTaskList = TaskList.builder()
                .id(1L)
                .name("Work")
                .user(testUser)
                .tasks(new ArrayList<>())
                .build();

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");
        when(authentication.isAuthenticated()).thenReturn(true);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
    }

    @Test
    void testCreateTaskList_Success() {
        CreateTaskListRequest request = CreateTaskListRequest.builder()
                .name("Work")
                .build();

        when(taskListRepository.existsByNameAndUser("Work", testUser)).thenReturn(false);
        when(taskListRepository.save(any(TaskList.class))).thenReturn(testTaskList);
        TaskListResponse response = taskListService.createTaskList(request);

        assertNotNull(response);
        assertEquals("Work", response.getName());
        verify(taskListRepository, times(1)).save(any(TaskList.class));
    }

    @Test
    void testCreateTaskList_NameAlreadyExists() {
        CreateTaskListRequest request = CreateTaskListRequest.builder()
                .name("Work")
                .build();

        when(taskListRepository.existsByNameAndUser("Work", testUser)).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> taskListService.createTaskList(request));
        verify(taskListRepository, never()).save(any(TaskList.class));
    }

    @Test
    void testGetUserTaskLists_Success() {
        List<TaskList> taskLists = List.of(testTaskList);
        when(taskListRepository.findByUser(testUser)).thenReturn(taskLists);

        List<TaskListResponse> response = taskListService.getUserTaskLists();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Work", response.get(0).getName());
    }

    @Test
    void testGetTaskListById_Success() {
        when(taskListRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testTaskList));

        TaskListResponse response = taskListService.getTaskListById(1L);

        assertNotNull(response);
        assertEquals("Work", response.getName());
    }

    @Test
    void testGetTaskListById_NotFound() {
        when(taskListRepository.findByIdAndUser(999L, testUser)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskListService.getTaskListById(999L));
    }

    @Test
    void testUpdateTaskList_Success() {
        UpdateTaskListRequest request = UpdateTaskListRequest.builder()
                .name("Work Updated")
                .build();

        when(taskListRepository.findByIdAndUser(1L, testUser)).thenReturn(Optional.of(testTaskList));
        when(taskListRepository.existsByNameAndUser("Work Updated", testUser)).thenReturn(false);
        when(taskListRepository.save(any(TaskList.class))).thenReturn(testTaskList);

        TaskListResponse response = taskListService.updateTaskList(1L, request);

        assertNotNull(response);
        verify(taskListRepository, times(1)).save(any(TaskList.class));
    }

    @Test
    void testDeleteTaskList_Success() {
        assertDoesNotThrow(() -> taskListService.deleteTaskList(1L));
        verify(taskListRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTaskList_NotFound() {
        assertDoesNotThrow(() -> taskListService.deleteTaskList(999L));
        verify(taskListRepository, times(1)).deleteById(999L);
    }

    @Test
    void testCreateTaskListWithNoName_Success() {
        CreateTaskListRequest nullNameRequest = CreateTaskListRequest.builder()
            .name(null)
            .build();

        assertThrows(IllegalArgumentException.class, () -> taskListService.createTaskList(nullNameRequest));
        verify(taskListRepository, times(0)).save(any(TaskList.class));
    }
}
