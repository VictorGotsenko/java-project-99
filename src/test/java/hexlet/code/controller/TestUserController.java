package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.AuthRequest;
import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.JWTUtils;
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
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import static org.assertj.core.api.Assertions.assertThat;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
public class TestUserController {
    @LocalServerPort
    private int port;

    private Faker faker = new Faker();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private String token;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Init method.
     */
    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .create();

        userRepository.save(testUser);
        token = jwtUtils.generateToken("hexlet@example.com");
    }

    /**
     * afterEach.
     */
    @AfterEach
    public void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("R - Test Welcome endpoint")
    public void testWelcome() throws Exception {
        ResponseEntity<String> response = testRestTemplate
                .getForEntity("http://localhost:" + port + "/welcome", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Welcome to Spring");
        assertThat(response.getHeaders().getContentType().toString()).contains("text/plain");
    }

    @Test
    @DisplayName("L - Test Login endpoint")
    public void testLogin() throws Exception {
        User userData = new User();
        userData.setEmail("hexlet@example.com");
        String passDigist = passwordEncoder.encode("qwerty");
        userData.setPasswordDigest(passDigist);
        userData.setFirstName("admin");
        userData.setLastName("root");
        userRepository.save(userData);
        AuthRequest auDTO = new AuthRequest();
        auDTO.setUsername("hexlet@example.com");
        auDTO.setPassword("qwerty");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        String body = objectMapper.writeValueAsString(auDTO);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = testRestTemplate
                .postForEntity("http://localhost:" + port + "/api/login",
                        entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("R - Test get by Id")
    void testShow() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String reqUrl = "http://localhost:" + port + "/api/users/" + testUser.getId();
        ResponseEntity<String> response = testRestTemplate
                .exchange(reqUrl, HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertThatJson(response.getBody()).and(t -> t.node("firstName").isEqualTo(testUser.getFirstName()));
    }

    @Test
    @DisplayName("R - Test get by No Id ")
    void testShowNoIdUres() {
        Long noId = 999L;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String reqUrl = "http://localhost:" + port + "/api/users/" + noId;
        ResponseEntity<String> response = testRestTemplate
                .exchange(reqUrl, HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertNotNull(response.getBody());
        assertEquals(response.getBody().toString(), "User with id " + noId + " not found");
    }

    @Test
    @DisplayName("R - Test get all")
    public void testIndex() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String reqUrl = "http://localhost:" + port + "/api/users";
        ResponseEntity<String> response = testRestTemplate
                .exchange(reqUrl, HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertNotNull(response.getBody());
        assertThatJson(response.getBody()).isArray();
    }

    @Test
    @DisplayName("C - Create user")
    public void testCreate() throws Exception {
        UserCreateDTO userCreate = new UserCreateDTO();
        userCreate.setFirstName("John");
        userCreate.setLastName("Doe");
        userCreate.setEmail("john_box@example.com");
        userCreate.setPassword("qwer1y");
        String body = objectMapper.writeValueAsString(userCreate);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = testRestTemplate
                .postForEntity("http://localhost:" + port + "/api/users",
                        entity,
                        String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThatJson(response.getBody()).and(t -> t.node("firstName").isEqualTo(userCreate.getFirstName()));
    }

    @Test
    @DisplayName("U - Update user")
    public void testUpdate() throws Exception {
        UserUpdateDTO userUpdate = new UserUpdateDTO();
        userUpdate.setFirstName(JsonNullable.of("Tom"));
        userUpdate.setEmail(JsonNullable.of("Tommy@example.com"));
        String body = objectMapper.writeValueAsString(userUpdate);

        // login testUser
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtUtils.generateToken(testUser.getEmail()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = testRestTemplate
                .exchange("http://localhost:" + port + "/api/users/" + testUser.getId(),
                        HttpMethod.PUT, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThatJson(response.getBody()).and(t -> t.node("firstName")
                .isEqualTo(userUpdate.getFirstName().get()));
    }

    @Test
    @DisplayName("D - Delete user")
    public void testDelete() throws Exception {
        // login testUser
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtUtils.generateToken(testUser.getEmail()));

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = testRestTemplate
                .exchange("http://localhost:" + port + "/api/users/" + testUser.getId(),
                        HttpMethod.DELETE, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(userRepository.existsById(testUser.getId())).isFalse();

    }
}



