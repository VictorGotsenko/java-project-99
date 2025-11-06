package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.task.TaskCreateDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.TaskUpdateDTO;
import hexlet.code.exeption.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class TestTaskController {
    @LocalServerPort
    private int port;

    private Faker faker = new Faker();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Autowired
    TaskMapper taskMapper;

    private Task testTask;
    private TaskStatus taskStatus;


    /**
     * setUp.
     */
    @BeforeEach
    void setUp() {
        taskStatus = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .supply(Select.field(TaskStatus::getName), () -> "Move1")
                .supply(Select.field(TaskStatus::getSlug), () -> "to_move1")
                .ignore(Select.field((TaskStatus::getCreatedAt)))
                .create();
        taskStatusRepository.save(taskStatus);
        testTask = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getName), () -> faker.gameOfThrones().house())
                .ignore(Select.field(Task::getIndex))
                .supply(Select.field(Task::getTaskStatus), () -> taskStatus)
                .ignore(Select.field(Task::getAssignee))
                .ignore(Select.field(Task::getCreatedAt))
                .create();
        taskRepository.save(testTask);
    }

    /**
     * afterEach.
     */
    @AfterEach
    public void clean() {
        taskRepository.deleteAll();
        taskStatusRepository.deleteAll();
    }

    @Test
    @DisplayName("R - Test get all")
    public void testIndex() throws Exception {
        List<TaskDTO> expectedStatuses = taskRepository.findAll().stream()
                .map(taskMapper::map)
                .toList();
        MvcResult result = mockMvc.perform(get("/api/tasks").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        String body = result.getResponse().getContentAsString();

        List<TaskDTO> listTasks = objectMapper.readValue(body, new TypeReference<>() {
        });

        assertThat(listTasks.size()).isEqualTo(expectedStatuses.size());
    }

    @Test
    @DisplayName("R - Test get by Id")
    public void testShow() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/tasks/" + testTask.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();
        String body = mvcResult.getResponse().getContentAsString();
        assertThatJson(body).and(t -> t.node("title").isEqualTo(testTask.getName()));
    }

    @Test
    @DisplayName("R - Test get by No Id")
    public void testShowNoId() throws Exception {
        Long noId = 999L;
        mockMvc.perform(get("/api/tasks/" + noId).with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException()
                        .getClass().equals(ResourceNotFoundException.class));
    }

    @Test
    @DisplayName("C - Create task")
    public void testCreate() throws Exception {
        TaskCreateDTO taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setTitle("Test title");
        taskCreateDTO.setContent("Test content");
        taskCreateDTO.setStatus(testTask.getTaskStatus().getSlug());
        MvcResult mvcResult = mockMvc.perform(post("/api/tasks").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreateDTO)))
                .andExpect(status().isCreated())
                .andReturn();
        String body = mvcResult.getResponse().getContentAsString();
        assertThatJson(body).and(t -> t.node("title").isEqualTo(taskCreateDTO.getTitle()));
    }


    @Test
    @DisplayName("U - Update task")
    public void testUpdate() throws Exception {
        TaskUpdateDTO updateDTO = new TaskUpdateDTO();
        updateDTO.setTitle(JsonNullable.of("Updated title"));
        updateDTO.setContent(JsonNullable.of("Description for task"));

        MvcResult mvcResult = mockMvc.perform(put("/api/tasks/" + testTask.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();
        assertThatJson(body).and(t -> t.node("title").isEqualTo(updateDTO.getTitle().get()));
    }

    @Test
    @DisplayName("D - Delete task")
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId()).with(jwt()))
                .andExpect(status().isNoContent());
        assertThat(taskStatusRepository.existsById(testTask.getId())).isFalse();
    }


}
