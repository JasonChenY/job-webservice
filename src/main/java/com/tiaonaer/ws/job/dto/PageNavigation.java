package com.tiaonaer.ws.job.dto;

import org.springframework.data.domain.Pageable;

/**
 * Created by echyong on 8/26/15.
 */
public class PageNavigation {
    public int pageNumber;
    public int pageSize;
    public int totalPages;
    public int numberOfElements;
    public long totalElements;
    public Pageable nextPagable;
    public Pageable previousPagable;
}
