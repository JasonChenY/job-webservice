package com.tiaonaer.ws.todo.repository.solr;

import com.tiaonaer.ws.todo.document.TodoDocument;
import com.tiaonaer.ws.todo.model.Todo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Petri Kainulainen
 */
public interface CustomTodoDocumentRepository {

    public long count(String searchTerm);

    public List<TodoDocument> search(String searchTerm, Pageable page);

    public void update(Todo todoEntry);
}
