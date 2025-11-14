package hexlet.code.specification;

import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TaskSpecification {

    /**
     * @param dto
     * @return Specification<Task>
     */
    public Specification<Task> build(TaskParamsDTO dto) {
        return withAssigneeId(dto.getAssigneeId())
                .and(withTitleCont(dto.getTitleCont()))
                .and(withStatus(dto.getStatus()))
                .and(withLabelId(dto.getLabelId()));
    }

    private Specification<Task> withAssigneeId(Long assigneeId) {
        return (root, query, criteriaBuilder) ->
                assigneeId == null ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("assignee").get("id"), assigneeId);
    }

    private Specification<Task> withTitleCont(String titleCont) {
        return (root, query, criteriaBuilder) ->
                titleCont == null ? criteriaBuilder.conjunction()
                        : criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + titleCont.toLowerCase() + "%");
    }

    private Specification<Task> withStatus(String status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("taskStatus").get("slug"), status);
    }

    private Specification<Task> withLabelId(Long labelId) {
        return (root, query, criteriaBuilder) ->
                labelId == null ? criteriaBuilder.conjunction()
                        : criteriaBuilder.equal(root.get("labels").get("id"), labelId);
    }

}
