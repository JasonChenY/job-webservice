package com.tiaonr.ws.job.dto;

/**
 * Created by echyong on 8/25/15.
 */
import java.util.Date;
import com.tiaonr.ws.job.document.JobDocument;

public class JobDTO {
    private String id;
    private String job_company;
    private String job_sub_company;
    private int job_type;
    private String job_url;
    private int job_url_type;
    private String job_title;
    private String job_location;
    private Date job_post_date;
    private Date job_expire_date;
    private String job_description;
    private Date job_index_date;
    private String job_category_domain;
    private Boolean job_expired;
    private long favorite_id;
    private int favorities_num;
    private int comments_num;
    private Boolean complain_pending;

    public JobDTO() {};
    public JobDTO(JobDocument doc) {
        this.id = doc.getId();
        this.job_company = doc.getJob_company();
        this.job_sub_company = doc.getJob_sub_company();
        this.job_type = doc.getJob_type();
        this.job_url = doc.getJob_url();
        this.job_url_type = doc.getJob_url_type();
        this.job_title = doc.getJob_title();
        this.job_location = doc.getJob_location();
        this.job_post_date = doc.getJob_post_date();
        this.job_expire_date = doc.getJob_expire_date();
        this.job_description = doc.getJob_description();
        this.job_index_date = doc.getJob_index_date();
        this.job_category_domain = doc.getJob_category_domain();
        this.job_expired = doc.getJob_expired();
        this.favorite_id = 0;
        this.favorities_num = 0;
        this.comments_num = 0;
        this.complain_pending = false;
    }

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

    public int getJob_type() {
        return job_type;
    }

    public void setJob_type(int job_type) {
        this.job_type = job_type;
    }

    public String getJob_url() {
        return job_url;
    }

    public void setJob_url(String job_url) {
        this.job_url = job_url;
    }

    public int getJob_url_type() {
        return job_url_type;
    }

    public void setJob_url_type(int job_url_type) {
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

    public Date getJob_post_date() {
        return job_post_date;
    }

    public void setJob_post_date(Date job_post_date) {
        this.job_post_date = job_post_date;
    }

    public Date getJob_expire_date() {
        return job_expire_date;
    }

    public void setJob_expire_date(Date job_expire_date) {
        this.job_expire_date = job_expire_date;
    }

    public String getJob_description() {
        return job_description;
    }

    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }

    public Date getJob_index_date() {
        return job_index_date;
    }

    public void setJob_index_date(Date job_index_date) {
        this.job_index_date = job_index_date;
    }

    public String getJob_category_domain() {
        return job_category_domain;
    }

    public void setJob_category_domain(String job_category_domain) {
        this.job_category_domain = job_category_domain;
    }

    public Boolean getJob_expired() {
        return job_expired;
    }

    public void setJob_expired(Boolean job_expired) {
        this.job_expired = job_expired;
    }

    public long getFavorite_id() {
        return favorite_id;
    }

    public void setFavorite_id(long favorite_id) {
        this.favorite_id = favorite_id;
    }

    public int getFavorities_num() {
        return favorities_num;
    }

    public void setFavorities_num(int favorities_num) {
        this.favorities_num = favorities_num;
    }

    public int getComments_num() {
        return comments_num;
    }

    public void setComments_num(int comments_num) {
        this.comments_num = comments_num;
    }

    public Boolean getComplain_pending() {
        return complain_pending;
    }

    public void setComplain_pending(Boolean complain_pending) {
        this.complain_pending = complain_pending;
    }
}
