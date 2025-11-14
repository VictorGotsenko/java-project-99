package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.AfterEach;
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

import java.util.HashMap;
import java.util.Map;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class TestLabelController {
    @LocalServerPort
    private int port;

    @Autowired
    private  MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private LabelRepository labelRepository;

    private Label testLabel;

    /**
     * Init.
     */
    @BeforeEach
    public void initTests() {
        testLabel = Instancio.of(Label.class)
                .ignore(Select.field(Label::getId))
                .set(Select.field("name"), "testLabel")
                .ignore(Select.field((Label::getCreatedAt)))
                .create();
        labelRepository.save(testLabel);
    }

    /**
     * afterEach.
     */
    @AfterEach
    public void clean() {
        labelRepository.deleteAll();
    }

    @Test
    @DisplayName("R - Test get all")
    public void testIndex() throws Exception {

        MvcResult result = mockMvc.perform(get("/api/labels").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();

    }

    @Test
    @DisplayName("R - Test get by Id")
    public void testShow() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/labels/" + testLabel.getId()).with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();
        assertThatJson(body).and(t -> t.node("name").isEqualTo(testLabel.getName()));
    }

    @Test
    @DisplayName("C - Create task_statuses")
    public void testCreate() throws Exception {
        Map<String, String> data = new HashMap<>();
        data.put("name", "LaLabel");

        MvcResult mvcResult = mockMvc.perform(post("/api/labels").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isCreated())
                .andReturn();
        Label label = labelRepository.findByName("LaLabel").get();

        String body = mvcResult.getResponse().getContentAsString();
        assertThatJson(body).and(t -> t.node("name").isEqualTo(data.get("name")));
        assertThat(label.getCreatedAt()).isNotNull();
        assertThat(label.getId()).isNotNull();
    }

    @Test
    @DisplayName("U - Update task_statuses")
    public void testUpdate() throws Exception {

        Map<String, String> data = new HashMap<>();
        data.put("name", "Updated");

        MvcResult mvcResult = mockMvc.perform(put("/api/labels/" + testLabel.getId()).with(jwt())
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isOk())
                .andReturn();

        String body = mvcResult.getResponse().getContentAsString();
        assertThatJson(body).and(t -> t.node("name").isEqualTo("Updated"));
    }

    @Test
    @DisplayName("D - Delete task_statuses")
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/labels/" + testLabel.getId()).with(jwt()))
                .andExpect(status().isNoContent());
        assertThat(labelRepository.existsById(testLabel.getId())).isFalse();
    }

}
