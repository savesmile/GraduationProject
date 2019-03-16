package com.lin_chen.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * token类。用于解析用户信息
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    public static final String DEFAULT_SALT = "bishezhengtamananzuo";
    private String userId;
}

