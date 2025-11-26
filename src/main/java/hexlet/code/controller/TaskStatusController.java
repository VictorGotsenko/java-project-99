package hexlet.code.controller;


import hexlet.code.dto.taskstatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskstatus.TaskStatusDTO;
import hexlet.code.dto.taskstatus.TaskStatusUpdateDTO;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.TaskStatusServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
@AllArgsConstructor
public class TaskStatusController {
    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusServiceImpl taskStatusServiceImpl;

    /**
     * @return List<TaskStatusDTO>
     */
    @GetMapping(path = "")
    public ResponseEntity<List<TaskStatusDTO>> index() {
        var result = taskStatusServiceImpl.getAll();
        return ResponseEntity.ok().header("X-Total-Count", String.valueOf(result.size())).body(result);
    }

    /**
     * @param id
     * @return TaskStatusDTO
     */
    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDTO show(@PathVariable long id) {
        return taskStatusServiceImpl.getById(id);
    }

    /**
     * @param dto
     * @return TaskStatusDTO
     */
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskStatusDTO create(@Valid @RequestBody TaskStatusCreateDTO dto) {
        return taskStatusServiceImpl.create(dto);
    }

    /**
     * @param id
     * @param dto
     * @return TaskStatusDTO
     */
    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskStatusDTO update(@PathVariable("id") Long id, @Valid @RequestBody TaskStatusUpdateDTO dto) {
        return taskStatusServiceImpl.update(id, dto);
    }

    /**
     * @param id
     */
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        taskStatusServiceImpl.delete(id);
    }
}
