package com.tiaonr.ws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.support.EmbeddedSolrServerFactoryBean;

import javax.annotation.Resource;

/**
 * @author jason.y.chen
 */
@Configuration
@EnableSolrRepositories("com.tiaonr.ws.job.repository.solr")
@Profile("dev")
public class EmbeddedSolrContext {

    private static final String PROPERTY_NAME_SOLR_SOLR_HOME = "solr.solr.home";

    @Resource
    private Environment environment;

    @Bean
    public EmbeddedSolrServerFactoryBean solrServerFactoryBean() {
        EmbeddedSolrServerFactoryBean factory = new EmbeddedSolrServerFactoryBean();

        factory.setSolrHome(environment.getRequiredProperty(PROPERTY_NAME_SOLR_SOLR_HOME));

        return factory;
    }

    @Bean
    public SolrTemplate solrTemplate() throws Exception {
        return new SolrTemplate(solrServerFactoryBean().getObject());
    }
}
