package com.tiaonaer.ws.job.model;

/**
 * Created by echyong on 8/24/15.
 */
public class Job {
    private String id;
    private String job_company;
    private String job_sub_company;
    private String job_url;
    private String job_url_type;
    private String job_title;
    private String job_location;
    private String job_post_date;
    private String job_expire_date;
    private String job_description;
    private String job_index_date;
    private String job_category;
    private Boolean job_expired;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJob_company() {
        return job_company;
    }
    public void setJob_company(String job_company) {
        this.job_company = job_company;
    }

    public String getJob_sub_company() {
        return job_sub_company;
    }
    public void setJob_sub_company(String job_sub_company) {
        this.job_sub_company = job_sub_company;
    }

    public String getJob_url() {
        return job_url;
    }
    public void setJob_url(String job_url) {
        this.job_url = job_url;
    }

    public String getJob_url_type() { return job_url_type; }
    public void setJob_url_type(String job_url_type) {
        this.job_url_type = job_url_type;
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

    public String getJob_post_date() {
        return job_post_date;
    }
    public void setJob_post_date(String job_post_date) {
        this.job_post_date = job_post_date;
    }

    public String getJob_expire_date() {
        return job_expire_date;
    }
    public void setJob_expire_date(String job_expire_date) {
        this.job_expire_date = job_expire_date;
    }

    public String getJob_description() {
        return job_description;
    }
    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }

    public String getJob_index_date() {
        return job_index_date;
    }
    public void setJob_index_date(String job_index_date) {
        this.job_index_date = job_index_date;
    }

    public String getJob_category() {
        return job_category;
    }
    public void setJob_category(String job_category) {
        this.job_category = job_category;
    }

    public Boolean getJob_expired() { return job_expired; }
    public void setJob_expired(Boolean job_expired ) { this.job_expired = job_expired; }

    @Override
    public String toString() {
        return "Job [id=" + id + ", url =" + job_url + "]";
    }
}
