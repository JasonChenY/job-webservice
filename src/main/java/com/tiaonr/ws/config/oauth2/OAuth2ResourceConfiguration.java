package com.tiaonr.ws.config.oauth2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;

/**
 * Created by echyong on 10/14/15.
 */
@PropertySource("classpath:oauth2.properties")
@Configuration
@EnableOAuth2Client
public class OAuth2ResourceConfiguration {
    @Value("${testServer.accessTokenUri}")
    private String testServerAccessTokenUri;

    @Value("${testServer.authorizationUri}")
    private String testServerAuthorizationUri;

    // To get an access code (authorise_code) before authentication, this is necessary for third party login function.
    private static class ExtendedBaseOAuth2ProtectedResourceDetails extends AuthorizationCodeResourceDetails {
        public boolean isClientOnly() {
            return true;
        }
    }

    @Bean
    public OAuth2ProtectedResourceDetails facebook() {
        AuthorizationCodeResourceDetails details = new ExtendedBaseOAuth2ProtectedResourceDetails();
        details.setId("facebook");
        details.setClientId("233668646673605");
        details.setClientSecret("33b17e044ee6a4fa383f46ec6e28ea1d");
        details.setAccessTokenUri("https://graph.facebook.com/oauth/access_token");
        details.setUserAuthorizationUri("https://www.facebook.com/dialog/oauth");
        details.setTokenName("oauth_token");
        details.setAuthenticationScheme(AuthenticationScheme.query);
        details.setClientAuthenticationScheme(AuthenticationScheme.form);
        return details;
    }

    @Bean
    public OAuth2ProtectedResourceDetails testServer() {
        AuthorizationCodeResourceDetails details = new ExtendedBaseOAuth2ProtectedResourceDetails();
        details.setId("oauth2-server");
        details.setClientId("tiaonr");
        details.setClientSecret("tiaonrsecret");
        details.setAccessTokenUri(testServerAccessTokenUri);
        details.setUserAuthorizationUri(testServerAuthorizationUri);
        details.setTokenName("access_token");
        details.setScope(Arrays.asList("read"));
        details.setAuthenticationScheme(AuthenticationScheme.query);
        details.setClientAuthenticationScheme(AuthenticationScheme.form);
        return details;
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public OAuth2RestTemplate facebookRestTemplate(OAuth2ClientContext clientContext) {
        OAuth2RestTemplate template = new OAuth2RestTemplate(facebook(), clientContext);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.valueOf("text/javascript")));
        template.setMessageConverters(Arrays.<HttpMessageConverter<?>> asList(converter));
        return template;
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public OAuth2RestTemplate testServerRestTemplate(OAuth2ClientContext clientContext) {
        OAuth2RestTemplate template = new OAuth2RestTemplate(testServer(), clientContext);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.valueOf("text/javascript")));
        template.setMessageConverters(Arrays.<HttpMessageConverter<?>> asList(converter));
        return template;
    }
}