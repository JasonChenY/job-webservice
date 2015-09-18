package com.tiaonaer.ws.job.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

import com.tiaonaer.ws.common.util.LocaleContextHolderWrapper;
import com.tiaonaer.ws.job.dto.FavoriteDTO;
import com.tiaonaer.ws.job.dto.FavoritesDTO;
import com.tiaonaer.ws.job.dto.JobsFacetDTO;
import com.tiaonaer.ws.job.model.Favorite;
import com.tiaonaer.ws.job.service.FavoriteService;
import com.tiaonaer.ws.job.exception.CustomRequestException;

/**
 * @author jason.y.chen
 */
@Controller
public class FavoriteController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteController.class);

    @Resource
    private FavoriteService service;

    @Resource
    private LocaleContextHolderWrapper localeHolderWrapper;

    @Resource
    private MessageSource messageSource;

    //curl -v -H "Content-Type:application/json" -H "Cookie:JSESSIONID=k2" -X POST -d "{\"job_id\":\"http://new.abb.com/cn/careers/job-advertisement/details/57467126/area-sales-manager-based-in-beijing\"}" http://192.168.137.128:8080/api/favorite
    @RequestMapping(value = "/api/favorite", method = RequestMethod.POST)
    @ResponseBody
    public FavoriteDTO add(@RequestBody FavoriteDTO dto) throws CustomRequestException {
        LOGGER.debug("Adding a new Favorite entry with information: {}", dto);
        return service.add(dto);
    }

    //curl -v -H "Content-Type:application/json" -H "Cookie:JSESSIONID=1fag4o7y1hr4u1ytxgbcl1jfw;" -X GET "http://192.168.137.128:8080/api/favorite?page.page=2&page.size=10&page.sort=id&page.sort.dir=desc"
    @RequestMapping(value = "/api/favorite/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public FavoriteDTO deleteById(@PathVariable("id") Long id) throws CustomRequestException {
        LOGGER.debug("Deleting a Favorite with id: {}", id);
        return  service.deleteById(id);
    }

    @RequestMapping(value = "/api/favorite", method = RequestMethod.GET, params="!job_id")
    @ResponseBody
    public FavoritesDTO favoritedJobList(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        LOGGER.debug("Finding all favorited joblist, page: {} ", pageable);
        return service.findFavoriteList(null, pageable);
    }

    //curl -v -H "Content-Type:application/json" -H "Cookie:JSESSIONID=1fag4o7y1hr4u1ytxgbcl1jfw;" -X GET http://192.168.137.128:8080/api/favorite
    //curl -v -H "Content-Type:application/json" -H "Cookie:JSESSIONID=1fag4o7y1hr4u1ytxgbcl1jfw;" -X GET "http://192.168.137.128:8080/api/favorite?page.page=2&page.size=10&page.sort=id&page.sort.dir=desc"
    @RequestMapping(value = "/api/favorite", method = RequestMethod.GET, params="job_id")
    @ResponseBody
    public FavoritesDTO favoriteList(
            @RequestParam(value = "job_id", required = true) String job_id,
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
        LOGGER.debug("Finding all favorites list with job_id {} page: {} ", job_id, pageable);
        return service.findFavoriteList(job_id, pageable);
    }

    @ExceptionHandler(CustomRequestException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleCommentNotFoundException(CustomRequestException ex) {
        LOGGER.debug("handling CommentNotFoundException");
        return ex.getMessage();
    }
}
