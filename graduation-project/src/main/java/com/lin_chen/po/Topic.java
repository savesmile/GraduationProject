package com.lin_chen.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 帖子详情
 **/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Topic {
    private String id;
    private String type;
    /**
     * 作者id
     */
    private String userId;
    private String title;
    private String content;
    private Date createTime;
}
