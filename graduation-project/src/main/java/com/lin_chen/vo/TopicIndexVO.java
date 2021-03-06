package com.lin_chen.vo;

import com.lin_chen.po.Topic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 帖子首页
 **/
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class TopicIndexVO {
    private String id;
    private String title;
    private String content;
    private String type;
    private Date createDate;
    /**
     * 回答数
     */
    private Integer commentNum;
    /**
     * 作者名字
     */
    private String authorName;
    /**
     * 作者头像
     */
    private String authorAvatar;
    /**
     * 最后回答人
     */
    private String lastAnswerName;
    /**
     * 最后回答时间
     */
    private Date lastCommentTime;


    public static TopicIndexVO switchTopicVO(Topic topic) {
        TopicIndexVO topicIndexVO = new TopicIndexVO()
                .setId(topic.getId())
                .setTitle(topic.getTitle())
                .setType(topic.getType())
                .setContent(topic.getContent());
        topicIndexVO.setCreateDate(topic.getCreateTime());
        return topicIndexVO;
    }
}
