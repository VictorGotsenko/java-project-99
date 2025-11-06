package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.exeption.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class TestTaskStatusController {
    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    private TaskStatus taskStatus;

    /**
     * setUp.
     */
    @BeforeEach
        void setUp() {
        taskStatusRepository.deleteAll();
        taskStatus = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .ignore(Select.field((TaskStatus::getCreatedAt)))
                .create();
        taskStatusRepository.save(taskStatus);
    }

    @Test
    @DisplayName("R - Test get all")
    public void testIndex() throws Exception {
        Map<String, String> taskStatuses = new HashMap<>();
        taskStatuses.put("Dra", "dra");
        taskStatuses.put("ToRev", "to_rev");
        for (Map.Entry<String, String> entry : taskStatuses.entrySet()) {
            TaskStatus taskStatus = new TaskStatus();
            taskStatus.setName(entry.getKey());
            taskStatus.setSlug(entry.getValue());
            taskStatusRepository.save(taskStatus);
        }
        MvcResult result = mockMvc.perform(get("/api/task_statuses").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    @DisplayName("R - Test get by Id")
    public void testShow() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/task_statuses/" + taskStatus.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();
        assertThatJson(body).and(t -> t.node("name").isEqualTo(taskStatus.getName()));
    }

    @Test
    @DisplayName("R - Test get by No Id")
    public void testShowNoId() throws Exception {
        Long noId = 999L;
        mockMvc.perform(get("/api/task_statuses/" + noId).with(jwt()))
                .andExpect(status().isNotFound())
                .andExpect(mvcResult -> mvcResult.getResolvedException()
                        .getClass().equals(ResourceNotFoundException.class));

    }

    @Test
    @DisplayName("C - Create task_statuses")
    public void testCreate() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("name", "Reserve");
        data.put("slug", "reserve");

        MvcResult mvcResult = mockMvc.perform(post("/api/task_statuses").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isCreated())
                .andReturn();

        TaskStatus task = taskStatusRepository.findBySlug("reserve").get();

        String body = mvcResult.getResponse().getContentAsString();
        assertThatJson(body).and(t -> t.node("name").isEqualTo("Reserve"));
        assertThat(task.getCreatedAt()).isNotNull();
        assertThat(task.getId()).isNotNull();
    }

    @Test
    @DisplayName("U - Update task_statuses")
    public void testUpdate() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("name", "Updated");

        MvcResult mvcResult = mockMvc.perform(put("/api/task_statuses/" + taskStatus.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();
        assertThatJson(body).and(t -> t.node("name").isEqualTo("Updated"));
    }

    @Test
    @DisplayName("D - Delete task_statuses")
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/task_statuses/" + taskStatus.getId()).with(jwt()))
                .andExpect(status().isNoContent());
        assertThat(taskStatusRepository.existsById(taskStatus.getId())).isFalse();
    }
}
