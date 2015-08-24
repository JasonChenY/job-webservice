package com.tiaonaer.ws.todo.service;

import com.tiaonaer.ws.todo.document.TodoDocument;
import com.tiaonaer.ws.todo.model.Todo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Petri Kainulainen
 */
public interface TodoIndexService {

    public void addToIndex(Todo todoEntry);

    public long countSearchResults(String searchTerm);

    public void deleteFromIndex(Long id);

    public List<TodoDocument> search(String searchTerm, Pageable page);

    public void update(Todo todoEntry);
}
