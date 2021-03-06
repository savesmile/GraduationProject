package com.lin_chen.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TopicVO {
    private String id;
    private String authorId;
    private String authorAvatar;
    private boolean isBoutique;
    private String authorName;
    private String title;
    private String content;
    private Date createTime;
    private List<CommentVO> comments;
}
