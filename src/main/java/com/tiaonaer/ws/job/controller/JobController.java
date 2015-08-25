package com.tiaonaer.ws.job.controller;

import com.tiaonaer.ws.common.util.LocaleContextHolderWrapper;
import com.tiaonaer.ws.job.document.JobDocument;
import com.tiaonaer.ws.job.dto.FormValidationErrorDTO;
import com.tiaonaer.ws.job.dto.TodoDTO;
import com.tiaonaer.ws.job.exception.FormValidationError;
import com.tiaonaer.ws.job.exception.TodoNotFoundException;
import com.tiaonaer.ws.job.model.Job;
import com.tiaonaer.ws.job.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.solr.core.query.result.FacetPage;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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


    @RequestMapping(value = "/api/jobs", method = RequestMethod.GET)
    @ResponseBody
    public FacetPage<JobDocument> findAll() {
        LOGGER.debug("Finding all todo entries.");

        return service.getInitJobsWithFacet();
    }
}
