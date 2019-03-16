package com.lin_chen.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 注册需要传入的信息
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SignPosts {
    private String phone;
    private String password;
    private Integer sex;
}
