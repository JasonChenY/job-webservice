package com.tiaonaer.ws.job.controller;

import com.tiaonaer.ws.common.util.LocaleContextHolderWrapper;
import com.tiaonaer.ws.job.document.JobDocument;
import com.tiaonaer.ws.job.dto.FormValidationErrorDTO;
import com.tiaonaer.ws.job.dto.JobDTO;
import com.tiaonaer.ws.job.dto.JobsFacetDTO;
import com.tiaonaer.ws.job.exception.FormValidationError;
import com.tiaonaer.ws.job.exception.TodoNotFoundException;
import com.tiaonaer.ws.job.model.Job;
import com.tiaonaer.ws.job.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetEntry;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.core.query.Field;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Collection;

/**
 * @author jason.y.chen
 */
@Controller
public class JobController {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobController.class);

    protected static final String OBJECT_NAME_TODO = "job";

    @Resource
    private JobService service;

    @Resource
    private LocaleContextHolderWrapper localeHolderWrapper;

    @Resource
    private MessageSource messageSource;

    @Resource
    private Validator validator;


    @RequestMapping(value = "/api/jobs", method = RequestMethod.GET/*, produces = "application/json"*/)
    @ResponseBody
    public JobsFacetDTO jobList(
            @RequestParam(value = "q", required = false) String query,
            @RequestParam(value="fq", required=false) List<String> filter_queries,
            @RequestParam(value = "days", defaultValue = "-1", required = false) int days,
            @RequestParam(value = "facet", defaultValue = "false", required = false) boolean facet,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        LOGGER.debug("Finding all jobs entries.");
        // In fact should create model object Job in JobService, and translate Job to JobDTO in controller.
        if ( facet )
            return service.getJobsWithFacet();
        else
            return service.getJobs(query, filter_queries, days, pageable);
    }

/*
The following example extracts the name form parameter from the POST form data:
@POST
@Consumes("application/x-www-form-urlencoded")
public void post(@FormParam("name") String name) {
    // Store the message
}

To obtain a general map of parameter names and values for query and path parameters, use the following code:
@GET
public String get(@Context UriInfo ui) {
    MultivaluedMap<String, String> queryParams = ui.getQueryParameters();
    MultivaluedMap<String, String> pathParams = ui.getPathParameters();
}

The following method extracts header and cookie parameter names and values into a map:
@GET
public String get(@Context HttpHeaders hh) {
    MultivaluedMap<String, String> headerParams = ui.getRequestHeaders();
    Map<String, Cookie> pathParams = ui.getCookies();
}

In general, @Context can be used to obtain contextual Java types related to the request or response.
For form parameters, it is possible to do the following:
@POST
@Consumes("application/x-www-form-urlencoded")
public void post(MultivaluedMap<String, String> formParams) {
    // Store the message
}
*/
}
