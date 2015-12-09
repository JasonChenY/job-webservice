package com.tiaonr.ws.job.service;

import com.tiaonr.ws.user.dto.ThirdPartyUser;
import com.tiaonr.ws.user.dto.UserDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by echyong on 10/11/15.
 */
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String GET_BINDING_USER_SQL = "select user_id from users_binding where identifier = ? and identity_type = ?";
    private static final String CREATE_USER_SQL = "insert into users (username, password, enabled, email, account_type) values (?,?,?,?,?)";
    private static final String CREATE_AUTHORITY_SQL = "insert into authorities (username, authority) values (?,?)";
    private static final String CREATE_BINDING_SQL = "insert into users_binding (identifier,identity_type,user_id,display_name) values (?,?,?,?)";
    private static final String UPDATE_USER_LOGINTIME_SQL = "update users set last_login_time=?, last_login_ip=? where username=?";
    private static final String UPDATE_USER_BINDING_LOGINTIME_SQL = "update users_binding set last_login_time=?, last_login_ip=? where identifier=? and identity_type=?";

    @Autowired
    @Qualifier("jdbcUserService")
    JdbcUserDetailsManager userDetailsManager;

    // userDetailsManager will be used for some basic user operation
    // jdbcTemplate will be used for OAUTH2 relating operation.
    // I should be able to UserService extends JdbcUserDetailsManager,
    // but there is one problem for XML based configuration,
    // because XML schema dont recognize dataSource property for UserService Class,
    // accordingly wont inject dataSource to base class JdbcUserDetailsManager???
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private Md5PasswordEncoder passwordEncoder;

    @Transactional
    public UserDetails registerUser(UserDetails user) {
        LOGGER.debug("enter registerUser");
        userDetailsManager.createUser(user);
        return user;
    }

    @Transactional
    public boolean registerUser(final UserDTO user) {
        LOGGER.debug("enter registerUser");
        jdbcTemplate.update(this.CREATE_USER_SQL, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, user.getUsername());
                ps.setString(2, passwordEncoder.encodePassword(user.getPassword(), user.getUsername()));
                ps.setBoolean(3, true);
                ps.setString(4, user.getEmail());
                ps.setInt(5, 0);
            }
        });
        jdbcTemplate.update(this.CREATE_AUTHORITY_SQL, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, user.getUsername());
                ps.setString(2, "ROLE_USER");
            }
        });
        return true;
    }

    public boolean userExists(String username) {
        return userDetailsManager.userExists(username);
    }

    @Transactional
    public String bindUser(final ThirdPartyUser user) {
        List users = jdbcTemplate.queryForList(this.GET_BINDING_USER_SQL, new Object[]{user.getIdentifier(), user.getIdentity_type()}, String.class);
        if(users.size() >= 1) {
            if ( users.size() > 1 ) {
                LOGGER.warn("More than one user found with name \'" + user.getIdentifier() + "\'");
            }
            return (String)users.get(0);
        } else {
            /* generate a internal system account */
            final String user_id = UUID.randomUUID().toString();

            /* create a new internal system account */
            jdbcTemplate.update(this.CREATE_USER_SQL, new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, user_id);
                    ps.setString(2, "N/A");
                    ps.setBoolean(3, true);
                    ps.setString(4, null); // to be extended to email in future.
                    ps.setInt(5, 1);
                }
            });

            /* create authority for this internal system account */
            jdbcTemplate.update(this.CREATE_AUTHORITY_SQL, new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, user_id);
                    ps.setString(2, "ROLE_USER");
                }
            });

            /* creating a binding entry */
            jdbcTemplate.update(this.CREATE_BINDING_SQL, new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setString(1, user.getIdentifier());
                    ps.setInt(2, user.getIdentity_type());
                    ps.setString(3, user_id);
                    ps.setString(4, user.getDisplay_name());
                }
            });

            return user_id;
        }
    }

    public void updateLoginTime(final String username, final String remoteAddress) {
        try {
            jdbcTemplate.update(UPDATE_USER_LOGINTIME_SQL, new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
                    ps.setString(2, remoteAddress);
                    ps.setString(3, username);
                }
            });
        } catch ( Exception e) {
            LOGGER.warn("Failed to update user login info, nonfatal");
        }
    }

    public void updateLoginTime(final ThirdPartyUser detail) {
        try {
            jdbcTemplate.update(UPDATE_USER_BINDING_LOGINTIME_SQL, new PreparedStatementSetter() {
                public void setValues(PreparedStatement ps) throws SQLException {
                    ps.setTimestamp(1, new java.sql.Timestamp(System.currentTimeMillis()));
                    ps.setString(2, detail.getLast_login_ip());
                    ps.setString(3, detail.getIdentifier());
                    ps.setInt(4, detail.getIdentity_type());
                }
            });
        } catch ( Exception e) {
            LOGGER.warn("Failed to update user login info, nonfatal");
        }
    }
}

/*
* User binding scenario:
* 1. login with third party account firstly,
- system will generate internal account,
- user favorited/complained some job items.
- user enter portal to register user account later to bind.
  ( wont logged in automatically with this account),

background should update database, change the bind account name, update foravortes/complains table,
and delete old system generated internal account.

2. user login with self registered account firstly.
choose action to bind third party account.
wait for loginSuccess.jsp page, then insert into users_binding table the new entry.

search users_bind table to see whether corresponding entry exists already,
2.1 if not, just insert new entry to users_binding table.

2.2 if exists, select the 'user_id' field, to check whether the binding account is an internal account,
    2.2.1 if yes, something similar to 1.
    2.2.2 otherwise, report critical errors. you cant bind same third party account to two system internal account.

3. when user logged in with system account,
no pic,  update the (binding_time, binding_ip) the login_time, login_ip ..... in users table.

4. when user loggged in with third party account, should include a link to the picture, and corresponding QQ/Weibo indicator.
update the login_time, login_ip in users bind table.
* */