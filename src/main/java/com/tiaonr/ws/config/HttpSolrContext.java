package com.tiaonr.ws.config;

import java.util.Arrays;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.support.HttpSolrServerFactoryBean;

import javax.annotation.Resource;

/**
 * @author jason.y.chen
 */
@Configuration
@EnableSolrRepositories("com.tiaonr.ws.job.repository.solr")
@Profile("prod")
public class HttpSolrContext {

    private static final String PROPERTY_NAME_SOLR_SERVER_URL = "solr.server.url";
    private static final String PROPERTY_NAME_SOLR_SERVER_USE_AUTH = "solr.server.use.auth";
    private static final String PROPERTY_NAME_SOLR_SERVER_AUTH_USERNAME = "solr.server.auth.username";
    private static final String PROPERTY_NAME_SOLR_SERVER_AUTH_PASSWORD = "solr.server.auth.password";

    @Resource
    private Environment environment;

    @Bean
    public HttpSolrServerFactoryBean solrServerFactoryBean() {
        HttpSolrServerFactoryBean factory = new HttpSolrServerFactoryBean();

        factory.setUrl(environment.getRequiredProperty(PROPERTY_NAME_SOLR_SERVER_URL));

        return factory;
    }

    @Bean
    public SolrTemplate solrTemplate() throws Exception {
        SolrServer solrServer = solrServerFactoryBean().getObject();

        if ( environment.getRequiredProperty(PROPERTY_NAME_SOLR_SERVER_USE_AUTH, boolean.class) ) {
            HttpSolrServer httpSolrServer = (HttpSolrServer) solrServer;

            Credentials credentials = new UsernamePasswordCredentials(
                    environment.getRequiredProperty(PROPERTY_NAME_SOLR_SERVER_AUTH_USERNAME),
                    environment.getRequiredProperty(PROPERTY_NAME_SOLR_SERVER_AUTH_PASSWORD));

            AbstractHttpClient httpClient = (AbstractHttpClient) httpSolrServer.getHttpClient();
            httpClient.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY), credentials);
            httpClient.getParams().setParameter("http.auth.target-scheme-pref", Arrays.asList(new String[]{"BASIC"}));
        }
        return new SolrTemplate(solrServer);
    }
}
