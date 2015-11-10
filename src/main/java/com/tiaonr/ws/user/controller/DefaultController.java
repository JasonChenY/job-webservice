package com.tiaonr.ws.user.controller;

import com.tiaonr.ws.job.service.UserService;
import com.tiaonr.ws.user.dto.UserDTO;
import com.tiaonr.ws.user.dto.UserRegisterDTO;
import com.tiaonr.ws.user.exception.UserRegisterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import javax.annotation.Resource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

/**
 * @author jason.y.chen
 */
@Controller
public class DefaultController extends UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultController.class);

    @Resource
    private UserService userService;

    @Autowired
    private Md5PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    @ResponseBody
    public UserDTO apiRegisterUser(@RequestBody UserDTO dto) throws UserRegisterException
    {
        UserDetails user = new User(dto.getUsername(),
                passwordEncoder.encodePassword(dto.getPassword(), dto.getUsername()),
                true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_USER"));
        try {
            LOGGER.debug("add user to db");
            userService.registerUser(dto);
        } catch (Exception e) {
            LOGGER.warn("User Register Failed");
            throw new UserRegisterException(e.getMessage());
        }
        loginUser(user, null);
        return getLoggedInUser();
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.GET)
    @ResponseBody
    public UserDTO apiGetLoggedInUser() {
        return getLoggedInUser();
    }

    @RequestMapping(value = "/api/user/{user_id}", method = RequestMethod.GET)
    @ResponseBody
    public boolean apiUserExists(@PathVariable("user_id") String user_id) {
        return userExists(user_id);
    }

    //cannt place email on url string(encodeURI wont encode '.'),
    // otherwise need do special decode here.
    // also cant use www-formencoded format
    @RequestMapping(value = "/api/user", method = RequestMethod.POST)
    @ResponseBody
    public boolean apiUserExistsByEmail(@RequestBody UserRegisterDTO info) {
        LOGGER.debug("check whether " + info.getEmail() + " exists");
        return userExists(info.getEmail());
    }

    @ExceptionHandler(UserRegisterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleUserRegisterException(UserRegisterException ex) {
        LOGGER.debug("handling User Register Exception");
        return ex.getMessage();
    }
}

