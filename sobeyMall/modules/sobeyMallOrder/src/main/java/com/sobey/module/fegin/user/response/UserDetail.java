package com.sobey.module.fegin.user.response;

import java.util.Map;

/**
 * @Description TODO...
 * @author WuChenYang
 * @createTime 2020/2/24 15:09
 */
public class UserDetail {

    private String avatar_url;
    private Object client_codes;
    private String create_time;
    private String description;
    private Boolean disabled;
    private String disabled_time;
    private String email;
    private Map<String, Object> extend;
    private Object groups;
    private Long id;
    private String identity;
    private String identity_type;
    private String nick_name;
    private String login_name;
    private Object organizations;
    private String password;
    private String phone;
    private String pwd_change_time;
    private Object relation_ids;
    private Object roles;
    private String site_code;
    private String user_code;

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public Object getClient_codes() {
        return client_codes;
    }

    public void setClient_codes(Object client_codes) {
        this.client_codes = client_codes;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getDisabled_time() {
        return disabled_time;
    }

    public void setDisabled_time(String disabled_time) {
        this.disabled_time = disabled_time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, Object> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, Object> extend) {
        this.extend = extend;
    }

    public Object getGroups() {
        return groups;
    }

    public void setGroups(Object groups) {
        this.groups = groups;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity_type() {
        return identity_type;
    }

    public void setIdentity_type(String identity_type) {
        this.identity_type = identity_type;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public Object getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Object organizations) {
        this.organizations = organizations;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd_change_time() {
        return pwd_change_time;
    }

    public void setPwd_change_time(String pwd_change_time) {
        this.pwd_change_time = pwd_change_time;
    }

    public Object getRelation_ids() {
        return relation_ids;
    }

    public void setRelation_ids(Object relation_ids) {
        this.relation_ids = relation_ids;
    }

    public Object getRoles() {
        return roles;
    }

    public void setRoles(Object roles) {
        this.roles = roles;
    }

    public String getSite_code() {
        return site_code;
    }

    public void setSite_code(String site_code) {
        this.site_code = site_code;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }
}

