package com.tiaonaer.ws.job.service;

import com.tiaonaer.ws.job.document.JobDocument;
import com.tiaonaer.ws.job.dto.JobDTO;
import com.tiaonaer.ws.job.dto.JobsFacetDTO;
import com.tiaonaer.ws.job.model.Job;
import com.tiaonaer.ws.job.repository.jpa.CommentRepository;
import com.tiaonaer.ws.job.repository.jpa.FavoriteRepository;
import com.tiaonaer.ws.job.repository.solr.JobDocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.Field;
import org.springframework.data.solr.core.query.result.FacetEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;

/**
 * Created by echyong on 8/25/15.
 */
@Service
public class JobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobService.class);

    @Resource
    private JobDocumentRepository solrRepository;

    @Resource
    private CommentRepository commentRepository;

    @Resource
    private FavoriteRepository favoriteRepository;

    @Transactional
    public JobsFacetDTO getJobsWithFacet() {
        LOGGER.debug("enter getJobsWithFacet");
        // Only need get jobs with facets without other query parameter, ignore them.
        FacetPage<JobDocument> jobs = solrRepository.getJobsWithFacet(new PageRequest(0, 10));
        JobsFacetDTO dto = new JobsFacetDTO(jobs);
        JobPage2JobsDTO(jobs, dto);
        for (Page<? extends FacetEntry> page : jobs.getAllFacets()) {
            for (FacetEntry facetEntry : page.getContent()) {
                String keyname = (String)(((Field)facetEntry.getKey()).getName());
                String name = facetEntry.getValue();
                long count = facetEntry.getValueCount();
                dto.addFacetField(keyname, name, count);
            }
        }
        return dto;
    }

    @Transactional
    public JobsFacetDTO getJobs(String query, List<String> filter_queries, int days, Pageable pageable) {
        LOGGER.debug("enter getJobs");
        // inside javascript: ?page.page=6&page.size=2&page.sort=id&page.sort.dir=desc   for Pageable.
        Page<JobDocument> jobs = solrRepository.search(query, filter_queries, days, pageable);
        JobsFacetDTO dto = new JobsFacetDTO(jobs);
        JobPage2JobsDTO(jobs, dto);
        return dto;
    }

    private void JobPage2JobsDTO(Page<JobDocument> jobs, JobsFacetDTO dto) {
        for ( JobDocument doc : jobs.getContent() ) {
            JobDTO jobdto = new JobDTO(doc);
            jobdto.setComments_num(commentRepository.count(doc.getId()));
            jobdto.setFavorities_num(favoriteRepository.count(doc.getId()));
            dto.addJobDTO(jobdto);
        }
    }
}
