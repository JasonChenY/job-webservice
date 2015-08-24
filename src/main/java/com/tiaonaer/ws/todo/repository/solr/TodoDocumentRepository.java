package com.tiaonaer.ws.todo.repository.solr;

import com.tiaonaer.ws.todo.document.TodoDocument;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author Petri Kainulainen
 */
public interface TodoDocumentRepository extends CustomTodoDocumentRepository, SolrCrudRepository<TodoDocument, String> {

}
