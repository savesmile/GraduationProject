package com.lin_chen.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用户信息
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User {
    /**
     * 默认男性
     */
    public static final int SEX_MALE = 0;
    /**
     * 默认女性
     */
    public static final int SEX_FEMALE = 1;

    private String id;
    private int sex;
    private String nickName;
    private String phone;
    private String password;
    private String introduction;
    private String avatar;

}
