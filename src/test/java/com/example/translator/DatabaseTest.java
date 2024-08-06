package com.example.translator;

import com.example.translator.repository.TranslationRepository;
import com.example.translator.repository.jdbc.JdbcTranslationRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
public class DatabaseTest {

    TranslationRepository repository;
    JdbcTemplate jdbcTemplate;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine").withInitScript("db/changelog/init_tables.sql");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }


    @BeforeAll
    static void init() {
        postgres.start();
    }

    @AfterAll
    static void destroy() {
        postgres.stop();
    }

    @Test
    @Transactional
    @Rollback
    public void saveNoteTest() {
        jdbcTemplate = new JdbcTemplate(DataSourceBuilder.create()
                .url(postgres.getJdbcUrl())
                .password(postgres.getPassword())
                .username(postgres.getUsername())
                .build());

        repository = new JdbcTranslationRepository(jdbcTemplate);

        String text = "Hello, world!";
        String translated = "Привет, мир!";
        String ip = "192.168.123.132";

        int was = jdbcTemplate.queryForObject("SELECT count(*) FROM note", Integer.class);
        repository.saveTranslation(ip, text, translated);
        int count = jdbcTemplate.queryForObject("SELECT count(*) FROM note", Integer.class);

        Assertions.assertThat(count - was).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    public void doubleSaveNoteTest() {
        jdbcTemplate = new JdbcTemplate(DataSourceBuilder.create()
                .url(postgres.getJdbcUrl())
                .password(postgres.getPassword())
                .username(postgres.getUsername())
                .build());

        repository = new JdbcTranslationRepository(jdbcTemplate);

        String text = "Hello, world!";
        String translated = "Привет, мир!";
        String ip = "192.168.123.132";

        int was = jdbcTemplate.queryForObject("SELECT count(*) FROM note", Integer.class);
        repository.saveTranslation(ip, text, translated);
        repository.saveTranslation(ip, text, translated);
        int count = jdbcTemplate.queryForObject("SELECT count(*) FROM note", Integer.class);

        Assertions.assertThat(count - was).isEqualTo(2);
    }
}
