package com.tiaonaer.ws.job.service;

import com.tiaonaer.ws.job.document.TodoDocument;
import com.tiaonaer.ws.job.dto.TodoDTO;
import com.tiaonaer.ws.job.exception.TodoNotFoundException;
import com.tiaonaer.ws.job.model.Todo;
import com.tiaonaer.ws.job.repository.jpa.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author jason.y.chen
 */
@Service
public class RepositoryTodoService implements TodoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryTodoService.class);

    @Resource
    private TodoIndexService indexService;

    @Resource
    private TodoRepository repository;

    @PreAuthorize("hasPermission('Todo', 'add')")
    @Transactional
    @Override
    public Todo add(TodoDTO added) {
        LOGGER.debug("Adding a new to-do entry with information: {}", added);

        Todo model = Todo.getBuilder(added.getTitle())
                .description(added.getDescription())
                .build();

        Todo persisted = repository.save(model);
        indexService.addToIndex(persisted);

        return persisted;
    }

    @PreAuthorize("hasPermission('Todo', 'search')")
    @Override
    public long countSearchResults(String searchTerm) {
        LOGGER.debug("Getting search result count for search term: {}", searchTerm);
        return indexService.countSearchResults(searchTerm);
    }

    @PreAuthorize("hasPermission('Todo', 'delete')")
    @Transactional(rollbackFor = {TodoNotFoundException.class})
    @Override
    public Todo deleteById(Long id) throws TodoNotFoundException {
        LOGGER.debug("Deleting a to-do entry with id: {}", id);

        Todo deleted = findById(id);
        LOGGER.debug("Deleting to-do entry: {}", deleted);

        repository.delete(deleted);
        indexService.deleteFromIndex(id);

        return deleted;
    }

    @PreAuthorize("hasPermission('Todo', 'list')")
    @Transactional(readOnly = true)
    @Override
    public List<Todo> findAll() {
        LOGGER.debug("Finding all to-do entries");
        return repository.findAll();
    }

    @PreAuthorize("hasPermission('Todo', 'find')")
    @Transactional(readOnly = true, rollbackFor = {TodoNotFoundException.class})
    @Override
    public Todo findById(Long id) throws TodoNotFoundException {
        LOGGER.debug("Finding a to-do entry with id: {}", id);

        Todo found = repository.findOne(id);
        LOGGER.debug("Found to-do entry: {}", found);

        if (found == null) {
            throw new TodoNotFoundException("No to-entry found with id: " + id);
        }

        return found;
    }

    @PreAuthorize("hasPermission('Todo', 'update')")
    @Transactional(rollbackFor = {TodoNotFoundException.class})
    @Override
    public Todo update(TodoDTO updated) throws TodoNotFoundException {
        LOGGER.debug("Updating todo entry with information: {}", updated);

        Todo model = findById(updated.getId());
        LOGGER.debug("Found a to-do entry: {}", model);

        model.update(updated.getDescription(), updated.getTitle());

        indexService.update(model);

        return model;
    }

    @PreAuthorize("hasPermission('Todo', 'search')")
    @Override
    public List<TodoDocument> search(String searchTerm, Pageable page) {
        LOGGER.debug("Search todo entries with search term: {} and page: {}", searchTerm, page);
        return indexService.search(searchTerm, page);
    }
}
