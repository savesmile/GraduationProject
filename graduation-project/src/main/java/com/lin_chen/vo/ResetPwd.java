package com.lin_chen.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author F_lin
 * @since 2019/3/24
 **/
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class ResetPwd {
    private String oldPwd;
    private String newPwd;
}
