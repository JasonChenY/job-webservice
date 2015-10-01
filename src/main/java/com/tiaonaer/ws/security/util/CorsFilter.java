package com.tiaonaer.ws.security.util;

/**
 * Created by echyong on 9/30/15.
 */
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
            // should use concrete url instead of *, otherwise api/user is rejected due to CORS, but ok for api/login.
            response.addHeader("Access-Control-Allow-Origin", "http://192.168.137.128");
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
