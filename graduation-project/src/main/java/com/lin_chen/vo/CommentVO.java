package com.lin_chen.vo;

import com.lin_chen.po.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author F_lin
 * @since 2019/3/23
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CommentVO {
    private String id;
    private String userId;
    private String userAvatar;
    private String content;
    private List<CommentVO> sunComment;

    public static CommentVO switchCommentVO(Comment comment) {
        CommentVO vo = new CommentVO();
        vo.setContent(comment.getContent());
        vo.setId(comment.getId());
        vo.setUserId(comment.getUserId());
        vo.setSunComment(Collections.emptyList());
        return vo;
    }

    public static CommentVO addSunComment(CommentVO father, Comment comment) {
        CommentVO sun = switchCommentVO(comment);
        List<CommentVO> sunComments = father.getSunComment();
        if (sunComments.isEmpty()) {
            father.setSunComment(new ArrayList<>(Collections.singletonList(sun)));
        }
        sunComments.add(sun);
        father.setSunComment(sunComments);
        return father;
    }

}
