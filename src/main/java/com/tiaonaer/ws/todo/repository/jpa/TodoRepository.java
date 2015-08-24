package com.tiaonaer.ws.todo.repository.jpa;

import com.tiaonaer.ws.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Petri Kainulainen
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
