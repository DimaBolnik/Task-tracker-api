package ru.bolnik.dima.task.tracker.api.factories;


import org.springframework.stereotype.Component;
import ru.bolnik.dima.task.tracker.api.dto.ProjectDto;
import ru.bolnik.dima.task.tracker.api.dto.TaskStateDto;
import ru.bolnik.dima.task.tracker.store.entities.ProjectEntity;
import ru.bolnik.dima.task.tracker.store.entities.TaskStateEntity;

@Component
public class TaskStateDtoFactory {

    public TaskStateDto makeTaskStateDto(TaskStateEntity entity) {

        return TaskStateDto.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .name(entity.getName())
                .ordinal(entity.getOrdinal())
                .build();
    }

}
