package ru.bolnik.dima.task.tracker.api.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import ru.bolnik.dima.task.tracker.api.dto.AckDto;
import ru.bolnik.dima.task.tracker.api.dto.ProjectDto;
import ru.bolnik.dima.task.tracker.api.exceptions.BadRequestException;
import ru.bolnik.dima.task.tracker.api.exceptions.NotFoundException;
import ru.bolnik.dima.task.tracker.api.factories.ProjectDtoFactory;
import ru.bolnik.dima.task.tracker.store.entities.ProjectEntity;
import ru.bolnik.dima.task.tracker.store.repositories.ProjectRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class ProjectController {

    ProjectRepository projectRepository;
    ProjectDtoFactory projectDtoFactory;

    public static final String FETCH_PROJECTS = "/api/projects";
    public static final String DELETE_PROJECT = "/api/projects/{project_id}";

    public static final String CREATE_OR_UPDATE_PROJECT = "/api/projects";


    @PutMapping(CREATE_OR_UPDATE_PROJECT)
    public ProjectDto createOrUpdateProject(
            @RequestParam(value = "project_id", required = false) Optional<Long> optionalProjectId,
            @RequestParam(value = "project_name", required = false) Optional<String> optionalProjectName ) {

        boolean isCreate = !optionalProjectId.isPresent();
        if (isCreate && !optionalProjectId.isPresent()) {
            throw new BadRequestException("Project name cannot be empty");
        }

        optionalProjectName = optionalProjectName
                .filter(projectName -> !projectName.trim().isEmpty());

        final ProjectEntity project = optionalProjectId
                .map(this::getProjectOrThrowException)
                .orElseGet(() -> ProjectEntity.builder().build());


        optionalProjectName
                .ifPresent(projectName -> {
                    projectRepository
                            .findByName(projectName)
                            .filter(anotherProject -> !Objects.equals(anotherProject.getId(), project.getId()))
                            .ifPresent(anotherProject -> {
                                throw new BadRequestException(String.format("Project \"%s\" already exists", projectName));


                            });
                    project.setName(projectName);
                });

        final ProjectEntity savedProject = projectRepository.saveAndFlush(project);


        return projectDtoFactory.makeProjectDto(savedProject);
    }


    @GetMapping(FETCH_PROJECTS)
    public List<ProjectDto> fetchProjects(
            @RequestParam(value = "prefix_name", required = false) Optional<String> optionalPrefixName) {

        optionalPrefixName = optionalPrefixName
                .filter(prefix -> !prefix.trim().isEmpty());

        Stream<ProjectEntity> projectStream = optionalPrefixName
                .map(projectRepository::streamAllByNameStartsWithIgnoreCase)
                .orElseGet(projectRepository::streamAllBy);

        if (optionalPrefixName.isPresent()) {
            projectStream = projectRepository
                    .streamAllByNameStartsWithIgnoreCase(optionalPrefixName.get());
        } else {
            projectStream = projectRepository.streamAllBy();
        }

        return projectStream
                .map(projectDtoFactory::makeProjectDto)
                .collect(Collectors.toList());
    }


    @DeleteMapping(DELETE_PROJECT)
    public AckDto deleteProject(@PathVariable("project_id") Long projectId) {

        getProjectOrThrowException(projectId);

        projectRepository.deleteById(projectId);

        return AckDto.makeDefault(true);

    }

    private ProjectEntity getProjectOrThrowException(Long projectId) {
        return projectRepository
                .findById(projectId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Project with \"$s\" not found",
                                        projectId)));
    }

}
