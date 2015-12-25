package com.tiaonr.ws.security.authentication;

import com.tiaonr.ws.user.dto.SecurityRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import javax.annotation.Resource;
import com.tiaonr.ws.job.service.UserService;
import com.tiaonr.ws.user.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * @author jason.y.chen
 */
public class RestAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Resource
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {


        Object principal = authentication.getPrincipal();
        String username;
        SecurityRole role;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
            Iterator<? extends GrantedAuthority> authority = ((UserDetails) principal).getAuthorities().iterator();
            GrantedAuthority a = authority.next();
            role = SecurityRole.valueOf(a.getAuthority());
        } else {
            username = (String)principal;
            role = SecurityRole.ROLE_USER;
        }

        WebAuthenticationDetails detail = (WebAuthenticationDetails)authentication.getDetails();

        userService.updateLoginTime(username, detail.getRemoteAddress());

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            UserDTO user = new UserDTO(username, role);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(user);
            out.append(json);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
