package com.tiaonaer.ws.job.dto;

import com.tiaonaer.ws.job.model.Favorite;

/**
 * Created by echyong on 9/1/15.
 */
public class FavoriteDTO {
    Long id;
    String job_id;
    String job_title;
    String job_company;
    String user_id;

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

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_company() {
        return job_company;
    }

    public void setJob_company(String job_company) {
        this.job_company = job_company;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
