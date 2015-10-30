package com.tiaonr.ws.job.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Type;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

/**
 * Created by echyong on 8/24/15.
 */
@Entity
@Table(name="comments")
public class Comment {
    public static final int MAX_LENGTH_CONTENTS = 500;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "creation_time", nullable = false)
    private Date creationTime;

    @Column(name = "content", nullable = false, length = MAX_LENGTH_CONTENTS)
    private String content;

    @Column(name = "modification_time", nullable = true)
    private Date modificationTime;

    @Column(name = "anonymous", nullable = false)
    private Boolean anonymous;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Column(name = "user_id", nullable =  false)
    private String user_id;

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    @Column(name = "job_id", nullable =  false)
    private String job_id;

    public Comment() {

    }

    public static Builder getBuilder(String job_id) {
        return new Builder(job_id);
    }

    public Long getId() {
        return id;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public String getContent() {
        return content;
    }

    public Date getModificationTime() {
        return modificationTime;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    @PrePersist
    public void prePersist() {
        creationTime = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        modificationTime = new Date();
    }

    public void update(String content, Boolean anonymous) {
        this.content = content;
        this.anonymous = anonymous;
    }

    public static class Builder {
        private Comment built;
        public Builder(String job_id) {
            built = new Comment();
            built.job_id = job_id;
            built.anonymous = false;
        }
        public Comment build() {
            return built;
        }
        public Builder content(String content) {
            built.content = content;
            return this;
        }
        public Builder anonymous(Boolean anonymous) {
            built.anonymous = anonymous;
            return this;
        }
        public Builder user_id(String user_id) {
            built.user_id = user_id;
            return this;
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

