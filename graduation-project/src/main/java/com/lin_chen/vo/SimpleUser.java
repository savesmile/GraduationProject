package com.lin_chen.vo;

import com.lin_chen.po.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 简单的user实体
 **/
@Setter
@Getter
@NoArgsConstructor
public class SimpleUser {
    private String username;
    private String avatar;
    private String nickName;

    public SimpleUser(User user) {
        this.username = user.getUsername();
        this.avatar = user.getAvatar();
        this.nickName = user.getNickName();
    }
}
