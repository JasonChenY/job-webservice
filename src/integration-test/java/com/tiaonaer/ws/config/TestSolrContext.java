package com.tiaonaer.ws.config;

import com.tiaonaer.ws.todo.repository.solr.TodoDocumentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.mock;

/**
 * @author Petri Kainulainen
 */
@Configuration
@Profile("test")
public class TestSolrContext {

    @Bean
    public TodoDocumentRepository todoDocumentRepository() {
        return mock(TodoDocumentRepository.class);
    }
}
