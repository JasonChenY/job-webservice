package com.tiaonaer.ws.user.controller;

import com.tiaonaer.ws.job.exception.CommentNotFoundException;
import com.tiaonaer.ws.job.service.UserService;
import com.tiaonaer.ws.security.util.SecurityContextUtil;
import com.tiaonaer.ws.user.dto.SecurityRole;
import com.tiaonaer.ws.user.dto.UserDTO;
import com.tiaonaer.ws.user.exception.UserRegisterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author jason.y.chen
 */
@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    private SecurityContextUtil securityContextUtil;

    @Resource
    private UserService userService;

    @Autowired
    @Qualifier("org.springframework.security.authenticationManager")
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    @ResponseBody
    public UserDTO register_user(@RequestBody UserDTO dto) throws UserRegisterException
    {
        try {
            LOGGER.debug("add user to db");
            UserDetails user = userService.registerUser(dto);
        } catch (Exception e) {
            LOGGER.warn("User Register Failed");
            throw new UserRegisterException(e.getMessage());
        }

        try{
            LOGGER.debug("authenticate the user from db again");
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
            token.setDetails(new WebAuthenticationDetails(request));
            Authentication authenticatedUser = authenticationManager.authenticate(token);

            LOGGER.debug("save Authentication to security context");
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

            LOGGER.debug("save security context to http session, authenticate done.");
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        } catch( AuthenticationException e ){
            LOGGER.warn("Authentication failed: " + e.getMessage());
            throw new UserRegisterException(e.getMessage());
        }

        return getLoggedInUser();
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    @ResponseBody
    public UserDTO getLoggedInUser() {
        LOGGER.debug("Getting logged in user.");
        UserDetails principal = securityContextUtil.getPrincipal();
        return createDTO(principal);
    }

    private UserDTO createDTO(UserDetails principal) {
        UserDTO dto = null;
        if (principal != null) {
            String username = principal.getUsername();
            SecurityRole role = getRole(principal.getAuthorities());

            dto = new UserDTO(username, role);
            dto.setPassword("[PROTECTED]");
        }

        LOGGER.debug("Created user dto: {}", dto);

        return dto;
    }

    private SecurityRole getRole(Collection<? extends GrantedAuthority> authorities) {
        LOGGER.debug("Getting role from authorities: {}", authorities);

        Iterator<? extends GrantedAuthority> authority = authorities.iterator();
        GrantedAuthority a = authority.next();

        return SecurityRole.valueOf(a.getAuthority());
    }

    @ExceptionHandler(UserRegisterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleUserRegisterException(UserRegisterException ex) {
        LOGGER.debug("handling User Register Exception");
        return ex.getMessage();
    }
}
