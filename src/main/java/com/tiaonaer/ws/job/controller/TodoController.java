package com.tiaonaer.ws.job.controller;

import com.tiaonaer.ws.common.util.LocaleContextHolderWrapper;
import com.tiaonaer.ws.job.document.TodoDocument;
import com.tiaonaer.ws.job.dto.FormValidationErrorDTO;
import com.tiaonaer.ws.job.dto.TodoDTO;
import com.tiaonaer.ws.job.exception.FormValidationError;
import com.tiaonaer.ws.job.exception.TodoNotFoundException;
import com.tiaonaer.ws.job.model.Todo;
import com.tiaonaer.ws.job.service.TodoService;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author jason.y.chen
 */
@Controller
public class TodoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TodoController.class);

    protected static final String OBJECT_NAME_TODO = "todo";

    @Resource
    private TodoService service;

    @Resource
    private LocaleContextHolderWrapper localeHolderWrapper;

    @Resource
    private MessageSource messageSource;

    @Resource
    private Validator validator;


    @RequestMapping(value = "/api/todo", method = RequestMethod.POST)
    @ResponseBody
    public TodoDTO add(@RequestBody TodoDTO dto) throws FormValidationError {
        LOGGER.debug("Adding a new to-do entry with information: {}", dto);

        validate(OBJECT_NAME_TODO, dto);

        Todo added = service.add(dto);
        LOGGER.debug("Added a to-do entry with information: {}", added);

       return createDTO(added);
    }

    @RequestMapping(value = "/api/todo/search/count/{searchTerm}", method = RequestMethod.GET)
    @ResponseBody
    public long countSearchResults(@PathVariable("searchTerm") String searchTerm) {
        LOGGER.debug("Finding search result count for search term: {}", searchTerm);
        return service.countSearchResults(searchTerm);
    }

    @RequestMapping(value = "/api/todo/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public TodoDTO deleteById(@PathVariable("id") Long id) throws TodoNotFoundException {
        LOGGER.debug("Deleting a to-do entry with id: {}", id);

        Todo deleted = service.deleteById(id);
        LOGGER.debug("Deleted to-do entry with information: {}", deleted);

        return createDTO(deleted);
    }

    @RequestMapping(value = "/api/todo", method = RequestMethod.GET)
    @ResponseBody
    public List<TodoDTO> findAll() {
        LOGGER.debug("Finding all todo entries.");

        List<Todo> models = service.findAll();
        LOGGER.debug("Found {} to-do entries.", models.size());

        return createDTOs(models);
    }

    private List<TodoDTO> createDTOs(List<Todo> models) {
        List<TodoDTO> dtos = new ArrayList<TodoDTO>();

        for (Todo model: models) {
            dtos.add(createDTO(model));
        }

        return dtos;
    }

    @RequestMapping(value = "/api/todo/{id}", method = RequestMethod.GET)
    @ResponseBody
    public TodoDTO findById(@PathVariable("id") Long id) throws TodoNotFoundException {
        LOGGER.debug("Finding to-do entry with id: {}", id);

        Todo found = service.findById(id);
        LOGGER.debug("Found to-do entry with information: {}", found);

        return createDTO(found);
    }

    @RequestMapping(value = "/api/todo/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public TodoDTO update(@RequestBody TodoDTO dto, @PathVariable("id") Long todoId) throws TodoNotFoundException, FormValidationError {
        LOGGER.debug("Updating a to-do entry with information: {}", dto);

        validate(OBJECT_NAME_TODO, dto);

        Todo updated = service.update(dto);
        LOGGER.debug("Updated the information of a to-entry to: {}", updated);

        return createDTO(updated);
    }

    private TodoDTO createDTO(Todo model) {
        TodoDTO dto = new TodoDTO();

        dto.setId(model.getId());
        dto.setDescription(model.getDescription());
        dto.setTitle(model.getTitle());

        return dto;
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

    @RequestMapping(value = "/api/todo/search/{searchTerm}", method = RequestMethod.GET)
    @ResponseBody
    public List<TodoDTO> search(@PathVariable("searchTerm") String searchTerm, Pageable page) {
        LOGGER.debug("Search todo entries with search term: {} and page: {}", searchTerm, page);

        List<TodoDocument> todoEntries = service.search(searchTerm, page);
        LOGGER.debug("Found {} todo entries", todoEntries.size());

        return createSearchResultDTOs(todoEntries);
    }

    private List<TodoDTO> createSearchResultDTOs(List<TodoDocument> todoEntries) {
        List<TodoDTO> dtos = new ArrayList<TodoDTO>();

        for (TodoDocument entry: todoEntries) {
            TodoDTO dto = new TodoDTO();

            dto.setId(Long.valueOf(entry.getId()));
            dto.setTitle(entry.getTitle());

            dtos.add(dto);
        }

        return dtos;
    }

    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleTodoNotFoundException(TodoNotFoundException ex) {
        LOGGER.debug("handling 404 error on a todo entry");
    }
}
