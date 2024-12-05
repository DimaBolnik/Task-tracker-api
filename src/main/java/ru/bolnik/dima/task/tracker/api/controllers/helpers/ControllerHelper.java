package ru.bolnik.dima.task.tracker.api.controllers.helpers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import ru.bolnik.dima.task.tracker.api.exceptions.NotFoundException;
import ru.bolnik.dima.task.tracker.store.entities.ProjectEntity;
import ru.bolnik.dima.task.tracker.store.repositories.ProjectRepository;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Transactional
public class ControllerHelper {

    ProjectRepository projectRepository;

    public ProjectEntity getProjectOrThrowException(Long projectId) {
        return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Project with \"$s\" not found",
                                        projectId)));
    }
}
