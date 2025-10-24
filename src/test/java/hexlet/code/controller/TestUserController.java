package hexlet.code.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TestUserController {

    @Test
    public void testWelcome() {
        assertThat(true).isTrue();
    }
}

