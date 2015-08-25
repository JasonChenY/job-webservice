package com.tiaonaer.ws.job.service;

import com.tiaonaer.ws.job.document.JobDocument;
import com.tiaonaer.ws.job.model.Job;
import com.tiaonaer.ws.job.repository.solr.JobDocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Iterator;

/**
 * Created by echyong on 8/25/15.
 */
@Service
public class JobService {
    private static final Logger LOGGER = LoggerFactory.getLogger(JobService.class);

    @Resource
    private JobDocumentRepository repository;

    @Transactional
    public FacetPage<JobDocument> getInitJobsWithFacet() {
        LOGGER.debug("enter getInitJobsWithFacet");
        FacetPage<JobDocument> jobs = repository.getInitJobsWithFacet(new PageRequest(0, 10));
        //List<JobDocument> joblist = jobs.getContent();
        Iterator itr = jobs.iterator();
        while (itr.hasNext()) {

            JobDocument job = (JobDocument) itr.next();

            job.setNum_comments(7);
            job.setNum_favorited(9);
        }
        return jobs;
    }
        /*
        * For Page<>
booksPage.getContent()       // get a list of (max) 10 books
booksPage.getTotalElements() // total number of elements (can be >10)
booksPage.getTotalPages()    // total number of pages
booksPage.getNumber()        // current page number
booksPage.isFirstPage()      // true if this is the first page
booksPage.hasNextPage()      // true if another page is available
booksPage.nextPageable()     // the pageable for requesting the next page
        * */
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
     */
}
