package com.tiaonaer.ws.job.dto;

/**
 * Created by echyong on 8/25/15.
 */
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.springframework.data.domain.Page;
import com.tiaonaer.ws.job.document.JobDocument;
import com.tiaonaer.ws.job.dto.JobDTO;
import com.tiaonaer.ws.job.dto.PageNavigation;

public class JobsFacetDTO extends PageNavigation {
    private List<JobDTO> jobs = new ArrayList<JobDTO>();
    private Map<String, Long> companies = new HashMap<String, Long>();
    private Map<String, Long> locations = new HashMap<String, Long>();

    public JobsFacetDTO(Page<?> page) { super(page);}

    public void addJobDTO(JobDTO dto) { jobs.add(dto); }
    public List<JobDTO> getJobs() { return jobs; }

    public void addFacetField(String field, String value, long count) {
        if ( "job_company".equals(field) ) {
            companies.put(value, count);
        } else if ( "job_location".equals(field) ) {
            locations.put(value, count);
        }
    }
    public Map<String, Long> getCompanies() { return companies; }
    public Map<String, Long> getLocations() { return locations; }
}
