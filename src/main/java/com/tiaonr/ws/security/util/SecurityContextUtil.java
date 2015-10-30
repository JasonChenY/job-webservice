package com.tiaonr.ws.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @author jason.y.chen
 */
@Component
public class SecurityContextUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContextUtil.class);

    public String getUser_id() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((UserDetails) principal).getUsername();
            } else if ( principal instanceof String) {
                return (String)principal;
            } else {
                return principal.toString();
            }
        } else {
            return null;
        }
    }
}
