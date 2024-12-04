package ru.bolnik.dima.task.tracker.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bolnik.dima.task.tracker.store.entities.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
}
