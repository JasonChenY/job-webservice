package com.tiaonr.ws.config.oauth2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import java.nio.charset.Charset;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.common.AuthenticationScheme;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.SystemDefaultHttpClient;
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
    public OAuth2ProtectedResourceDetails qq() {
        AuthorizationCodeResourceDetails details = new ExtendedBaseOAuth2ProtectedResourceDetails();
        details.setId("qq");
        details.setClientId("101260439");
        details.setClientSecret("ccf8b37bf112fd97793834027fb4e3cd");
        details.setAccessTokenUri("https://graph.qq.com/oauth2.0/token");
        details.setUserAuthorizationUri("https://graph.qq.com/oauth2.0/authorize");
        details.setTokenName("access_token");
        details.setScope(Arrays.asList("get_user_info"));
        details.setAuthenticationScheme(AuthenticationScheme.query);
        details.setClientAuthenticationScheme(AuthenticationScheme.form);
        return details;
    }

    @Bean
    public OAuth2ProtectedResourceDetails weibo() {
        AuthorizationCodeResourceDetails details = new ExtendedBaseOAuth2ProtectedResourceDetails();
        details.setId("weibo");
        details.setClientId("408623291");
        details.setClientSecret("6bad87c8925908ec8bd66795576153ba");
        details.setAccessTokenUri("https://api.weibo.com/oauth2/access_token");
        details.setUserAuthorizationUri("https://api.weibo.com/oauth2/authorize");
        details.setTokenName("access_token");
        details.setScope(Arrays.asList("email"));
        details.setAuthenticationScheme(AuthenticationScheme.query);
        details.setClientAuthenticationScheme(AuthenticationScheme.form);
        return details;
    }

    @Bean
    public OAuth2ProtectedResourceDetails weixin() {
        AuthorizationCodeResourceDetails details = new ExtendedBaseOAuth2ProtectedResourceDetails();
        details.setId("weixin");
        details.setClientIdName("appid");
        details.setClientId("wx5802184b308ffb9d");
        details.setClientSecret("9cc179ed74f95ed7ca7f19720647263a");
        details.setAccessTokenUri("https://api.weixin.qq.com/sns/oauth2/access_token");
        details.setUserAuthorizationUri("https://open.weixin.qq.com/connect/qrconnect");
        details.setTokenName("access_token");
        details.setScope(Arrays.asList("snsapi_login"));
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

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public OAuth2RestTemplate qqRestTemplate(OAuth2ClientContext clientContext) {
        OAuth2RestTemplate template = new OAuth2RestTemplate(qq(), clientContext);

        // Failed because no suitable message converter for access token, because tencent use text/html,
        // add additioanl HttpMessageConverter here doesnt take effect.
        //
        // Use HttpComponentsClientHttpRequestFactory to get wire logs, but also failed.
        // Finally fallback to recompile spring-web's RestTemplate.java to add raw log.
        //
        // Solution: recompile spring-security-oauth2, changing FormOAuth2AccessTokenMessageConverter.java to support text/html.
        /*
        client.RestTemplate (RestTemplate.java:handleResponse(641)) - POST request for "https://graph.qq.com/oauth2.0/token" resulted in 200 (OK)
        client.RestTemplate (RestTemplate.java:handleResponse(651)) - Server : tws
        client.RestTemplate (RestTemplate.java:handleResponse(651)) - Date : Thu, 05 Nov 2015 02:40:38 GMT
        client.RestTemplate (RestTemplate.java:handleResponse(651)) - Content-Type : text/html; charset=utf-8
        client.RestTemplate (RestTemplate.java:handleResponse(651)) - Content-Length : 111
        client.RestTemplate (RestTemplate.java:handleResponse(651)) - Connection : keep-alive
        client.RestTemplate (RestTemplate.java:handleResponse(651)) - Keep-Alive : timeout=15
        client.RestTemplate (RestTemplate.java:handleResponse(651)) - Cache-Control : no-cache
        client.RestTemplate (RestTemplate.java:handleResponse(645)) - access_token=1E94EE8B1093EF9789A9DB07C9BA469F&expires_in=7776000&refresh_token=1511DCF2F7C713C4F746D3327B063BB0
        */
        CloseableHttpClient httpClient = HttpClients.createMinimal();
        HttpComponentsClientHttpRequestFactory httpClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        template.setRequestFactory(httpClientHttpRequestFactory);

        // After getting access token, need get openid before invoke any api,
        // Again Tencent return JSON format data, but content-type is text/html.
        // We can register custom MessageConverter here (This doesnt impact convert for access token)
        /*
        http.wire (Wire.java:wire(72)) - http-outgoing-0 >> "GET /oauth2.0/me?access_token=1E94EE8B1093EF9789A9DB07C9BA469F HTTP/1.1[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 >> "Accept: text/html[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 >> "Host: graph.qq.com[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 >> "Connection: Keep-Alive[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 >> "User-Agent: Apache-HttpClient/4.4.1 (Java/1.7.0_79)[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 >> "[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 << "HTTP/1.1 200 OK[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 << "Server: tws[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 << "Date: Thu, 05 Nov 2015 05:24:31 GMT[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 << "Content-Type: text/html; charset=utf-8[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 << "Content-Length: 83[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 << "Connection: keep-alive[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 << "Keep-Alive: timeout=15[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 << "Cache-Control: no-cache[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 << "[\r][\n]"
        http.wire (Wire.java:wire(72)) - http-outgoing-0 << "callback( {"client_id":"101260439","openid":"91EF4826024612D98DD712EC4AA11B9B"} );
        */
        StringHttpMessageConverter stringconverter = new StringHttpMessageConverter();
        stringconverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text","html", Charset.forName("utf-8"))));
        /*
        http-outgoing-2 >> "GET /user/get_user_info?oauth_consumer_key=101260439&format=json&openid=91EF4826024612D98DD712EC4AA11B9B&access_token=1E94EE8B1093EF9789A9DB07C9BA469F HTTP/1.1[\r][\n]"
        http-outgoing-2 >> "Accept: text/html[\r][\n]"
        http-outgoing-2 >> "Host: graph.qq.com[\r][\n]"
        http-outgoing-2 >> "Connection: Keep-Alive[\r][\n]"
        http-outgoing-2 >> "User-Agent: Apache-HttpClient/4.4.1 (Java/1.7.0_79)[\r][\n]"
        http-outgoing-2 >> "[\r][\n]"
        http-outgoing-2 << "HTTP/1.1 200 OK[\r][\n]"
        http-outgoing-2 << "Server: tws[\r][\n]"
        http-outgoing-2 << "Date: Thu, 05 Nov 2015 13:10:27 GMT[\r][\n]"
        http-outgoing-2 << "Content-Type: text/html; charset=utf-8[\r][\n]"
        http-outgoing-2 << "Content-Length: 786[\r][\n]"
        http-outgoing-2 << "Connection: keep-alive[\r][\n]"
        http-outgoing-2 << "Keep-Alive: timeout=15[\r][\n]"
        http-outgoing-2 << "[\r][\n]"
        http-outgoing-2 << "{[\n]"
        http-outgoing-2 << "    "ret": 0,[\n]"
        http-outgoing-2 << "    "msg": "",[\n]"
        http-outgoing-2 << "    "is_lost":0,[\n]"
        http-outgoing-2 << "    "nickname": "[0xe9][0x99][0x88][0xe5][0x8b][0x87]",[\n]"
        http-outgoing-2 << "    "gender": "[0xe7][0x94][0xb7]",[\n]"
        http-outgoing-2 << "    "province": "[0xe4][0xb8][0x8a][0xe6][0xb5][0xb7]",[\n]"
        http-outgoing-2 << "    "city": "",[\n]"
        http-outgoing-2 << "    "year": "1979",[\n]"
        http-outgoing-2 << "    "figureurl": "http:\/\/qzapp.qlogo.cn\/qzapp\/101260439\/91EF4826024612D98DD712EC4AA11B9B\/30",[\n]"
        http-outgoing-2 << "    "figureurl_1": "http:\/\/qzapp.qlogo.cn\/qzapp\/101260439\/91EF4826024612D98DD712EC4AA11B9B\/50",[\n]"
        http-outgoing-2 << "    "figureurl_2": "http:\/\/qzapp.qlogo.cn\/qzapp\/101260439\/91EF4826024612D98DD712EC4AA11B9B\/100",[\n]"
        http-outgoing-2 << "    "figureurl_qq_1": "http:\/\/q.qlogo.cn\/qqapp\/101260439\/91EF4826024612D98DD712EC4AA11B9B\/40",[\n]"
        http-outgoing-2 << "    "figureurl_qq_2": "http:\/\/q.qlogo.cn\/qqapp\/101260439\/91EF4826024612D98DD712EC4AA11B9B\/100",[\n]"
        http-outgoing-2 << "    "is_yellow_vip": "0",[\n]"
        http-outgoing-2 << "    "vip": "0",[\n]"
        http-outgoing-2 << "    "yellow_vip_level": "0",[\n]"
        http-outgoing-2 << "    "level": "0",[\n]"
        http-outgoing-2 << "    "is_yellow_year_vip": "0"[\n]"
        http-outgoing-2 << "}[\n]"
        */
        MappingJackson2HttpMessageConverter jsonconverter = new MappingJackson2HttpMessageConverter();
        jsonconverter.setSupportedMediaTypes(Arrays.asList(new MediaType("text","html", Charset.forName("utf-8"))));
        template.setMessageConverters(Arrays.<HttpMessageConverter<?>> asList(stringconverter, jsonconverter));

        return template;
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public OAuth2RestTemplate weiboRestTemplate(OAuth2ClientContext clientContext) {
        OAuth2RestTemplate template = new OAuth2RestTemplate(weibo(), clientContext);
        /*
        POST request for "https://api.weibo.com/oauth2/access_token" resulted in 200 (OK)
        Server : nginx/1.6.1
        Date : Thu, 05 Nov 2015 14:09:46 GMT
        Content-Type : text/plain;charset=UTF-8
        Content-Length : 117
        Connection : keep-alive
        Pragma : No-cache
        Cache-Control : no-cache
        Expires : Thu, 01 Jan 1970 00:00:00 GMT
        Api-Server-IP : 10.73.89.48
        {"access_token":"2.00EETB7B0PaXe83c06e23e1408Exr_","remind_in":"157679999","expires_in":157679999,"uid":"1300667624"}
        */
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        template.setMessageConverters(Arrays.<HttpMessageConverter<?>> asList(converter));
        return template;
    }

    @Bean
    @Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
    public OAuth2RestTemplate weixinRestTemplate(OAuth2ClientContext clientContext) {
        OAuth2RestTemplate template = new OAuth2RestTemplate(weixin(), clientContext);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON));
        template.setMessageConverters(Arrays.<HttpMessageConverter<?>> asList(converter));
        return template;
    }
}
