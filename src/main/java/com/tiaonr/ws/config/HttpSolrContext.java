package com.tiaonr.ws.config;

import java.io.IOException;
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

import org.apache.http.client.CredentialsProvider;
import org.apache.http.HttpRequest;
import org.apache.http.HttpHost;
import org.apache.http.HttpException;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;

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

    private static class PreemptiveAuthInterceptor implements HttpRequestInterceptor {
        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
            AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);

            // If no auth scheme avaialble yet, try to initialize it
            // preemptively
            if (authState.getAuthScheme() == null) {
                CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(ClientContext.CREDS_PROVIDER);
                HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
                Credentials creds = credsProvider.getCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()));
                if (creds == null)
                    throw new HttpException("No credentials for preemptive authentication");
                authState.setAuthScheme(new BasicScheme());
                authState.setCredentials(creds);
            }
        }
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
            httpClient.addRequestInterceptor(new PreemptiveAuthInterceptor(),0);
        }
        return new SolrTemplate(solrServer);
    }
}
