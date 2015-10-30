package com.tiaonr.ws.job.dto;

import org.springframework.data.domain.Page;
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

    public PageNavigation(Page<?> page) {
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.totalPages = page.getTotalPages();
        this.numberOfElements = page.getNumberOfElements();
        this.totalElements = page.getTotalElements();
        this.nextPagable = page.nextPageable();
        this.previousPagable = page.previousPageable();
    }
}
