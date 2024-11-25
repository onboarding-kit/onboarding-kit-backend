package com.example.common;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = CommonServiceApplication.class)
@SpringBootTest
public class TestFixtureTests {

    @Test
    void contestLoad() {
        System.out.println("hey");
    }
}
