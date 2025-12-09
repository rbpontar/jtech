package com.sample.poc.application.dto;

import java.time.LocalDateTime;

import com.sample.poc.domain.model.TaskList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskListResponse {

  private Long id;
  private String name;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Integer taskCount;

  public static TaskListResponse fromTaskList(TaskList taskList) {
    return TaskListResponse.builder()
        .id(taskList.getId())
        .name(taskList.getName())
        .createdAt(taskList.getCreatedAt())
        .updatedAt(taskList.getUpdatedAt())
        .taskCount(taskList.getTasks() != null ? taskList.getTasks().size() : 0)
        .build();
  }
}
