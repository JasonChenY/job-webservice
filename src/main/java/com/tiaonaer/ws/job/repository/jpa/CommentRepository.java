package com.tiaonaer.ws.job.repository.jpa;

import java.util.List;
import com.tiaonaer.ws.job.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
/**
 * Created by echyong on 8/27/15.
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select u from Comment u where u.job_id = ?1")
    Page<Comment> findByJob_id(String job_id, Pageable pageable);
}
