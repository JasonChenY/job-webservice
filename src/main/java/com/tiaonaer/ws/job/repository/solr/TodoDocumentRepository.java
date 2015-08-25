package com.tiaonaer.ws.job.repository.solr;

import com.tiaonaer.ws.job.document.TodoDocument;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * @author jason.y.chen
 */
public interface TodoDocumentRepository extends CustomTodoDocumentRepository, SolrCrudRepository<TodoDocument, String> {

}
