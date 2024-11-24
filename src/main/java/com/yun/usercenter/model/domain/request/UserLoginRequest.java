package com.yun.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 4466612732608920074L;


    private String userAccount;

    private String userPassword;
}
