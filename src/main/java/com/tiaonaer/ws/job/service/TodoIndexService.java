package com.tiaonaer.ws.job.service;

import com.tiaonaer.ws.job.document.TodoDocument;
import com.tiaonaer.ws.job.model.Todo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author jason.y.chen
 */
public interface TodoIndexService {

    public void addToIndex(Todo todoEntry);

    public long countSearchResults(String searchTerm);

    public void deleteFromIndex(Long id);

    public List<TodoDocument> search(String searchTerm, Pageable page);

    public void update(Todo todoEntry);
}
