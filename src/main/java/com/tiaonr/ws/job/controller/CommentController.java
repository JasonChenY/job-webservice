package com.tiaonr.ws.job.controller;

import com.tiaonr.ws.common.util.LocaleContextHolderWrapper;
import com.tiaonr.ws.job.dto.FormValidationErrorDTO;
import com.tiaonr.ws.job.dto.CommentDTO;
import com.tiaonr.ws.job.dto.CommentsDTO;
import com.tiaonr.ws.job.exception.FormValidationError;
import com.tiaonr.ws.job.exception.CommentNotFoundException;
import com.tiaonr.ws.job.model.Comment;
import com.tiaonr.ws.job.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author jason.y.chen
 */
@Controller
public class CommentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentController.class);

    protected static final String OBJECT_NAME_COMMENT = "comment";

    @Resource
    private CommentService service;

    @Resource
    private LocaleContextHolderWrapper localeHolderWrapper;

    @Resource
    private MessageSource messageSource;

    @Resource
    private Validator validator;

    //curl -v -H "Content-Type:application/json" -H "Cookie: openid_provider=yahoo; JSESSIONID=1dratjc1zdx4waoz4vowamrje; i18next=zh-CN" -X POST -d "{\"content\":\"test3 without specifying anonymous\", \"job_id\":\"http://new.abb.com/cn/careers/job-advertisement/details/57480179/painting-robot-engineer\"}" http://192.168.137.128:8080/api/comment
    //curl -v -H "Content-Type:application/json" -H "Cookie: openid_provider=yahoo; JSESSIONID=1dratjc1zdx4waoz4vowamrje; i18next=zh-CN" -X POST -d "{\"content\":\"test4 comment in anonymous\", \"job_id\":\"http://new.abb.com/cn/careers/job-advertisement/details/57480179/painting-robot-engineer\", \"anonymous\":true}" http://192.168.137.128:8080/api/comment
    @RequestMapping(value = "/api/comment", method = RequestMethod.POST)
    @ResponseBody
    public CommentDTO add(@RequestBody CommentDTO dto) throws FormValidationError {
        LOGGER.debug("Adding a new comment entry with information: {}", dto);
        validate(OBJECT_NAME_COMMENT, dto);
        return service.add(dto);
    }

    //curl -v -H "Content-Type:application/json" -H "Cookie: openid_provider=yahoo; JSESSIONID=kuwm1r7a2k7j1u96vun32n4m8; i18next=zh-CN" -X DELETE http://192.168.137.128:8080/api/comment/7
    @RequestMapping(value = "/api/comment/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommentDTO deleteById(@PathVariable("id") Long id) throws CommentNotFoundException {
        LOGGER.debug("Deleting a comment with id: {}", id);
        return  service.deleteById(id);
    }

    //curl -v -H "Content-Type:application/json" -H "Cookie: JSESSIONID=kuwm1r7a2k7j1u96vun32n4m8;" -X GET http://192.168.137.128:8080/api/comment?page.page=3&page.size=10&page.sort=creationTime&page.sort.dir=desc&job_id=http://new.abb.com/cn/careers/job-advertisement/details/57480179/painting-robot-engineer"
    @RequestMapping(value = "/api/comment", method = RequestMethod.GET)
    @ResponseBody
    public CommentsDTO commentList(
            @RequestParam(value = "job_id", required = true) String job_id,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        LOGGER.debug("Finding all comments for job {} page: {} ", job_id, pageable);
        return service.findAll(job_id, pageable);
    }

    @RequestMapping(value = "/api/comment/{id}", method = RequestMethod.GET)
    @ResponseBody
    public CommentDTO findById(@PathVariable("id") Long id) throws CommentNotFoundException {
        LOGGER.debug("Finding comment with id: {}", id);
        return service.findById(id);
    }

    //curl -v -H "Content-Type:application/json" -H "Cookie: JSESSIONID=1mwdoy60gttqa182pdb9jwbuz2;" -X PUT -d "{\"id\":21, \"content\":\"update comments via PUT again\", \"job_id\":\"http://new.abb.com/cn/careers/job-advertisement/details/57480179/painting-robot-engineer\", \"anonymous\":true}" http://192.168.137.128:8080/api/comment/21
    @RequestMapping(value = "/api/comment/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public CommentDTO update(@RequestBody CommentDTO dto, @PathVariable("id") Long commentId) throws CommentNotFoundException, FormValidationError {
        LOGGER.debug("Updating a comment with information: {}", dto);
        validate(OBJECT_NAME_COMMENT, dto);
        if ( dto.getId() == null )
            dto.setId(commentId);

        if ( dto.getId() != commentId )
            throw new CommentNotFoundException("Comment ID mismatch with id in request url");
        else
            return service.update(dto);
    }

    private void validate(String objectName, Object validated) throws FormValidationError {
        LOGGER.debug("Validating object: " + validated);

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(validated, objectName);
        validator.validate(validated, bindingResult);

        if (bindingResult.hasErrors()) {
            LOGGER.debug("Validation errors found:" + bindingResult.getFieldErrors());
            throw new FormValidationError(bindingResult.getFieldErrors());
        }
    }

    @ExceptionHandler(FormValidationError.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FormValidationErrorDTO handleFormValidationError(FormValidationError validationError) {
        LOGGER.debug("Handling form validation error");

        Locale current = localeHolderWrapper.getCurrentLocale();

        List<FieldError> fieldErrors = validationError.getFieldErrors();

        FormValidationErrorDTO dto = new FormValidationErrorDTO();

        for (FieldError fieldError: fieldErrors) {
            String[] fieldErrorCodes = fieldError.getCodes();
            for (int index = 0; index < fieldErrorCodes.length; index++) {
                String fieldErrorCode = fieldErrorCodes[index];

                String localizedError = messageSource.getMessage(fieldErrorCode, fieldError.getArguments(), current);
                if (localizedError != null && !localizedError.equals(fieldErrorCode)) {
                    LOGGER.debug("Adding error message: {} to field: {}", localizedError, fieldError.getField());
                    dto.addFieldError(fieldError.getField(), localizedError);
                    break;
                }
                else {
                    if (isLastFieldErrorCode(index, fieldErrorCodes)) {
                        dto.addFieldError(fieldError.getField(), localizedError);
                    }
                }
            }
        }

        return dto;
    }

    private boolean isLastFieldErrorCode(int index, String[] fieldErrorCodes) {
        return index == fieldErrorCodes.length - 1;
    }

    @ExceptionHandler(CommentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleCommentNotFoundException(CommentNotFoundException ex) {
        LOGGER.debug("handling 404 error on a comment entry");
        return ex.getMessage();
    }
}
