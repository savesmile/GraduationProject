package com.lin_chen.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author F_lin
 * @since 2019/3/23
 **/
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class LastCommentInFo {
    private String userId;
    private Date commentTime;
}
