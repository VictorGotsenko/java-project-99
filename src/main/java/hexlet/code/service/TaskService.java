package hexlet.code.service;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.dto.task.TaskUpdateDTO;

import java.util.List;

public interface TaskService {
    List<TaskDTO> getAll(TaskParamsDTO dto);
    TaskDTO getById(Long id);
    TaskDTO create(TaskCreateDTO dto);
    TaskDTO update(Long id, TaskUpdateDTO dto);
    void delete(Long id);
}
