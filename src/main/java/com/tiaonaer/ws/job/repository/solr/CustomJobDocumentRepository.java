package com.tiaonaer.ws.job.repository.solr;

import com.tiaonaer.ws.job.document.JobDocument;
import com.tiaonaer.ws.job.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author jason.y.chen
 */
public interface CustomJobDocumentRepository {

    public long count(String searchTerm);

    public Page<JobDocument> search(String query, List<String> filter_query, int days, Pageable page);

    public void update(JobDocument job);

    public JobDocument findByJobID(String job_id);
}
