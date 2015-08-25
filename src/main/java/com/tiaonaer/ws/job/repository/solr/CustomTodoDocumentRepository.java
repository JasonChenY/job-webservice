package com.tiaonaer.ws.job.repository.solr;

import com.tiaonaer.ws.job.document.TodoDocument;
import com.tiaonaer.ws.job.model.Todo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author jason.y.chen
 */
public interface CustomTodoDocumentRepository {

    public long count(String searchTerm);

    public List<TodoDocument> search(String searchTerm, Pageable page);

    public void update(Todo todoEntry);
}
