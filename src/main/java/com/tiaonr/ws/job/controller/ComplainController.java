package com.tiaonr.ws.job.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.tiaonr.ws.job.dto.CommentDTO;
import com.tiaonr.ws.job.exception.CommentNotFoundException;
import com.tiaonr.ws.job.exception.FormValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.tiaonr.ws.common.util.LocaleContextHolderWrapper;
import com.tiaonr.ws.job.dto.ComplainDTO;
import com.tiaonr.ws.job.dto.ComplainsDTO;
import com.tiaonr.ws.job.model.Complain;
import com.tiaonr.ws.job.service.ComplainService;
import com.tiaonr.ws.job.exception.CustomRequestException;

/**
 * @author jason.y.chen
 */
@Controller
public class ComplainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComplainController.class);

    @Resource
    private ComplainService service;

    @Resource
    private LocaleContextHolderWrapper localeHolderWrapper;

    @Resource
    private MessageSource messageSource;

    //curl -v -H "Content-Type:application/json" -H "Cookie:JSESSIONID=8gm23umstm8d1oyr83qx0y760" -X POST -d "{\"job_id\":\"http://new.abb.com/cn/careers/job-advertisement/details/57480179/painting-robot-engineer\", \"type\":1}" http://192.168.137.128:8080/api/complain
    @RequestMapping(value = "/api/complain", method = RequestMethod.POST)
    @ResponseBody
    public ComplainDTO add(@RequestBody ComplainDTO dto) throws CustomRequestException {
        LOGGER.debug("Adding a new complain entry with information: {}", dto);
        return service.add(dto);
    }

    //curl -v -H "Content-Type:application/json" -H "Cookie:JSESSIONID=8gm23umstm8d1oyr83qx0y760" -X GET http://192.168.137.128:8080/api/complain
    @RequestMapping(value = "/api/complain", method = RequestMethod.GET)
    @ResponseBody
    public ComplainsDTO complainList(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        LOGGER.debug("Finding all complains, page: {}", pageable);
        return service.findAll(pageable);
    }

    @RequestMapping(value = "/api/complain/admin", method = RequestMethod.GET)
    @ResponseBody
    public ComplainsDTO complainList(
            @RequestParam(value = "user_id", required = false) String user_id,
            @RequestParam(value = "type", required = false, defaultValue = "-1") int type,
            @RequestParam(value = "status", required = false, defaultValue = "-1") int status,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        LOGGER.debug("Finding all complains with optional user_id: {}, page: {}", user_id, pageable);
        return service.findAll(user_id, type, status, pageable);
    }

    //curl -v -H "Content-Type:application/json" -H "Cookie:JSESSIONID=ci294drpd9im1v5kvua7z7fte" -X PUT -d "{\"status\":2}" http://192.168.137.128:8080/api/complain/2
    @RequestMapping(value = "/api/complain/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ComplainDTO update(@RequestBody ComplainDTO dto, @PathVariable("id") Long complainId) throws CustomRequestException {
        LOGGER.debug("Updating a complain with information: {}", dto);

        if ( dto.getId() == null )
            dto.setId(complainId);

        if ( dto.getId() != complainId )
            throw new CustomRequestException("Complain ID mismatch with id in request url");
        else
            return service.update(dto);
    }

    @ExceptionHandler(CustomRequestException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleCustomRequestException(CustomRequestException ex) {
        LOGGER.debug("handling CustomRequestException");
        return ex.getMessage();
    }
}
