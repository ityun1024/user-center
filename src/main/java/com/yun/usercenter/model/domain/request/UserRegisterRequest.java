package com.yun.usercenter.model.domain.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = -8851651247467610606L;

    private String userAccount;

    private String userPassword;

    private String checkPassword;
}
