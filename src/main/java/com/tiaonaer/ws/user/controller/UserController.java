package com.tiaonaer.ws.user.controller;

import com.tiaonaer.ws.job.service.UserService;
import com.tiaonaer.ws.security.util.SecurityContextUtil;
import com.tiaonaer.ws.user.dto.SecurityRole;
import com.tiaonaer.ws.user.dto.ThirdPartyUser;
import com.tiaonaer.ws.user.dto.UserDTO;
import com.tiaonaer.ws.user.exception.UserRegisterException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by echyong on 10/18/15.
 */
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    private SecurityContextUtil securityContextUtil;

    @Resource
    private UserService userService;

    @Autowired
    @Qualifier("org.springframework.security.authenticationManager")
    private AuthenticationManager authenticationManager;

    public String bindUser(ThirdPartyUser user) {
        return userService.bindUser(user);
    }
    public void loginUser(UserDetails user, ThirdPartyUser detail) throws AuthenticationException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user, "N/A", AuthorityUtils.createAuthorityList("ROLE_USER"));
        if ( detail != null ) {
            detail.setLast_login_ip(request.getRemoteAddr());
            detail.setLast_login_time(DateTime.now());
            token.setDetails(detail);
        } else {
            token.setDetails(new WebAuthenticationDetails(request));
        }
        //Authentication authenticatedUser = authenticationManager.authenticate(token);

        LOGGER.debug("save Authentication to security context");
        SecurityContextHolder.getContext().setAuthentication(token);

        LOGGER.debug("save security context to http session, authenticate done.");
        request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
    }

    public UserDTO getLoggedInUser() {
        LOGGER.debug("Getting logged in user.");
        UserDTO dto = new UserDTO();
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ( authentication != null ) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
                dto.setRole(getRole(((UserDetails) principal).getAuthorities()));
            } else {
                username = (String)principal;
            }
            dto.setUsername(username);

            if (authentication.getDetails() instanceof ThirdPartyUser) {
                ThirdPartyUser detail = (ThirdPartyUser)authentication.getDetails();
                dto.setUsername(detail.getIdentifier());
                dto.setIdentity_type(detail.getIdentity_type());
                //dto.setFigure_url(...);
                userService.updateLoginTime(detail);
            } else if (authentication.getDetails() instanceof WebAuthenticationDetails) {
                WebAuthenticationDetails detail = (WebAuthenticationDetails)authentication.getDetails();
                userService.updateLoginTime(username, detail.getRemoteAddress());
            }
        }
        return dto;
    }

    public boolean userExists(String user_id) {
        LOGGER.debug("check whether this user_id is occupied");
        return userService.userExists(user_id);
    }

    private SecurityRole getRole(Collection<? extends GrantedAuthority> authorities) {
        LOGGER.debug("Getting role from authorities: {}", authorities);

        Iterator<? extends GrantedAuthority> authority = authorities.iterator();
        GrantedAuthority a = authority.next();

        return SecurityRole.valueOf(a.getAuthority());
    }
}


