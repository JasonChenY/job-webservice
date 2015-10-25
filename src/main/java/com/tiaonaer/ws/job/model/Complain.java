package com.tiaonaer.ws.job.model;

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
@Table(name="complains")
public class Complain {
    public static final int MAX_LENGTH_CONTENTS = 500;

    public Complain() {}

    public Complain(String job_id, String user_id, String content, int type) {
        this.job_id = job_id;
        this.user_id = user_id;
        this.content = content;
        this.type = type;
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

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public String getApprove_user_id() {
        return approve_user_id;
    }

    public void setApprove_user_id(String approve_user_id) {
        this.approve_user_id = approve_user_id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id", nullable =  false)
    private String user_id;

    @Column(name = "job_id", nullable =  false)
    private String job_id;

    @Column(name = "type", nullable =  false)
    private int type;
    // 0 - job is applied/expired already; 1 - job with useless content

    @Column(name = "content", nullable = false, length = MAX_LENGTH_CONTENTS)
    private String content;

    @Column(name = "status", nullable = false)
    private int status;
    // 0 - pending;  1 - accepted;  2 - rejected;

    @Column(name = "creation_time", nullable = false)
    private Date creationTime;

    @Column(name = "approve_time", nullable = true)
    private Date approveTime;

    @Column(name = "approve_user_id", nullable =  false)
    private String approve_user_id;

    @PrePersist
    public void prePersist() {
        creationTime = new Date();
        status = 0; // pending
    }

    @PreUpdate
    public void preUpdate() {
        approveTime = new Date();
    }

    public void update(String approver, int status) {
        this.approve_user_id = approver;
        this.status = status;
    }
}

