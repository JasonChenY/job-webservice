package com.tiaonr.ws.user.controller;

/**
 * Created by echyong on 10/14/15.
 */
import com.tiaonr.ws.user.dto.ThirdPartyUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestOperations;
import org.springframework.ui.Model;
import com.tiaonr.ws.user.dto.UserDTO;

@PropertySource("classpath:oauth2.properties")
@Controller
public class TestServerController extends UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestServerController.class);

    @Value("${testServer.resourceUri}")
    private String testServerResourceUri;

    @Autowired
    private RestOperations testServerRestTemplate;

    private static class TestServerUser {
        public String username;
        public String email;
        public String phone;
    }
    // render with jsp page
    @RequestMapping("/testServer/login")
    public String login(Model model) throws AuthenticationException {
        TestServerUser testuser = testServerRestTemplate.getForObject(testServerResourceUri + "/getUserInfo", TestServerUser.class);

        //translate thirdparty user account to UserDTO.
        ThirdPartyUser detail = new ThirdPartyUser();
        detail.setIdentifier(testuser.username);
        detail.setIdentity_type(1);

        /* bound the third party user to one system user if not done yet */
        String user_id = bindUser(detail);
        detail.setUser_id(user_id);

        /* login the new user to http session */
        UserDetails user = new User(user_id, "N/A",
                true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_USER"));
        loginUser(user, detail);

        model.addAttribute("username", testuser.username);
        model.addAttribute("email", testuser.email);
        model.addAttribute("phone", testuser.phone);

        return "loginSuccess";
    }
}
