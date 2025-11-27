package hexlet.code.service;

import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskParamsDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exeption.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskSpecification taskSpecification;

    /**
     * @param dto
     * @return List<TaskDTO>
     */
    @Override
    public List<TaskDTO> getAll(TaskParamsDTO dto) {
        var spec = taskSpecification.build(dto);
        return taskRepository.findAll(spec).stream()
                .map(taskMapper::map)
                .toList();
    }

    /**
     * @param id
     * @return TaskDTO
     */
    @Override
    public TaskDTO getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(" -=Not found=- Task with id: " + id));
        return taskMapper.map(task);
    }

    /**
     * @param dto
     * @return TaskDTO
     */
    @Override
    public TaskDTO create(TaskCreateDTO dto) {
        Task task = taskMapper.map(dto);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    /**
     * @param id
     * @param dto
     * @return TaskDTO
     */
    @Override
    public TaskDTO update(Long id, TaskUpdateDTO dto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        taskMapper.update(dto, task);
        taskRepository.save(task);
        return taskMapper.map(task);

    }

    /**
     * @param id
     */
    @Override
    public void delete(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found"));
        taskRepository.delete(task);
    }
}
