package com.tiaonaer.ws.todo.repository.solr;

import com.tiaonaer.ws.todo.document.TodoDocument;
import com.tiaonaer.ws.todo.model.Todo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.PartialUpdate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Petri Kainulainen
 */
@Repository
public class TodoDocumentRepositoryImpl implements CustomTodoDocumentRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoDocumentRepositoryImpl.class);

    @Resource
    private SolrTemplate solrTemplate;

    @Override
    public long count(String searchTerm) {
        LOGGER.debug("Finding count for search term: {}", searchTerm);

        String[] words = searchTerm.split(" ");
        Criteria conditions = createSearchConditions(words);
        SimpleQuery countQuery = new SimpleQuery(conditions);

        return solrTemplate.count(countQuery);
    }

    @Override
    public List<TodoDocument> search(String searchTerm, Pageable page) {
        LOGGER.debug("Building a criteria query with search term: {} and page: {}", searchTerm, page);

        String[] words = searchTerm.split(" ");

        Criteria conditions = createSearchConditions(words);
        SimpleQuery search = new SimpleQuery(conditions);
        search.setPageRequest(page);

        Page results = solrTemplate.queryForPage(search, TodoDocument.class);
        return results.getContent();
    }

    private Criteria createSearchConditions(String[] words) {
        Criteria conditions = null;

        for (String word: words) {
            if (conditions == null) {
                conditions = new Criteria(TodoDocument.FIELD_TITLE).contains(word)
                        .or(new Criteria(TodoDocument.FIELD_DESCRIPTION).contains(word));
            }
            else {
                conditions = conditions.or(new Criteria(TodoDocument.FIELD_TITLE).contains(word))
                        .or(new Criteria(TodoDocument.FIELD_DESCRIPTION).contains(word));
            }
        }

        return conditions;
    }

    @Override
    public void update(Todo todoEntry) {
        LOGGER.debug("Performing partial update for todo entry: {}", todoEntry);

        PartialUpdate update = new PartialUpdate(TodoDocument.FIELD_ID, todoEntry.getId().toString());

        update.add(TodoDocument.FIELD_DESCRIPTION, todoEntry.getDescription());
        update.add(TodoDocument.FIELD_TITLE, todoEntry.getTitle());

        solrTemplate.saveBean(update);
        solrTemplate.commit();
    }
}
