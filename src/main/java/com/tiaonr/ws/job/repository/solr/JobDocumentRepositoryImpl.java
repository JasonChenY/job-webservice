package com.tiaonr.ws.job.repository.solr;

import com.tiaonr.ws.job.document.JobDocument;
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
        Criteria conditions = createKeywordSearchConditions(searchTerm);
        SimpleQuery countQuery = new SimpleQuery(conditions);
        return solrTemplate.count(countQuery);
    }

    public JobDocument findByJobID(String job_id) {
        LOGGER.debug("Finding job via job id: {}", job_id);
        Criteria condition = new Criteria(JobDocument.JOB_UNIQUE_ID).is(job_id);
        SimpleQuery query = new SimpleQuery(condition);
        return solrTemplate.queryForObject(query, JobDocument.class);
    }

    /* Create dynamic query, to be extended to include job_post_date */
    @Override
    public Page<JobDocument> search(String query, List<String> filter_query, int days, Pageable page) {
        LOGGER.debug("Building a criteria query with search keyword: {}", query);

        Query  q = null;
        Criteria simple = createKeywordSearchConditions(query);
        if ( simple == null )
            q = new SimpleQuery("*:*");
        else
            q = new SimpleQuery(simple);

        Criteria filter = createFilterSearchConditions(filter_query);
        if ( filter != null ) {
            q = q.addFilterQuery(new SimpleFilterQuery(filter));
        }

        q.setPageRequest(page);
        //Query q = new SimpleQuery("*:*").addFilterQuery(new SimpleFilterQuery(new Criteria(QueryFunction.query("{!query v = 'one'}"))));
        // return results.getContent() ===> List<JobDocument>
        Page<JobDocument> results = solrTemplate.queryForPage(q, JobDocument.class);
        return results;
    }

    private Criteria createFilterSearchConditions(List<String> filter_query) {
        if ( filter_query == null ) return null;
        // Assume format: fq=job_company:(A OR B OR C)&fq=job_location:(D OR E OR F)
        // But backbone.paginator dont support multiple query parameter with same names,
        // Solution: fq=(job_company:(A OR B OR C) AND job_location:(D OR E OR F))
        for ( int i = 0; i < filter_query.size(); i++ ) {
            LOGGER.debug("createFilterSearchConditions: " + filter_query.get(i));
        }
        Criteria conditions = null;
        if ( filter_query.size() == 1 ) {
            conditions = new SimpleStringCriteria(filter_query.get(0));
        } else {
            // Use Expression to bypass the decode & encode process.
            for (int i = 0; i < filter_query.size(); i++) {
                String fq = filter_query.get(i);

                // Here should check later whether we received some encoded ":".
                if (fq.startsWith(JobDocument.JOB_COMPANY)) {
                    Criteria tmp = new Criteria(JobDocument.JOB_COMPANY).expression(fq.substring(JobDocument.JOB_COMPANY.length() + 1));
                    conditions = (conditions == null ? tmp : conditions.and(tmp));
                } else if (fq.startsWith(JobDocument.JOB_LOCATION)) {
                    Criteria tmp = new Criteria(JobDocument.JOB_LOCATION).expression(fq.substring(JobDocument.JOB_LOCATION.length() + 1));
                    conditions = (conditions == null ? tmp : conditions.and(tmp));
                }
            }
        }
        return conditions;

        /*
        if ( companies.size() > 0 ) {
            conditions = conditions.and(new Criteria(JobDocument.JOB_COMPANY).in(companies));
        }
        if ( locations.size() > 0 ) {
            conditions = conditions.and(new Criteria(JobDocument.JOB_LOCATION).in(locations));
        }*/
    }

    private Criteria createKeywordSearchConditions(String query) {
        if ( query == null ) return null;
        if ( query.startsWith("\"") && query.endsWith("\"") ) {
            return new Criteria(JobDocument.JOB_TITLE).expression(query)
                    .or(new Criteria(JobDocument.JOB_DESCRIPTION).expression(query));
        } else {
            String[] words = query.split(" ");
/*
            Criteria conditions = null;

            for (String word : words) {
                if (conditions == null) {
                    conditions = new Criteria(JobDocument.JOB_TITLE).contains(word)
                            .or(new Criteria(JobDocument.JOB_DESCRIPTION).contains(word));
                } else {
                    conditions = conditions.or(new Criteria(JobDocument.JOB_TITLE).contains(word))
                            .or(new Criteria(JobDocument.JOB_DESCRIPTION).contains(word));
                }
            }
            return conditions;
*/
            return new Criteria(JobDocument.JOB_TITLE).contains(words)
                    .or(new Criteria(JobDocument.JOB_DESCRIPTION).contains(words));

        }
    }

    @Override
    public void update(JobDocument job) {
        LOGGER.debug("Performing partial update for todo entry: {}", job);

        PartialUpdate update = new PartialUpdate(JobDocument.JOB_UNIQUE_ID, job.getId());

        /* Now we only see one case, e.g to set job's expired status */
        update.add(JobDocument.JOB_EXPIRED, job.getJob_expired());

        solrTemplate.saveBean(update);
        solrTemplate.commit();
    }



}
