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
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
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

    @RequestMapping("/weixin/login")
    public String login(Model model) throws AuthenticationException {
        LOGGER.debug("enter weixin login");

        /* weixin return openid inside access token, no additional api to fetch openid, which is not standard.
           solution: change oauth (OAuth2RestTemplate.java) to carry openid (if existing) from accessToken.AdditionalInformation for outgoing request.
           String url = "https://api.weixin.qq.com/sns/userinfo&openid=" + openid;
        */
        String url = "https://api.weixin.qq.com/sns/userinfo";
        Weixin_userinfo userinfo = weixinRestTemplate.getForObject(url, Weixin_userinfo.class);
        LOGGER.debug("nickname: " + userinfo.getNickname());
        LOGGER.debug("figure_url: " + userinfo.getHeadimgurl());

        /*
         *From here can get openid, but fortunately weixin will return openid in response as well.
         *java.util.Map<String, Object> maps = ((OAuth2RestTemplate)weixinRestTemplate).getOAuth2ClientContext().getAccessToken().getAdditionalInformation();
        */
        ThirdPartyUser detail = new ThirdPartyUser();
        detail.setIdentifier(userinfo.getOpenid());
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
