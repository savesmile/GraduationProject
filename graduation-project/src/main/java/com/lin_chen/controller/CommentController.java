package com.lin_chen.controller;

import com.lin_chen.po.Comment;
import com.lin_chen.util.UserId;
import com.lin_chen.vo.PostCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author F_lin
 * @since 2019/3/23
 **/
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    MongoOperations mongoOperations;

    @PostMapping("/one")
    public void postComment(@UserId String userId,
                            @RequestBody PostCommentVO postCommentVO) {
        Comment comment = new Comment();
        comment.setContent(postCommentVO.getContent());
        comment.setTopicId(postCommentVO.getTopicId());
        comment.setCommitTime(new Date());
        comment.setToUserId(postCommentVO.getToUserId());
        comment.setFatherCommentId(postCommentVO.getFatherCommentId());
        comment.setUserId(userId);
        mongoOperations.save(comment);
    }


    @GetMapping("/me")
    public List<Comment> getMyComment(@UserId String userId) {
        return mongoOperations.find(Query.query(Criteria.where("toUserId").is(userId)), Comment.class);
    }

}
