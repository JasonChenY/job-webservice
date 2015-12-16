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
public class WeiboController extends UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeiboController.class);

    @Autowired
    private RestOperations weiboRestTemplate;

    private static class Weibo_userinfo {
        String id;
        String screen_name;
        String name;
        String province;
        String city;
        String location;
        String description;
        String url;
        String profile_image_url;
        String domain;
        String gender;
        int followers_count;
        int friends_count;
        int statuses_count;
        int favourites_count;
        String created_at;
        Boolean following;
        Boolean allow_all_act_msg;
        Boolean geo_enabled;
        Boolean verified;
        Boolean allow_all_comment;
        String avatar_large;

        String verified_reason;
        Boolean follow_me;
        int online_status;
        int bi_followers_count;

        public Weibo_userinfo() {}

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getScreen_name() {
            return screen_name;
        }

        public void setScreen_name(String screen_name) {
            this.screen_name = screen_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getProfile_image_url() {
            return profile_image_url;
        }

        public void setProfile_image_url(String profile_image_url) {
            this.profile_image_url = profile_image_url;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getFollowers_count() {
            return followers_count;
        }

        public void setFollowers_count(int followers_count) {
            this.followers_count = followers_count;
        }

        public int getFriends_count() {
            return friends_count;
        }

        public void setFriends_count(int friends_count) {
            this.friends_count = friends_count;
        }

        public int getStatuses_count() {
            return statuses_count;
        }

        public void setStatuses_count(int statuses_count) {
            this.statuses_count = statuses_count;
        }

        public int getFavourites_count() {
            return favourites_count;
        }

        public void setFavourites_count(int favourites_count) {
            this.favourites_count = favourites_count;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public Boolean getFollowing() {
            return following;
        }

        public void setFollowing(Boolean following) {
            this.following = following;
        }

        public Boolean getAllow_all_act_msg() {
            return allow_all_act_msg;
        }

        public void setAllow_all_act_msg(Boolean allow_all_act_msg) {
            this.allow_all_act_msg = allow_all_act_msg;
        }

        public Boolean getGeo_enabled() {
            return geo_enabled;
        }

        public void setGeo_enabled(Boolean geo_enabled) {
            this.geo_enabled = geo_enabled;
        }

        public Boolean getVerified() {
            return verified;
        }

        public void setVerified(Boolean verified) {
            this.verified = verified;
        }

        public Boolean getAllow_all_comment() {
            return allow_all_comment;
        }

        public void setAllow_all_comment(Boolean allow_all_comment) {
            this.allow_all_comment = allow_all_comment;
        }

        public String getAvatar_large() {
            return avatar_large;
        }

        public void setAvatar_large(String avatar_large) {
            this.avatar_large = avatar_large;
        }

        public String getVerified_reason() {
            return verified_reason;
        }

        public void setVerified_reason(String verified_reason) {
            this.verified_reason = verified_reason;
        }

        public Boolean getFollow_me() {
            return follow_me;
        }

        public void setFollow_me(Boolean follow_me) {
            this.follow_me = follow_me;
        }

        public int getOnline_status() {
            return online_status;
        }

        public void setOnline_status(int online_status) {
            this.online_status = online_status;
        }

        public int getBi_followers_count() {
            return bi_followers_count;
        }

        public void setBi_followers_count(int bi_followers_count) {
            this.bi_followers_count = bi_followers_count;
        }
    }

    private static class Weibo_uid {
        String uid;

        public Weibo_uid() {}

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }

    @RequestMapping("/weibo/login")
    public String login(Model model) throws AuthenticationException {

        Weibo_uid uid = weiboRestTemplate.getForObject("https://api.weibo.com/2/account/get_uid.json", Weibo_uid.class);
        LOGGER.debug("weibo uid: " + uid.uid);

        String url = "https://api.weibo.com/2/users/show.json?uid=" + uid.getUid();
        Weibo_userinfo userinfo = weiboRestTemplate.getForObject(url, Weibo_userinfo.class);

        //translate thirdparty user account to UserDTO.
        ThirdPartyUser detail = new ThirdPartyUser();
        detail.setIdentifier(uid.uid);
        detail.setIdentity_type(3);
        detail.setDisplay_name(userinfo.getName());

        /* bound the third party user to one system user if not done yet */
        String user_id = bindUser(detail);
        detail.setUser_id(user_id);

        /* login the new user to http session */
        UserDetails user = new User(user_id, "N/A",
                true, true, true, true, AuthorityUtils.createAuthorityList("ROLE_USER"));
        loginUser(user, detail);

        model.addAttribute("username", userinfo.getName());
        model.addAttribute("email", "N/A");
        model.addAttribute("phone", "N/A");

        return "loginSuccess";
    }
}
