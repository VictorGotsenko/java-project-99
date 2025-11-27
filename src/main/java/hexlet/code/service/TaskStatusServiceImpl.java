package hexlet.code.service;

import hexlet.code.dto.taskstatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskstatus.TaskStatusDTO;
import hexlet.code.dto.taskstatus.TaskStatusUpdateDTO;
import hexlet.code.exeption.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {
    private final TaskStatusRepository taskStatusRepository;
    private final TaskStatusMapper taskStatusMapper;

    /**
     * @return List<TaskStatusDTO>
     */
    @Override
    public List<TaskStatusDTO> getAll() {
        return taskStatusRepository.findAll().stream()
                .map(taskStatusMapper::map)
                .toList();
    }

    /**
     * @param id
     * @return TaskStatusDTO
     */
    @Override
    public TaskStatusDTO getById(Long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(" -=Not found=- Task Status with id: " + id));
        return taskStatusMapper.map(taskStatus);
    }

    /**
     * @param dto
     * @return TaskStatusDTO
     */
    @Override
    public TaskStatusDTO create(TaskStatusCreateDTO dto) {
        TaskStatus taskStatus = taskStatusMapper.map(dto);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);
    }

    /**
     * @param id
     * @param dto
     * @return List<TaskStatusDTO>
     */
    @Override
    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO dto) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task Status with id " + id + " not found"));
        taskStatusMapper.update(dto, taskStatus);
        taskStatusRepository.save(taskStatus);
        return taskStatusMapper.map(taskStatus);
    }

    /**
     * @param id
     */
    @Override
    public void delete(Long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task Status with id " + id + " not found"));
        taskStatusRepository.delete(taskStatus);
    }
}
