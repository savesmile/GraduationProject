package com.lin_chen.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author F_lin
 * @since 2019/3/23
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PostCommentVO {
    private String toUserId;
    private String content;
    private String topicId;
    private String fatherCommentId;

}
