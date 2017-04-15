package com.tiaonr.ws.job.document;

import java.util.List;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;
import com.tiaonr.ws.job.document.SearchableJobDefinition;

/**
 * @author Christoph Strobl
 */
@SolrDocument(solrCoreName = "jobs")
public class JobDocument implements SearchableJobDefinition {
    private @Id @Indexed String id;
    private @Indexed(JOB_COMPANY) String job_company;
    private @Indexed(JOB_SUB_COMPANY) String job_sub_company;
    private @Indexed(JOB_TYPE) int job_type;
    private @Field(JOB_URL) String job_url;
    private @Indexed(JOB_URL_TYPE) int job_url_type;
    private @Indexed(JOB_TITLE) String job_title;
    private @Indexed(JOB_LOCATION) String job_location;
    private @Indexed(JOB_POST_DATE) Date job_post_date;
    private @Indexed(JOB_EXPIRE_DATE) Date job_expire_date;
    private @Indexed(JOB_DESCRIPTION) String job_description;
    private @Indexed(JOB_DESCRIPTION_STRIPPED) String job_description_stripped;
    private @Indexed(JOB_INDEX_DATE) Date job_index_date;
    private @Indexed(JOB_CATEGORY_DOMAIN) String job_category_domain;
    private @Indexed(JOB_EXPIRED) Boolean job_expired;

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

    public int getJob_type() { return job_type; }
    public void setJob_type(int job_type) {
        this.job_url_type = job_type;
    }

    public String getJob_url() {
        return job_url;
    }
    public void setJob_url(String job_url) {
        this.job_url = job_url;
    }

    public int getJob_url_type() { return job_url_type; }
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

    public Date getJob_expire_date() { return job_expire_date;}
    public void setJob_expire_date(Date job_expire_date) {
        this.job_expire_date = job_expire_date;
    }

    public String getJob_description() {
        return job_description;
    }
    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }

    public String getJob_description_stripped() {
        return job_description_stripped;
    }
    public void setJob_description_stripped(String job_description_stripped) {
        this.job_description_stripped = job_description_stripped;
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

    public Boolean getJob_expired() {return job_expired; }
    public void setJob_expired(Boolean job_expired) { this.job_expired = job_expired;}

    @Override
    public String toString() {
        return "Job [id=" + id + ", url =" + job_url + "]";
    }
}
