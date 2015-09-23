package com.tiaonaer.ws.job.dto;

/**
 * Created by echyong on 8/25/15.
 */
import com.tiaonaer.ws.job.model.Complain;
import org.joda.time.DateTime;
import java.util.Date;

public class ComplainDTO {
    public ComplainDTO() {

    }
    public ComplainDTO(Complain model) {
        this.id = model.getId();
        this.job_id = model.getJob_id();
        this.user_id = model.getUser_id();
        this.creationTime = model.getCreationTime();
        this.approveTime = model.getApproveTime();
        this.type = model.getType();
        this.content = model.getContent();
        this.status = model.getStatus();
        this.approve_user_id = model.getApprove_user_id();
    }

    private Long id;
    private String user_id;
    private String job_id;
    private DateTime creationTime;
    private DateTime approveTime;
    private int type;
    private String content;
    private int status;
    private String approve_user_id;

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

    public DateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = creationTime;
    }

    public DateTime getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(DateTime approveTime) {
        this.approveTime = approveTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getApprove_user_id() {
        return approve_user_id;
    }

    public void setApprove_user_id(String approve_user_id) {
        this.approve_user_id = approve_user_id;
    }

    // Following fields are purely used for Admin to approve Complain.
    // return job detail information when fetching complain,
    // - pros: dont need refetch again from server when switching to ComplainDetail.
    // - cos: will transfer the detail info even Admin wont check the detail info.
    //   By default, Admin only fetch Pending Complain, in theory should check all the detail to decide
    // whether accept or reject the complain.
    // One alternative is to only fetch job detail in Complain Detail View, should deal with invalid chars in job_id.

    private String job_title;
    private String job_company;
    private String job_sub_company;
    private int job_type;
    private String job_url;
    private int job_url_type;
    private String job_location;
    private Date job_post_date;
    private Date job_expire_date;
    private String job_description;
    private Date job_index_date;
    private String job_category_domain;
    private Boolean job_expired;

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
}
