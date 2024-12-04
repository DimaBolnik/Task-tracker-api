package ru.bolnik.dima.task.tracker.api.factories;


import org.springframework.stereotype.Component;
import ru.bolnik.dima.task.tracker.api.dto.TaskDto;
import ru.bolnik.dima.task.tracker.store.entities.TaskEntity;

@Component
public class TaskDtoFactory {

    public TaskDto makeTaskDto(TaskEntity entity) {

        return TaskDto.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

}
