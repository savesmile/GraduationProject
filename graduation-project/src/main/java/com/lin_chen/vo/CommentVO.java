package com.lin_chen.vo;

import com.lin_chen.po.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommentVO {
    private String id;
    private String userId;
    private String belongTopicId;
    private String belongTopic;
    private String authorAvatar;
    private String authorName;
    private String content;
    private Date createTime;
    private List<CommentVO> sunComment;

    public static CommentVO switchCommentVO(Comment comment) {
        CommentVO vo = new CommentVO();
        vo.setContent(comment.getContent());
        vo.setId(comment.getId());
        vo.setUserId(comment.getUserId());
        vo.setSunComment(Collections.emptyList());
        vo.setCreateTime(comment.getCommitTime());
        return vo;
    }

    public static CommentVO addSunComment(CommentVO father, CommentVO sun) {
        List<CommentVO> sunComments = father.getSunComment();
        if (sunComments.isEmpty()) {
            father.setSunComment(new ArrayList<>(Collections.singletonList(sun)));
        } else {
            sunComments.add(sun);
            father.setSunComment(sunComments);
        }
        return father;
    }

}
