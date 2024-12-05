package ru.bolnik.dima.task.tracker.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bolnik.dima.task.tracker.store.entities.TaskStateEntity;

import java.util.Optional;

public interface TaskStateRepository extends JpaRepository<TaskStateEntity, Long> {

    Optional<TaskStateEntity> findTaskStateEntityByProjectIdAndNameContainsIgnoreCase(Long projectId, String taskStateName);
}
