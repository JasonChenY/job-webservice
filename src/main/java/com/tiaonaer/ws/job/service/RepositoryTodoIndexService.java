package com.tiaonaer.ws.job.service;

import com.tiaonaer.ws.job.document.TodoDocument;
import com.tiaonaer.ws.job.model.Todo;
import com.tiaonaer.ws.job.repository.solr.TodoDocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jason.y.chen
 */
@Service
public class RepositoryTodoIndexService implements TodoIndexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTodoIndexService.class);

    @Resource
    private TodoDocumentRepository repository;

    @Transactional
    @Override
    public void addToIndex(Todo todoEntry) {
        LOGGER.debug("Saving a todo entry with information: {}", todoEntry);
        TodoDocument document = TodoDocument.getBuilder(todoEntry.getId(), todoEntry.getTitle())
                .description(todoEntry.getDescription())
                .build();

        LOGGER.debug("Saving document with information: {}", document);

        repository.save(document);
    }

    @Override
    public long countSearchResults(String searchTerm) {
        LOGGER.debug("Getting search result count for search term: {}", searchTerm);
        return repository.count(searchTerm);
    }

    @Transactional
    @Override
    public void deleteFromIndex(Long id) {
        LOGGER.debug("Deleting an existing document with id: {}", id);
        repository.delete(id.toString());
    }

    @Override
    public List<TodoDocument> search(String searchTerm, Pageable page) {
        LOGGER.debug("Searching documents with search term: {} and page: {}", searchTerm, page);
        return repository.search(searchTerm, page);
    }

    @Transactional
    @Override
    public void update(Todo todoEntry) {
        LOGGER.debug("Updating the information of a todo entry: {}", todoEntry);
        repository.update(todoEntry);
    }
}
