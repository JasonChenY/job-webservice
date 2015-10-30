package com.tiaonr.ws.job.model;

import javax.persistence.*;

/**
 * Created by echyong on 8/27/15.
 */
@Entity
@Table(name="favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(name = "job_id", nullable =  false)
    String job_id;

    @Column(name = "user_id", nullable =  false)
    String user_id;

    public Favorite() {

    }

    public Favorite(String job_id, String user_id) {
        this.job_id = job_id;
        this.user_id = user_id;
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
}
