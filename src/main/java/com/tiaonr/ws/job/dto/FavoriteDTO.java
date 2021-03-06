package com.tiaonr.ws.job.dto;

import com.tiaonr.ws.job.model.Favorite;

/**
 * Created by echyong on 9/1/15.
 */
public class FavoriteDTO {

    private Long id;
    private String user_id;
    private String job_id;
    private String job_title;
    private String job_company;
    private String job_location;
    private String job_description;
    private String job_url;

    private Boolean job_expired;

    public FavoriteDTO() {

    }
    public FavoriteDTO(Favorite model) {
        this.id = model.getId();
        this.job_id = model.getJob_id();
        this.user_id = model.getUser_id();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_company() {
        return job_company;
    }

    public void setJob_company(String job_company) {
        this.job_company = job_company;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_location() {
        return job_location;
    }

    public void setJob_location(String job_location) {
        this.job_location = job_location;
    }

    public String getJob_description() {
        return job_description;
    }

    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }

    public String getJob_url() {
        return job_url;
    }

    public void setJob_url(String job_url) {
        this.job_url = job_url;
    }

    public Boolean getJob_expired() {
        return job_expired;
    }

    public void setJob_expired(Boolean job_expired) {
        this.job_expired = job_expired;
    }
}
