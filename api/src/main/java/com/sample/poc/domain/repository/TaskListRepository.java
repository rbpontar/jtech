package com.sample.poc.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sample.poc.domain.model.TaskList;
import com.sample.poc.domain.model.User;

@Repository
public interface TaskListRepository extends JpaRepository<TaskList, Long> {
    List<TaskList> findByUser(User user);

    Optional<TaskList> findByIdAndUser(Long id, User user);

    boolean existsByIdAndUser(Long id, User user);

    boolean existsByNameAndUser(String name, User user);

    Optional<TaskList> findByNameAndUser(String name, User user);
}
