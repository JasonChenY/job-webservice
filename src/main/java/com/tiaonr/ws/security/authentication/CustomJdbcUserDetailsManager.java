package com.tiaonr.ws.security.authentication;

/**
 * Created by echyong on 11/10/15.
 */
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CustomJdbcUserDetailsManager extends JdbcUserDetailsManager {
    @Override
    public List<UserDetails> loadUsersByUsername(String username) {
        return this.getJdbcTemplate().query(super.getUsersByUsernameQuery(), new String[]{username,username}, new RowMapper() {
            public UserDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                String username = rs.getString(1);
                String password = rs.getString(2);
                boolean enabled = rs.getBoolean(3);
                return new User(username, password, enabled, true, true, true, AuthorityUtils.NO_AUTHORITIES);
            }
        });
    }

    @Override
    public boolean userExists(String username) {
        List users = this.getJdbcTemplate().queryForList("select username from users where (username = ? or email = ?) and account_type = 0", new String[]{username, username}, String.class);
        if(users.size() > 1) {
            throw new IncorrectResultSizeDataAccessException("More than one user found with name \'" + username + "\'", 1);
        } else {
            return users.size() == 1;
        }
    }
}
