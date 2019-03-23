package com.lin_chen.vo;

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
@NoArgsConstructor
@Accessors(chain = true)
public class PostTopicVO {
    private String title;
    private String content;
    private String type;
}
