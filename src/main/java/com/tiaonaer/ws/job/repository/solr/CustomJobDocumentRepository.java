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

    public Page<JobDocument> search(List<String> companies, List<String> locations, String searchTerm, Pageable page);

    public void update(Job job);
}
