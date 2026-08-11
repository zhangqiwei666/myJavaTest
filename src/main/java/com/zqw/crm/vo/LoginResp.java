package com.zqw.crm.vo;

import java.util.List;

public class LoginResp {

    private String token;
    private String username;
    private String realName;
    private List<String> roles;
    private List<String> permissions;

    public LoginResp() {
    }

    public LoginResp(String token, String username, String realName, List<String> roles, List<String> permissions) {
        this.token = token;
        this.username = username;
        this.realName = realName;
        this.roles = roles;
        this.permissions = permissions;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }

    public List<String> getPermissions() { return permissions; }
    public void setPermissions(List<String> permissions) { this.permissions = permissions; }
}
