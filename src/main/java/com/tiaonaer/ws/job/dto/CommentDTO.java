package com.tiaonaer.ws.job.dto;

/**
 * Created by echyong on 8/25/15.
 */
import com.tiaonaer.ws.job.model.Comment;
import java.util.Date;

public class CommentDTO {
    public CommentDTO() {

    }
    public CommentDTO(Comment model, String user_id) {
        this.id = model.getId();
        this.creationTime = model.getCreationTime();
        this.modificationTime = model.getModificationTime();
        this.content = model.getContent();
        this.job_id = model.getJob_id();
        this.anonymous = model.getAnonymous();
        this.user_id = model.getUser_id();
        if ( model.getAnonymous() && !model.getUser_id().equals(user_id)) {
            this.setUser_id("匿名");
        }
    }
    private Long id;
    private Date creationTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(Date modificationTime) {
        this.modificationTime = modificationTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
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

    private Date modificationTime;
    private String content;
    private Boolean anonymous;
    private String user_id;
    private String job_id;
}
