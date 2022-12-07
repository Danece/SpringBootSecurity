package com.springboot_practice.demo.vo;
import javax.validation.constraints.NotEmpty;

public class UserInfoVo {
    private Long id;
    private String operation;
    private String name;
    private String password;
    private String authority;

    @NotEmpty(message = "使用者名稱不可為空")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = "密碼不可為空")
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotEmpty(message = "權限不可為空")
    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Long getId() {
        return this.id;
    }

    public String getOperation() {
        return this.operation;
    }

}
