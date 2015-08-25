package com.tiaonaer.ws.job.repository.solr;

import com.tiaonaer.ws.job.document.JobDocument;
import com.tiaonaer.ws.job.model.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jason.y.chen
 */
@Repository
public class JobDocumentRepositoryImpl implements CustomJobDocumentRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobDocumentRepositoryImpl.class);

    @Resource
    private SolrTemplate solrTemplate;

    @Override
    public long count(String searchTerm) {
        LOGGER.debug("Finding count for search term: {}", searchTerm);
        Criteria conditions = new Criteria();
        createKeywordSearchConditions(conditions, searchTerm);
        SimpleQuery countQuery = new SimpleQuery(conditions);
        return solrTemplate.count(countQuery);
    }

    /* Create dynamic query, to be extended to include job_post_date */
    @Override
    public Page<JobDocument> search(List<String> companies, List<String> locations, String searchTerm, Pageable page) {
        LOGGER.debug("Building a criteria query with search term: {}", searchTerm);

        Criteria simple = new Criteria();
        createKeywordSearchConditions(simple, searchTerm);

        Criteria filter = new Criteria();
        createFilterSearchConditions(filter, companies, locations);

        Query  q = new SimpleQuery(simple).addFilterQuery(new SimpleFilterQuery(filter));
        q.setPageRequest(page);
        //Query q = new SimpleQuery("*:*").addFilterQuery(new SimpleFilterQuery(new Criteria(QueryFunction
        //        .query("{!query v = 'one'}"))));
        // return results.getContent() ===> List<JobDocument>
        Page<JobDocument> results = solrTemplate.queryForPage(q, JobDocument.class);
        return results;
    }

    private void createFilterSearchConditions(Criteria conditions, List<String> companies, List<String> locations) {
        if ( companies.size() > 0 ) {
            conditions = conditions.and(new Criteria(JobDocument.JOB_COMPANY).in(companies));
        }
        if ( locations.size() > 0 ) {
            conditions = conditions.and(new Criteria(JobDocument.JOB_LOCATION).in(locations));
        }
    }

    private void createKeywordSearchConditions(Criteria conditions, String searchTerm) {
        String[] words = searchTerm.split(" ");
        for (String word: words) {
            conditions = conditions.or(new Criteria(JobDocument.JOB_TITLE).contains(word))
                    .or(new Criteria(JobDocument.JOB_DESCRIPTION).contains(word));
        }
    }

    @Override
    public void update(Job job) {
        LOGGER.debug("Performing partial update for todo entry: {}", job);

        PartialUpdate update = new PartialUpdate(JobDocument.JOB_UNIQUE_ID, job.getId());

        /* Now we only see one case, e.g to set job's expired status */
        update.add(JobDocument.JOB_EXPIRED, job.getJob_expired());

        solrTemplate.saveBean(update);
        solrTemplate.commit();
    }



}
