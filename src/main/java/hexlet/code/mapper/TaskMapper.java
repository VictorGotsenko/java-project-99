package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
@SuppressWarnings("java:S6813")
public abstract class TaskMapper {
    @Autowired
    private  TaskStatusRepository taskStatusRepository;
    @Autowired
    private  LabelRepository labelRepository;


    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "labels", source = "taskLabelIds") //, qualifiedByName = "mapLabels")
    public abstract Task map(TaskCreateDTO dto);

    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "taskLabelIds", source = "labels") // , qualifiedByName = "mapTaskLabel")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "labels", source = "taskLabelIds")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);

    /**
     * @param statusSlug
     * @return TaskStatus
     */
    public TaskStatus toTaskStatus(String statusSlug) {
        return taskStatusRepository.findBySlug(statusSlug)
                .orElseThrow();
    }

    /**
     * @param taskLabelIds
     * @return Set
     */
    public Set<Label> toLabels(Set<Long> taskLabelIds) {
        if (taskLabelIds == null) {
            return new HashSet<>();
        } else {
            Set<Label> result = new HashSet<>();
            List<Label> listLabels = labelRepository.findAll();
            for (Long n : taskLabelIds) {
                Optional<Label> value = listLabels.stream()
                        .filter(v -> (v.getId() == n))
                        .findFirst();
                if (value.isPresent()) {
                    result.add(value.get());
                }
            }
            return result;
        }
    }

    /**
     * @param labels
     * @return Set
     */
    public Set<Long> toTaskLabelIds(Set<Label> labels) {
        if (labels == null) {
            return new HashSet<>();
        } else {
            return labels.stream()
                    .map(Label::getId)
                    .collect(Collectors.toSet());

        }
    }
}
