package com.tiaonr.ws.job.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tiaonr.ws.job.model.Comment;
import com.tiaonr.ws.job.dto.CommentDTO;
import com.tiaonr.ws.job.dto.CommentsDTO;
import com.tiaonr.ws.job.exception.CommentNotFoundException;
import com.tiaonr.ws.security.util.SecurityContextUtil;
import com.tiaonr.ws.job.repository.jpa.CommentRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

/**
 * Created by echyong on 8/27/15.
 */
@Service
public class CommentService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentService.class);

    @Resource
    private CommentRepository repository;

    @Resource
    private SecurityContextUtil securityContextUtil;

    @Transactional
    @PreAuthorize("hasPermission('Comment', 'add')")
    public CommentDTO add(CommentDTO dto) {
        LOGGER.debug("enter createComment");
        Comment model = Comment.getBuilder(dto.getJob_id())
                .content(dto.getContent())
                .user_id(securityContextUtil.getUser_id())
                .anonymous(dto.getAnonymous() != null ? dto.getAnonymous() : false)
                .build();
        Comment persisted = repository.save(model);

        return new CommentDTO(persisted, securityContextUtil.getUser_id());
    }

    @PreAuthorize("hasPermission('Comment', 'delete')")
    @Transactional(rollbackFor = {CommentNotFoundException.class})
    public CommentDTO deleteById(Long id) throws CommentNotFoundException {
        LOGGER.debug("Deleting a comment entry with id: {}", id);

        Comment found = repository.findOne(id);
        LOGGER.debug("Found Comment entry: {}", found);

        if (found == null) {
            throw new CommentNotFoundException("No Comment entry found with id: " + id);
        }
        LOGGER.debug("Deleting Comment entry: {}", found);

        repository.delete(found);

        return new CommentDTO(found, securityContextUtil.getUser_id());
    }

    @PreAuthorize("hasPermission('Comment', 'list')")
    @Transactional(readOnly = true)
    public CommentsDTO findAll(String job_id, Pageable pageable) {
        LOGGER.debug("Finding all Comments for job {}, page: {}", job_id, pageable);
        Page<Comment> comments = repository.findByJob_id(job_id, pageable);
        CommentsDTO dto = new CommentsDTO(comments);
        dto.feedDTOs(comments.getContent(), securityContextUtil.getUser_id());
        return dto;
    }

    @PreAuthorize("hasPermission('Comment', 'find')")
    @Transactional(readOnly = true, rollbackFor = {CommentNotFoundException.class})
    public CommentDTO findById(Long id) throws CommentNotFoundException {
        LOGGER.debug("Finding a Comment entry with id: {}", id);

        Comment found = repository.findOne(id);
        LOGGER.debug("Found Comment entry: {}", found);

        if (found == null) {
            throw new CommentNotFoundException("No Comment entry found with id: " + id);
        }

        return new CommentDTO(found, securityContextUtil.getUser_id());
    }

    @PreAuthorize("hasPermission('Comment', 'update')")
    @Transactional(rollbackFor = {CommentNotFoundException.class})
    public CommentDTO update(CommentDTO dto) throws CommentNotFoundException {
        LOGGER.debug("Updating comment with information: {}", dto);

        Comment found = repository.findOne(dto.getId());
        LOGGER.debug("Found Comment entry: {}", found);

        if (found == null) {
            throw new CommentNotFoundException("No Comment entry found with id: " + dto.getId());
        }

        LOGGER.debug("Found a comment entry: {}", found);

        found.update(dto.getContent(), dto.getAnonymous());

        return new CommentDTO(found, securityContextUtil.getUser_id());
    }
}
