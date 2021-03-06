package com.tiaonr.ws.user.dto;

/**
 * Created by echyong on 10/19/15.
 */
import java.util.Date;

public class ThirdPartyUser {
    private String identifier;
    private int identity_type; // 1: TestServer 2: QQ 3: Weibo 4: Baidu
    private String user_id;
    private String display_name; // nickname: QQ
    private Date binding_time;
    private Date last_login_time;
    private String last_login_ip;
    private String access_token;
    private String refresh_token;

    public ThirdPartyUser() {

    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getIdentity_type() {
        return identity_type;
    }

    public void setIdentity_type(int identity_type) {
        this.identity_type = identity_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Date getBinding_time() {
        return binding_time;
    }

    public void setBinding_time(Date binding_time) {
        this.binding_time = binding_time;
    }

    public Date getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(Date last_login_time) {
        this.last_login_time = last_login_time;
    }

    public String getLast_login_ip() {
        return last_login_ip;
    }

    public void setLast_login_ip(String last_login_ip) {
        this.last_login_ip = last_login_ip;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getDisplay_name() { return display_name; }

    public void setDisplay_name(String display_name) { this.display_name = display_name; }

}
