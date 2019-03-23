package com.lin_chen.po;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * 评论
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private String id;
    private String userId;
    private String topicId;
    private String toUserId;
    /**
     * 父评论id
     */
    private String fatherCommentId;
    /**
     * 内容
     */
    private String content;
    private Date commitTime;

    /**
     * 判断是否是一级评论
     */
    public boolean isFirstLevelReply() {
        return this.fatherCommentId == null;
    }
}
