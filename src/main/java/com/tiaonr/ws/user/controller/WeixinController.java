package com.tiaonr.ws.user.controller;

/**
 * Created by echyong on 12/15/15.
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

import java.util.regex.Pattern;
import java.util.regex.Matcher;

@PropertySource("classpath:oauth2.properties")
@Controller
public class WeixinController extends UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeixinController.class);

    @Autowired
    private RestOperations weixinRestTemplate;

    private static class Weixin_userinfo {
        String openid;
        String nickname;
        int sex;
        String province;
        String city;
        String country;
        String headimgurl;
        //"privilege":["PRIVILEGE1","PRIVILEGE2"],
        String unionid;

        public String getOpenid() {
            return openid;
        }
        public void setOpenid(String openid) {
            this.openid = openid;
        }
        public String getNickname() {
            return nickname;
        }
        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
        public String getProvince() {
            return province;
        }
        public void setProvince(String province) {
            this.province = province;
        }
        public int getSex() {
            return sex;
        }
        public void setSex(int sex) {
            this.sex = sex;
        }
        public String getCity() {
            return city;
        }
        public void setCity(String city) {
            this.city = city;
        }
        public String getCountry() {
            return country;
        }
        public void setCountry(String country) {
            this.country = country;
        }
        public String getHeadimgurl() {
            return headimgurl;
        }
        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }
        public String getUnionid() {
            return unionid;
        }
        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }
        public Weixin_userinfo() {}
    }

    private static class Weixin_info {
        String access_token;
        int expires_in;
        String refresh_token;
        String openid;
        String scope;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public Weixin_info() {}
    }

    @RequestMapping("/weixin/login")
    public String login(Model model) throws AuthenticationException {
        LOGGER.debug("enter qq login");

        Weixin_info weixin_info = weixinRestTemplate.getForObject("https://api.weixin.qq.com/sns/oauth2/access_token", Weixin_info.class);
        LOGGER.debug("openid string: " + weixin_info.getOpenid());
/*
        String openid = null;
        Matcher m = Pattern.compile("\"openid\"\\s*:\\s*\"(\\w+)\"").matcher(me);
        if(m.find()) {
            openid = m.group(1);
            LOGGER.debug("openid: " + openid);
        } else {
            model.addAttribute("reason", "failed to get openid");
            return "loginFailure";
        }
*/
        String openid = weixin_info.getOpenid();

        String url = "https://api.weixin.qq.com/sns/userinfo&openid=" + openid;
        Weixin_userinfo userinfo = weixinRestTemplate.getForObject(url, Weixin_userinfo.class);
        LOGGER.debug("nickname: " + userinfo.getNickname());
        LOGGER.debug("figure_url: " + userinfo.getHeadimgurl());

        //translate thirdparty user account to UserDTO.
        ThirdPartyUser detail = new ThirdPartyUser();
        detail.setIdentifier(openid);
        detail.setIdentity_type(4);
        detail.setDisplay_name(userinfo.getNickname());

        /* bound the third party user to one system user if not done yet */
        String user_id = bindUser(detail);
        detail.setUser_id(user_id);

        /* login the new user to http session */
        UserDetails user = new User(user_id, "N/A",
                true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_USER"));
        loginUser(user, detail);

        model.addAttribute("username", userinfo.getNickname());
        model.addAttribute("email", "N/A");
        model.addAttribute("phone", "N/A");

        return "loginSuccess";
    }
}
