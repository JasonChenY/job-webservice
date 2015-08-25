package com.tiaonaer.ws.job.repository.jpa;

import com.tiaonaer.ws.job.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jason.y.chen
 */
public interface TodoRepository extends JpaRepository<Todo, Long> {
}
