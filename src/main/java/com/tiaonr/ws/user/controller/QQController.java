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
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Map;
import java.util.HashMap;

@PropertySource("classpath:oauth2.properties")
@Controller
public class QQController extends UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QQController.class);

    @Autowired
    private RestOperations qqRestTemplate;

    private static class QQ_userinfo {
        int ret;

        public int getRet() {
            return ret;
        }

        public void setRet(int ret) {
            this.ret = ret;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public int getIs_lost() {
            return is_lost;
        }

        public void setIs_lost(int is_lost) {
            this.is_lost = is_lost;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getFigureurl() {
            return figureurl;
        }

        public void setFigureurl(String figureurl) {
            this.figureurl = figureurl;
        }

        public String getFigureurl_1() {
            return figureurl_1;
        }

        public void setFigureurl_1(String figureurl_1) {
            this.figureurl_1 = figureurl_1;
        }

        public String getFigureurl_2() {
            return figureurl_2;
        }

        public void setFigureurl_2(String figureurl_2) {
            this.figureurl_2 = figureurl_2;
        }

        public String getFigureurl_qq_1() {
            return figureurl_qq_1;
        }

        public void setFigureurl_qq_1(String figureurl_qq_1) {
            this.figureurl_qq_1 = figureurl_qq_1;
        }

        public String getFigureurl_qq_2() {
            return figureurl_qq_2;
        }

        public void setFigureurl_qq_2(String figureurl_qq_2) {
            this.figureurl_qq_2 = figureurl_qq_2;
        }

        public String getIs_yellow_vip() {
            return is_yellow_vip;
        }

        public void setIs_yellow_vip(String is_yellow_vip) {
            this.is_yellow_vip = is_yellow_vip;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getYellow_vip_level() {
            return yellow_vip_level;
        }

        public void setYellow_vip_level(String yellow_vip_level) {
            this.yellow_vip_level = yellow_vip_level;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getIs_yellow_year_vip() {
            return is_yellow_year_vip;
        }

        public void setIs_yellow_year_vip(String is_yellow_year_vip) {
            this.is_yellow_year_vip = is_yellow_year_vip;
        }

        String msg;
        int is_lost;
        String nickname;
        String gender;
        String province;
        String city;
        String year;
        String figureurl;
        String figureurl_1;
        String figureurl_2;
        String figureurl_qq_1;
        String figureurl_qq_2;
        String is_yellow_vip;
        String vip;
        String yellow_vip_level;
        String level;
        String is_yellow_year_vip;

        public QQ_userinfo() {}
    }

    @RequestMapping("/qq/login")
    public String login(Model model) throws AuthenticationException {
        LOGGER.debug("enter qq login");

        String me = qqRestTemplate.getForObject("https://graph.qq.com/oauth2.0/me", String.class);
        LOGGER.debug("openid string: " + me);

        String openid = null;
        Matcher m = Pattern.compile("\"openid\"\\s*:\\s*\"(\\w+)\"").matcher(me);
        if(m.find()) {
            openid = m.group(1);
            LOGGER.debug("openid: " + openid);
        } else {
            model.addAttribute("reason", "failed to get openid");
            return "loginFailure";
        }

        /*
        Map<String, String> urlVariables = new HashMap<String, String>();
        urlVariables.put("openid", openid);
        urlVariables.put("oauth_consumer_key", "101260439");
        urlVariables.put("format", "json");
        QQ_userinfo userinfo = qqRestTemplate.getForObject("https://graph.qq.com/user/get_user_info", QQ_userinfo.class, urlVariables);
        */
        String url = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=101260439&format=json&openid=" + openid;
        QQ_userinfo userinfo = qqRestTemplate.getForObject(url, QQ_userinfo.class);
        LOGGER.debug("nickname: " + userinfo.nickname);
        LOGGER.debug("figure_url: " + userinfo.figureurl);

        //translate thirdparty user account to UserDTO.
        ThirdPartyUser detail = new ThirdPartyUser();
        detail.setIdentifier(openid);
        detail.setIdentity_type(2);
        detail.setDisplay_name(userinfo.nickname);

        /* bound the third party user to one system user if not done yet */
        String user_id = bindUser(detail);
        detail.setUser_id(user_id);

        /* login the new user to http session */
        UserDetails user = new User(user_id, "N/A",
                true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_USER"));
        loginUser(user, detail);

        model.addAttribute("username", userinfo.nickname);
        model.addAttribute("email", "N/A");
        model.addAttribute("phone", "N/A");

        return "loginSuccess";
    }
}
