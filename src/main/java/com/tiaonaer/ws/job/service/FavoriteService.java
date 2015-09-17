package com.tiaonaer.ws.job.service;

import java.util.List;

import com.tiaonaer.ws.job.document.JobDocument;
import com.tiaonaer.ws.job.repository.solr.JobDocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tiaonaer.ws.job.model.Favorite;
import com.tiaonaer.ws.job.dto.FavoriteDTO;
import com.tiaonaer.ws.job.dto.FavoritesDTO;
import com.tiaonaer.ws.job.exception.CustomRequestException;
import com.tiaonaer.ws.security.util.SecurityContextUtil;
import com.tiaonaer.ws.job.repository.jpa.FavoriteRepository;

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
public class FavoriteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteService.class);

    @Resource
    private FavoriteRepository repository;

    @Resource
    private JobDocumentRepository solrRepository;

    @Resource
    private SecurityContextUtil securityContextUtil;

    @Transactional
    @PreAuthorize("hasPermission('Favorite', 'add')")
    public FavoriteDTO add(FavoriteDTO dto) throws CustomRequestException {
        LOGGER.debug("enter Favorite");

        if ( repository.count(dto.getJob_id(), securityContextUtil.getUser_id()) > 0 ) {
            throw new CustomRequestException("This job already in favorite list");
        } else if ( dto.getUser_id() != null && !dto.getUser_id().equals(securityContextUtil.getUser_id()) ) {
            throw new CustomRequestException("Can add favorite with user id of yourself");
        } else {
            Favorite model = new Favorite(dto.getJob_id(),securityContextUtil.getUser_id());
            Favorite persisted = repository.save(model);
            return modelToDTO(persisted,1);
        }
    }

    @PreAuthorize("hasPermission('Favorite', 'delete')")
    @Transactional
    public FavoriteDTO deleteById(Long id) throws CustomRequestException {
        LOGGER.debug("remove favorite for: {}", id);

        Favorite found = repository.findOne(id);
        LOGGER.debug("Found Favorite: {}", found);

        if (found == null) {
            throw new CustomRequestException("404 No Favorite entry found with id: " + id);
        } else {
            LOGGER.debug("Deleting Favorite entry: {}", found);
            if (!found.getUser_id().equals(securityContextUtil.getUser_id())) {
                throw new CustomRequestException("403 Try to delete an favorites not owned by yourself");
            } else {
                repository.delete(found);
                return new FavoriteDTO(found); // Dont need to populate additional info here.
            }
        }
    }

    @PreAuthorize("hasPermission('Favorite', 'list')")
    @Transactional(readOnly = true)
    public FavoritesDTO findAll(String job_id, Pageable pageable) {
        LOGGER.debug("Finding all Favorite for job {}, page: {}", job_id, pageable);

        Page<Favorite> favorites = null;
        int type = 0;
        if ( false /* user_id hasAuthority(ROLE_ADMIN) */ ) {
            if ( job_id != null  ) {
                favorites = repository.findByJob_id(job_id, pageable);
                type = 1;
            } else {
                favorites = repository.findAll(pageable);
                type = 2;
            }
        } else {
            // normal user to get own favorite lists
            favorites = repository.findByUser_id(securityContextUtil.getUser_id(), pageable);
            type = 0;
        }

        FavoritesDTO dto = new FavoritesDTO(favorites);
        for ( Favorite model: favorites.getContent() ) {
            dto.addFavorite(modelToDTO(model, type));
        }
        return dto;
    }

    public FavoriteDTO modelToDTO(Favorite model, int type) {
        FavoriteDTO dto = new FavoriteDTO(model);
        // fetching job_title and job_company here
        JobDocument doc = solrRepository.findByJobID(model.getJob_id());
        if ( doc == null ) {
            LOGGER.warn("404 Favorited Job not exist in repository, data mismatch, id: {}", model.getJob_id());
            dto.setJob_title("Sorry, this job already deleted!");
        } else {
            dto.setJob_title(doc.getJob_title());
            dto.setJob_company(doc.getJob_company());
        }
        return dto;
    }
}
