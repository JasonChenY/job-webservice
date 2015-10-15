package com.tiaonaer.ws.user.controller;

/**
 * Created by echyong on 10/14/15.
 */

import com.tiaonaer.ws.job.service.UserService;
import com.tiaonaer.ws.security.util.SecurityContextUtil;
import com.tiaonaer.ws.user.exception.UserRegisterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestOperations;

import com.tiaonaer.ws.user.dto.UserDTO;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.annotation.Resource;

@Controller
public class TestServerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestServerController.class);

    @Autowired
    private RestOperations testServerRestTemplate;

    @Resource
    private SecurityContextUtil securityContextUtil;

    @Resource
    private UserService userService;

    @Autowired
    @Qualifier("org.springframework.security.authenticationManager")
    private AuthenticationManager authenticationManager;

    /* to render JSON
    @RequestMapping("/testServer/getUserInfo")
    @ResponseBody
    public UserDTO getUserInfo() throws Exception {

        UserDTO result = testServerRestTemplate
                .getForObject("http://localhost/oauth2-server/resource/getUserInfo", UserDTO.class);
        return result;
    }*/

    // render with jsp page
    @RequestMapping("/testServer/login")
    public String login(Model model) throws Exception {

        UserDTO dto = testServerRestTemplate.getForObject("http://localhost/oauth2-server/resource/getUserInfo", UserDTO.class);

        /* Register New User and secure http session */
        try {
            LOGGER.debug("add user to db");
            dto.setPassword("N/A");
            UserDetails user = userService.registerUser(dto);
        } catch (Exception e) {
            LOGGER.warn("User Register Failed, but it is normal case");
        }

        try{
            LOGGER.debug("authenticate the user from db again");
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
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

        model.addAttribute("username", dto.getUsername());
        model.addAttribute("email", dto.getEmail());
        model.addAttribute("phone", dto.getPhone());

        return "loginSuccess";
    }
}
