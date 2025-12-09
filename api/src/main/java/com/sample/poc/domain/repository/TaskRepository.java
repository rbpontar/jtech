package com.sample.poc.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sample.poc.domain.model.Task;
import com.sample.poc.domain.model.TaskList;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // List<Task> findByUser(User user);

    List<Task> findByTaskList(TaskList taskList);
    List<Task> findByTaskListId(Long Id);

    // Optional<Task> findByIdAndUser(Long id, User user);

    // boolean existsByIdAndUser(Long id, User user);
    boolean existsById(Long id);
}
