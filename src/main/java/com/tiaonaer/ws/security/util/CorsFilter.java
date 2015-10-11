package com.tiaonaer.ws.security.util;

/**
 * Created by echyong on 9/30/15.
 */
/*
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

public class CorsFilter extends OncePerRequestFilter  {
    static final String ORIGIN = "Origin";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //if (request.getHeader(ORIGIN) == null || request.getHeader(ORIGIN).equals("null")) {
            // - Should use concrete url instead of * when switch from http to https,
            //  otherwise api/user is rejected due to CORS, but ok for api/login.
            // - Cant specify multiple origin, solution to use origin from request.
            //
            response.addHeader("Access-Control-Allow-Origin", "http://www.tiaonaer.com");
            response.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,HEAD,DELETE,OPTION");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.addHeader("Access-Control-Max-Age", "10000");

            String reqHead = request.getHeader("Access-Control-Request-Headers");
            if ( reqHead == null || reqHead.isEmpty() ) {
                reqHead = "Origin, X-Requested-With, Content-Type, Accept, If-Modified-Since";
            }
            response.addHeader("Access-Control-Allow-Headers", reqHead);
        //}
        if (request.getMethod().equals("OPTIONS")) {
            try {
                response.getWriter().print("OK");
                response.getWriter().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            filterChain.doFilter(request, response);
        }
    }
}
*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorsFilter implements Filter
{
    private static final Logger log = LoggerFactory.getLogger(CorsFilter.class);

    // Request headers
    private static final String ORIGIN_HEADER = "Origin";
    private static final String ACCESS_CONTROL_REQUEST_METHOD_HEADER = "Access-Control-Request-Method";
    private static final String ACCESS_CONTROL_REQUEST_HEADERS_HEADER = "Access-Control-Request-Headers";
    // Response headers
    private static final String ACCESS_CONTROL_ALLOW_ORIGIN_HEADER = "Access-Control-Allow-Origin";
    private static final String ACCESS_CONTROL_ALLOW_METHODS_HEADER = "Access-Control-Allow-Methods";
    private static final String ACCESS_CONTROL_ALLOW_HEADERS_HEADER = "Access-Control-Allow-Headers";
    private static final String ACCESS_CONTROL_MAX_AGE_HEADER = "Access-Control-Max-Age";
    private static final String ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER = "Access-Control-Allow-Credentials";
    // Implementation constants
    private static final String ALLOWED_ORIGINS_PARAM = "allowedOrigins";
    private static final String ALLOWED_METHODS_PARAM = "allowedMethods";
    private static final String ALLOWED_HEADERS_PARAM = "allowedHeaders";
    private static final String PREFLIGHT_MAX_AGE_PARAM = "preflightMaxAge";
    private static final String ALLOWED_CREDENTIALS_PARAM = "allowCredentials";
    private static final String ANY_ORIGIN = "*";
    private static final List SIMPLE_HTTP_METHODS = Arrays.asList("GET", "POST", "HEAD");

    private boolean anyOriginAllowed = false;
    private List<String> allowedOrigins = new ArrayList<String>();
    private List<String> allowedMethods = new ArrayList<String>();
    private List<String> allowedHeaders = new ArrayList<String>();
    private int preflightMaxAge = 0;
    private boolean allowCredentials = false;

    public void init(FilterConfig config) throws ServletException
    {
        String allowedOriginsConfig = config.getInitParameter(ALLOWED_ORIGINS_PARAM);
        if (allowedOriginsConfig == null) allowedOriginsConfig = "*";
        String[] allowedOrigins = allowedOriginsConfig.split(",");
        for (String allowedOrigin : allowedOrigins)
        {
            allowedOrigin = allowedOrigin.trim();
            if (allowedOrigin.length() > 0)
            {
                if (ANY_ORIGIN.equals(allowedOrigin))
                {
                    anyOriginAllowed = true;
                    this.allowedOrigins.clear();
                    break;
                }
                else
                {
                    this.allowedOrigins.add(allowedOrigin);
                }
            }
        }

        String allowedMethodsConfig = config.getInitParameter(ALLOWED_METHODS_PARAM);
        if (allowedMethodsConfig == null) allowedMethodsConfig = "GET,POST";
        allowedMethods.addAll(Arrays.asList(allowedMethodsConfig.split(",")));

        String allowedHeadersConfig = config.getInitParameter(ALLOWED_HEADERS_PARAM);
        if (allowedHeadersConfig == null) allowedHeadersConfig = "X-Requested-With,Content-Type,Accept,Origin";
        allowedHeaders.addAll(Arrays.asList(allowedHeadersConfig.split(",")));

        String preflightMaxAgeConfig = config.getInitParameter(PREFLIGHT_MAX_AGE_PARAM);
        if (preflightMaxAgeConfig == null) preflightMaxAgeConfig = "1800"; // Default is 30 minutes
        try
        {
            preflightMaxAge = Integer.parseInt(preflightMaxAgeConfig);
        }
        catch (NumberFormatException x)
        {
            log.info("Cross-origin filter, could not parse '{}' parameter as integer: {}"+ PREFLIGHT_MAX_AGE_PARAM+ preflightMaxAgeConfig);
        }

        String allowedCredentialsConfig = config.getInitParameter(ALLOWED_CREDENTIALS_PARAM);
        if (allowedCredentialsConfig == null) allowedCredentialsConfig = "false";
        allowCredentials = Boolean.parseBoolean(allowedCredentialsConfig);

        log.debug("init: Cross-origin filter configuration: " +
                ALLOWED_ORIGINS_PARAM + " = " + allowedOriginsConfig + ", " +
                ALLOWED_METHODS_PARAM + " = " + allowedMethodsConfig + ", " +
                ALLOWED_HEADERS_PARAM + " = " + allowedHeadersConfig + ", " +
                PREFLIGHT_MAX_AGE_PARAM + " = " + preflightMaxAgeConfig + ", " +
                ALLOWED_CREDENTIALS_PARAM + " = " + allowedCredentialsConfig);
    }

    // Stuff when init this filter as a bean
    private String allowedOriginsConfig = "*";
    private String allowedMethodsConfig = "GET,POST,DELETE,PUT";
    private String allowedHeadersConfig = "X-Requested-With,Content-Type,Accept,Origin";
    private String preflightMaxAgeConfig = "1800";
    private String allowedCredentialsConfig = "false";
    public void setAllowedOriginsConfig(String allowedOriginsConfig) {
        this.allowedOriginsConfig = allowedOriginsConfig;
    }

    public void setAllowedMethodsConfig(String allowedMethodsConfig) {
        this.allowedMethodsConfig = allowedMethodsConfig;
    }

    public void setAllowedHeadersConfig(String allowedHeadersConfig) {
        this.allowedHeadersConfig = allowedHeadersConfig;
    }

    public void setPreflightMaxAgeConfig(String preflightMaxAgeConfig) {
        this.preflightMaxAgeConfig = preflightMaxAgeConfig;
    }

    public void setAllowedCredentialsConfig(String allowedCredentialsConfig) {
        this.allowedCredentialsConfig = allowedCredentialsConfig;
    }

    public void beaninit() {
        String[] allowedOrigins = allowedOriginsConfig.split(",");
        for (String allowedOrigin : allowedOrigins)
        {
            allowedOrigin = allowedOrigin.trim();
            if (allowedOrigin.length() > 0)
            {
                if (ANY_ORIGIN.equals(allowedOrigin))
                {
                    anyOriginAllowed = true;
                    this.allowedOrigins.clear();
                    break;
                }
                else
                {
                    this.allowedOrigins.add(allowedOrigin);
                }
            }
        }

        allowedMethods.addAll(Arrays.asList(allowedMethodsConfig.split(",")));

        allowedHeaders.addAll(Arrays.asList(allowedHeadersConfig.split(",")));

        try
        {
            preflightMaxAge = Integer.parseInt(preflightMaxAgeConfig);
        }
        catch (NumberFormatException x)
        {
            log.info("Cross-origin filter, could not parse '{}' parameter as integer: {}"+ PREFLIGHT_MAX_AGE_PARAM+ preflightMaxAgeConfig);
        }

        allowCredentials = Boolean.parseBoolean(allowedCredentialsConfig);

        log.debug("beaninit: Cross-origin filter configuration: " +
                ALLOWED_ORIGINS_PARAM + " = " + allowedOriginsConfig + ", " +
                ALLOWED_METHODS_PARAM + " = " + allowedMethodsConfig + ", " +
                ALLOWED_HEADERS_PARAM + " = " + allowedHeadersConfig + ", " +
                PREFLIGHT_MAX_AGE_PARAM + " = " + preflightMaxAgeConfig + ", " +
                ALLOWED_CREDENTIALS_PARAM + " = " + allowedCredentialsConfig);

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        handle((HttpServletRequest)request, (HttpServletResponse)response, chain);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        String origin = request.getHeader(ORIGIN_HEADER);
        // Is it a cross origin request ?
        if (origin != null && isEnabled(request))
        {
            if (originMatches(origin))
            {
                if (isSimpleRequest(request))
                {
                    log.debug("Cross-origin request to {} is a simple cross-origin request"+ request.getRequestURI());
                    handleSimpleResponse(request, response, origin);
                }
                else
                {
                    log.debug("Cross-origin request to {} is a preflight cross-origin request" +request.getRequestURI());
                    handlePreflightResponse(request, response, origin);
                }
            }
            else
            {
                log.debug("Cross-origin request to " + request.getRequestURI() + " with origin " + origin + " does not match allowed origins " + allowedOrigins);
            }
        }

        chain.doFilter(request, response);
    }

    protected boolean isEnabled(HttpServletRequest request)
    {
        // WebSocket clients such as Chrome 5 implement a version of the WebSocket
        // protocol that does not accept extra response headers on the upgrade response
        if ("Upgrade".equalsIgnoreCase(request.getHeader("Connection")) &&
                "WebSocket".equalsIgnoreCase(request.getHeader("Upgrade")))
        {
            return false;
        }
        return true;
    }

    private boolean originMatches(String origin)
    {
        if (anyOriginAllowed) return true;
        for (String allowedOrigin : allowedOrigins)
        {
            if (allowedOrigin.equals(origin)) return true;
        }
        return false;
    }

    private boolean isSimpleRequest(HttpServletRequest request)
    {
        String method = request.getMethod();
        if (SIMPLE_HTTP_METHODS.contains(method))
        {
            // TODO: implement better section 6.1
            // Section 6.1 says that for a request to be simple, custom request headers must be simple.
            // Here for simplicity I just check if there is a Access-Control-Request-Method header,
            // which is required for preflight requests
            return request.getHeader(ACCESS_CONTROL_REQUEST_METHOD_HEADER) == null;
        }
        return false;
    }

    private void handleSimpleResponse(HttpServletRequest request, HttpServletResponse response, String origin)
    {
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, origin);
        if (allowCredentials) response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, "true");
    }

    private void handlePreflightResponse(HttpServletRequest request, HttpServletResponse response, String origin)
    {
        // Implementation of section 5.2

        // 5.2.3 and 5.2.5
        boolean methodAllowed = isMethodAllowed(request);
        if (!methodAllowed) return;
        // 5.2.4 and 5.2.6
        boolean headersAllowed = areHeadersAllowed(request);
        if (!headersAllowed) return;
        // 5.2.7
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, origin);
        if (allowCredentials) response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, "true");
        // 5.2.8
        if (preflightMaxAge > 0) response.setHeader(ACCESS_CONTROL_MAX_AGE_HEADER, String.valueOf(preflightMaxAge));
        // 5.2.9
        response.setHeader(ACCESS_CONTROL_ALLOW_METHODS_HEADER, commify(allowedMethods));
        // 5.2.10
        response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS_HEADER, commify(allowedHeaders));
    }

    private boolean isMethodAllowed(HttpServletRequest request)
    {
        String accessControlRequestMethod = request.getHeader(ACCESS_CONTROL_REQUEST_METHOD_HEADER);
        log.debug("{} is {}"+ ACCESS_CONTROL_REQUEST_METHOD_HEADER+ accessControlRequestMethod);
        boolean result = false;
        if (accessControlRequestMethod != null)
        {
            result = allowedMethods.contains(accessControlRequestMethod);
        }
        log.debug("Method {} is" + (result ? "" : " not") + " among allowed methods {}"+accessControlRequestMethod+allowedMethods);
        return result;
    }

    private boolean areHeadersAllowed(HttpServletRequest request)
    {
        String accessControlRequestHeaders = request.getHeader(ACCESS_CONTROL_REQUEST_HEADERS_HEADER);
        log.debug("{} is {}"+ ACCESS_CONTROL_REQUEST_HEADERS_HEADER+  accessControlRequestHeaders);
        boolean result = true;
        if (accessControlRequestHeaders != null)
        {
            String[] headers = accessControlRequestHeaders.split(",");
            for (String header : headers)
            {
                boolean headerAllowed = false;
                for (String allowedHeader : allowedHeaders)
                {
                    if (header.trim().equalsIgnoreCase(allowedHeader.trim()))
                    {
                        headerAllowed = true;
                        break;
                    }
                }
                if (!headerAllowed)
                {
                    result = false;
                    break;
                }
            }
        }
        log.debug("Headers [{}] are" + (result ? "" : " not") + " among allowed headers {}"+accessControlRequestHeaders+allowedHeaders);
        return result;
    }

    private String commify(List<String> strings)
    {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < strings.size(); ++i)
        {
            if (i > 0) builder.append(",");
            String string = strings.get(i);
            builder.append(string);
        }
        return builder.toString();
    }

    public void destroy()
    {
        anyOriginAllowed = false;
        allowedOrigins.clear();
        allowedMethods.clear();
        allowedHeaders.clear();
        preflightMaxAge = 0;
        allowCredentials = false;
    }
}