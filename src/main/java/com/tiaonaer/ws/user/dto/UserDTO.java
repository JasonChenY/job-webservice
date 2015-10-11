package com.tiaonaer.ws.user.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author jason.y.chen
 */
public class UserDTO {

    private String username;

    private SecurityRole role;

    private String password;

    public UserDTO() {}

    public UserDTO(String username, SecurityRole role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public SecurityRole getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
