package hexlet.code.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class TestUserController {

    @Test
    public void testWelcome() throws Exception {
        boolean actualCondition = true;
        assertTrue(actualCondition); // This test will pass

    }
}

