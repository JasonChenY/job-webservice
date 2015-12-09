package com.tiaonr.ws.job.service;

import java.util.List;

import com.tiaonr.ws.job.document.JobDocument;
import com.tiaonr.ws.job.dto.CommentDTO;
import com.tiaonr.ws.job.exception.CommentNotFoundException;
import com.tiaonr.ws.job.model.Comment;
import com.tiaonr.ws.job.repository.solr.JobDocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tiaonr.ws.job.model.Complain;
import com.tiaonr.ws.job.dto.ComplainDTO;
import com.tiaonr.ws.job.dto.ComplainsDTO;
import com.tiaonr.ws.job.exception.CustomRequestException;
import com.tiaonr.ws.security.util.SecurityContextUtil;
import com.tiaonr.ws.job.repository.jpa.ComplainRepository;

import org.springframework.context.annotation.ImportResource;
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
public class ComplainService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComplainService.class);

    @Resource
    private ComplainRepository repository;

    @Resource
    private JobDocumentRepository solrRepository;

    @Resource
    private SecurityContextUtil securityContextUtil;

    @Transactional
    @PreAuthorize("hasPermission('Complain', 'add')")
    public ComplainDTO add(ComplainDTO dto) throws CustomRequestException {
        LOGGER.debug("add Complain");

        if ( repository.count(dto.getJob_id(), securityContextUtil.getUser_id()) > 0 ) {
            throw new CustomRequestException("You have complained about this job before");
        } else if ( dto.getUser_id() != null && !dto.getUser_id().equals(securityContextUtil.getUser_id()) ) {
            throw new CustomRequestException("Can only complain with user id of yourself");
        } else {
            Complain model = new Complain(dto.getJob_id(),
                                          securityContextUtil.getUser_id(),
                                          dto.getContent(),
                                          dto.getType());
            Complain persisted = repository.save(model);
            return modelToDTO(persisted);
        }
    }

    @PreAuthorize("hasPermission('Complain', 'list')")
    @Transactional(readOnly = true)
    public ComplainsDTO findAll(Pageable pageable) {
        LOGGER.debug("Finding user's own Complains page: {}", pageable);

        // normal user to get own complains lists
        Page<Complain> complains = repository.findByUser_id(securityContextUtil.getUser_id(), pageable);

        ComplainsDTO dto = new ComplainsDTO(complains);
        for ( Complain model: complains.getContent() ) {
            dto.addComplain(modelToDTO(model));
        }
        return dto;
    }

    @PreAuthorize("hasPermission('Complain', 'adminlist')")
    @Transactional(readOnly = true)
    public ComplainsDTO findAll(String user_id, int type, int status, Pageable pageable) {
        LOGGER.debug("Finding all Complains Optional user: {}, page: {}", user_id, pageable);

        Page<Complain> complains = null;
        if ( user_id != null  )
            complains = repository.findByUser_id(user_id, pageable);
        else {
            // This can be optimized with JdbcTemplate later
            if ( type != -1 && status != -1 )
                complains = repository.findByTypeStatus(type, status, pageable);
            else if ( type != -1 )
                complains = repository.findByType(type, pageable);
            else if ( status != -1 )
                complains = repository.findByStatus(status, pageable);
            else
                complains = repository.findAll(pageable);
        }

        ComplainsDTO dto = new ComplainsDTO(complains);
        for ( Complain model: complains.getContent() ) {
            dto.addComplain(modelToDTO(model));
        }
        return dto;
    }

    @PreAuthorize("hasPermission('Complain', 'update')")
    @Transactional
    public ComplainDTO update(ComplainDTO dto) throws CustomRequestException {
        LOGGER.debug("Updating complain with information: {}", dto);

        Complain model = repository.findOne(dto.getId());
        LOGGER.debug("Found complain entry: {}", model);

        if (model == null) {
            throw new CustomRequestException("No Complain entry found with id: " + dto.getId());
        } else {
            LOGGER.debug("Found the complain entry: {}", model);

            model.update(securityContextUtil.getUser_id(), dto.getStatus());

            if ((dto.getType() == 0) && (dto.getStatus() == 1)) {
                JobDocument doc = solrRepository.findByJobID(model.getJob_id());
                if (doc == null) {
                    throw new CustomRequestException("404 Complained Job not exist in repository, data mismatch");
                } else {
                    doc.setJob_expired(true);
                    solrRepository.update(doc);

                    ComplainDTO dtoret = new ComplainDTO(model);
                    dtoret.setJob_title(doc.getJob_title());
                    dtoret.setJob_company(doc.getJob_company());
                    return dtoret;
                }
            } else {
                return modelToDTO(model);
            }
        }
    }

    public ComplainDTO modelToDTO(Complain model) {
        ComplainDTO dto = new ComplainDTO(model);
        // fetching job_title and job_company here
        JobDocument doc = solrRepository.findByJobID(model.getJob_id());
        if ( doc == null ) {
            LOGGER.warn("404 Complained Job not exist in repository, data mismatch, id: {}", model.getJob_id());
            dto.setJob_title("Job Removed From Database already!");
            return null;
        } else {
            dto.setJob_title(doc.getJob_title());
            dto.setJob_company(doc.getJob_company());
            return dto;
        }
    }
}
