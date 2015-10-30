package com.tiaonr.ws.user.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author jason.y.chen
 */
public class UserDTO {
    private String username;
    private String password;
    private SecurityRole role;
    private int identity_type; // 0: normal account  1: TestServer 2: QQ 3: Weibo 4: Sina
    private String figure_url;
    private String email;
    private String phone;

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
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public SecurityRole getRole() {
        return role;
    }
    public void setRole(SecurityRole role) {
        this.role = role;
    }
    public int getIdentity_type() {
        return identity_type;
    }
    public void setIdentity_type(int identity_type) {
        this.identity_type = identity_type;
    }
    public String getFigure_url() {
        return figure_url;
    }
    public void setFigure_url(String figure_url) {
        this.figure_url = figure_url;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
