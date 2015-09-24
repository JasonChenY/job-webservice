package com.tiaonaer.ws.job.dto;

/**
 * Created by echyong on 8/25/15.
 */
import com.tiaonaer.ws.job.model.Complain;
import org.joda.time.DateTime;

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

    private String job_title;
    private String job_company;

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

}
