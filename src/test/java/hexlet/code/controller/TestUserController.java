package hexlet.code.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.AuthRequest;
import hexlet.code.dto.user.UserCreateDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.dto.user.UserUpdateDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.JWTUtils;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.instancio.Select;
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


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.assertj.core.api.Assertions.assertThat;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class TestUserController {
    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    private Faker faker = new Faker();
    private User testUser;
    private String token;

    /**
     * Init method.
     */
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        testUser = new User();
        testUser = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .create();
        userRepository.save(testUser);
        token = jwtUtils.generateToken("hexlet@example.com");
    }

    @Test
    @DisplayName("R - Test Welcome endpoint")
    void testWelcome() {
        ResponseEntity<String> response = testRestTemplate
                .getForEntity("http://localhost:" + port + "/welcome", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Welcome to Spring");
        assertThat(response.getHeaders().getContentType().toString()).contains("text/plain");
    }

    @Test
    @DisplayName("L - Test Login endpoint")
    void testLogin() throws Exception {
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
        String msg = " -=Not found=- User with id: ";
        Long noId = 999L;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String reqUrl = "http://localhost:" + port + "/api/users/" + noId;
        ResponseEntity<String> response = testRestTemplate
                .exchange(reqUrl, HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertNotNull(response.getBody());
        assertEquals(response.getBody(), msg + noId);
    }

    @Test
    @DisplayName("R - Test get all")
    void testIndex() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String reqUrl = "http://localhost:" + port + "/api/users";
        ResponseEntity<String> response = testRestTemplate
                .exchange(reqUrl, HttpMethod.GET, entity, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        String body = response.getBody();
        List<UserDTO> userDTOS = objectMapper.readValue(body, new TypeReference<>() {
        });
        var actual = userDTOS.stream().map(userMapper::map).toList();
        var expected = userRepository.findAll();

        Assertions.assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    @DisplayName("C - Create user")
    void testCreate() throws Exception {
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
    void testUpdate() throws Exception {
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
    void testDelete() {
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



