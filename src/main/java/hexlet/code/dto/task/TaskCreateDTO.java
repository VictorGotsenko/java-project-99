package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {

    @NotNull
    @Size(min = 1)
    String title;

    String content;
    Integer index;

    @NotNull
    @JsonProperty("status")
    String status;

    @JsonProperty("assignee_id")
    Long assigneeId;

    private Set<Long> taskLabelIds;


}
