package ru.bolnik.dima.task.tracker.api.factories;


import org.springframework.stereotype.Component;
import ru.bolnik.dima.task.tracker.api.dto.ProjectDto;
import ru.bolnik.dima.task.tracker.store.entities.ProjectEntity;

@Component
public class ProjectDtoFactory {

    public ProjectDto makeProjectDto(ProjectEntity entity) {

        return ProjectDto.builder()
                .id(entity.getId())
                .createdAt(entity.getCreatedAt())
                .name(entity.getName())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

}
