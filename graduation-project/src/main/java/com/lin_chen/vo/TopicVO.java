package com.lin_chen.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

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
public class TopicVO {
    private String id;
    private String authorId;
    private String authorAvatar;
    private String title;
    private String content;
    private List<CommentVO> comments;
}
