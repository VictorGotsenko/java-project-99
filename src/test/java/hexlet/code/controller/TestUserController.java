package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;


import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class TestUserController {
    @LocalServerPort
    private int port;

    private Faker faker = new Faker();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User userData;
    private JwtRequestPostProcessor token;

    @Autowired
    private TestRestTemplate testRestTemplate;

    /**
     * Init method.
     */
    @BeforeEach
    public void setUp() {
        userData = new User();
        userData = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();

        userRepository.save(userData);

        token = jwt().jwt(builder -> builder.subject(userData.getEmail()));

    }

/*
    @Test
    @DisplayName("R - Test from workshop")
    void testGetAll() {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setEmail(faker.internet().emailAddress());


        ResponseEntity<UserDTO> userDTOResponseEntity = testRestTemplate
                .getForEntity("http://localhost:" + port + "/api/users/" + userData.getId(),
                        UserDTO.class);

        UserDTO testUserDTO = userDTOResponseEntity.getBody();
        assertNotNull(testUserDTO);

    }

 */

    @Test
    public void testWelcome() throws Exception {
        MvcResult result = mockMvc.perform(get("/welcome"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/users").with(jwt()))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {

        var request = get("/api/users/" + userData.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body).and(
                v -> v.node("email").isEqualTo(userData.getEmail()),
                v -> v.node("firstName").isEqualTo(userData.getFirstName()),
                v -> v.node("lastName").isEqualTo(userData.getLastName()));
    }


    @Test
    public void testCreate() throws Exception {
        UserCreateDTO userCreate = new UserCreateDTO();
        userCreate.setFirstName("John");
        userCreate.setLastName("Doe");
        userCreate.setEmail("john_box@example.com");
        userCreate.setPassword("qwer1y");
        mockMvc.perform(post("/api/users")
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(userCreate)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("John"));
    }

    @Test
    public void testUpdate() throws Exception {
        userRepository.save(userData);
        UserUpdateDTO userUpdate = new UserUpdateDTO();
        userUpdate.setFirstName(JsonNullable.of("Tom"));
        mockMvc.perform(put("/api/users/" + userData.getId())
                        .with(token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(userUpdate)))
                .andExpect(status().isOk());
        User result = userRepository.findById(userData.getId()).get();
        assertThat(result.getFirstName()).isEqualTo(userUpdate.getFirstName().get());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/users/" + userData.getId()).with(token))
                .andExpect(status().isNoContent());
        assertThat(userRepository.existsById(userData.getId())).isFalse();
    }


}

