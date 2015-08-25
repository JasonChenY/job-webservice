package com.tiaonaer.ws.job.service;

import com.tiaonaer.ws.job.document.TodoDocument;
import com.tiaonaer.ws.job.dto.TodoDTO;
import com.tiaonaer.ws.job.exception.TodoNotFoundException;
import com.tiaonaer.ws.job.model.Todo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author jason.y.chen
 */
public interface TodoService {

    /**
     * Adds a new to-do entry.
     * @param added The information of the added to-do entry.
     * @return  The added to-do entry.
     */
    public Todo add(TodoDTO added);

    /**
     * Finds the search result count for the given search term.
     * @param searchTerm    The search term
     * @return  The search result count
     */
    public long countSearchResults(String searchTerm);

    /**
     * Deletes a to-do entry.
     * @param id    The id of the deleted to-do entry.
     * @return  The deleted to-do entry.
     * @throws TodoNotFoundException    if no to-do entry is found with the given id.
     */
    public Todo deleteById(Long id) throws TodoNotFoundException;

    /**
     * Returns a list of to-do entries.
     * @return
     */
    public List<Todo> findAll();

    /**
     * Finds a to-do entry.
     * @param id    The id of the wanted to-do entry.
     * @return  The found to-entry.
     * @throws TodoNotFoundException    if no to-do entry is found with the given id.
     */
    public Todo findById(Long id) throws TodoNotFoundException;

    /**
     * Updates the information of a to-do entry.
     * @param updated   The information of the updated to-do entry.
     * @return  The updated to-do entry.
     * @throws TodoNotFoundException    If no to-do entry is found with the given id.
     */
    public Todo update(TodoDTO updated) throws TodoNotFoundException;

    /**
     * Searches the todo entries which title or description contains the given search term.
     * @param searchTerm
     * @return The list of todo entries. If matching todo entries are not found, the method returns an empty list.
     */
    public List<TodoDocument> search(String searchTerm, Pageable page);
}
