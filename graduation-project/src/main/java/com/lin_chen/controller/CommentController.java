package com.lin_chen.controller;

import com.lin_chen.po.Comment;
import com.lin_chen.po.JsonResult;
import com.lin_chen.po.Topic;
import com.lin_chen.service.UserService;
import com.lin_chen.util.UserId;
import com.lin_chen.vo.CommentVO;
import com.lin_chen.vo.PostCommentVO;
import com.lin_chen.vo.SimpleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author F_lin
 * @since 2019/3/23
 **/
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    UserService userService;

    @PostMapping("/one")
    public Object postComment(@UserId String userId,
                              @RequestBody PostCommentVO postCommentVO) {
        Comment comment = new Comment();
        comment.setContent(postCommentVO.getContent());
        comment.setTopicId(postCommentVO.getTopicId());
        comment.setCommitTime(new Date());
        comment.setToUserId(postCommentVO.getToUserId());
        comment.setFatherCommentId(postCommentVO.getFatherCommentId());
        comment.setUserId(userId);
        mongoOperations.save(comment);
        return JsonResult.success();

    }


    @GetMapping("/me")
    public Object getMyComment(@UserId String userId) {
        List<Comment> comments = mongoOperations.find(Query.query(Criteria.where("toUserId").is(userId)), Comment.class);
        List<CommentVO> result = comments.stream().map(comment -> {
            CommentVO vo = new CommentVO();
            vo.setId(comment.getId());
            vo.setCreateTime(comment.getCommitTime());
            SimpleUser user = userService.getUserById(comment.getUserId());
            Topic topic = mongoOperations.findOne(Query.query(Criteria.where("_id").is(comment.getTopicId())), Topic.class);
            vo.setAuthorName(user == null ? null : user.getNickName());
            vo.setBelongTopicId(topic.getId());
            vo.setContent(comment.getContent());
            return vo;
        }).collect(Collectors.toList());

        return JsonResult.success(result);
    }

}
