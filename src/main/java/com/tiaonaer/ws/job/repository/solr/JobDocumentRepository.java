package com.tiaonaer.ws.job.repository.solr;

import com.tiaonaer.ws.job.document.JobDocument;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.Facet;
import org.springframework.data.solr.repository.Query;
import org.springframework.data.solr.repository.SolrCrudRepository;

/**
 * Created by echyong on 8/24/15.
 */
public interface JobDocumentRepository extends CustomJobDocumentRepository, SolrCrudRepository<JobDocument, String> {
    /* Used for find all expired jobs */
    @Query(value="*:*", filters = {JobDocument.JOB_EXPIRED + ":true"})
    public Page<JobDocument> findAllExpiredJobs(Pageable page);

    //http://192.168.137.128/api/job?q=*:*&job_type:0&job_expired:false&facet=true&fq=&facet.field=job_company&facet.field=job_location&facet.mincount=1&sort=job_post_date+desc&wt=json&indent=true
    /* Used for the initial url to get facets information */
    @Query(value = "*:*", filters = {
            JobDocument.JOB_TYPE + ":0",
            JobDocument.JOB_EXPIRED + ":false"
    })
    @Facet(fields = { JobDocument.JOB_COMPANY, JobDocument.JOB_LOCATION },
           minCount=1, limit=1000
           /*Bug in Jackson ? queries = { JobDocument.JOB_POST_DATE + ":[NOW-1DAY TO NOW]",
                       JobDocument.JOB_POST_DATE + ":[NOW-7DAY TO NOW]",
                       JobDocument.JOB_POST_DATE + ":[NOW-31DAY TO NOW]",
                       JobDocument.JOB_POST_DATE + ":[NOW-365DAY TO NOW]"
           }*/
    )
    public FacetPage<JobDocument> getJobsWithFacet(Pageable page);

    /*
    FacetPage<Book> booksFacetPage = bookRepository.findByNameAndFacetOnCategories(bookName, new PageRequest(0, 10));
booksFacetPage.getContent(); // the first 10 books
for (Page<? extends FacetEntry> page : booksFacetPage.getAllFacets()) {
  for (FacetEntry facetEntry : page.getContent()) {
    String categoryName = facetEntry.getValue();  // name of the category
    long count = facetEntry.getValueCount();      // number of books in this category
    // convert the category name back to an enum
    Category category = Category.valueOf(categoryName.toUpperCase());
  }
}
     */
}
