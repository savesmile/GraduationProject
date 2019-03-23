package com.lin_chen.controller;

import com.lin_chen.po.Comment;
import com.lin_chen.po.Topic;
import com.lin_chen.service.CommentService;
import com.lin_chen.service.UserService;
import com.lin_chen.util.StringUtils;
import com.lin_chen.util.UserId;
import com.lin_chen.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author F_lin
 * @since 2019/3/23
 **/
@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;
    @Autowired
    MongoOperations mongoOperations;

    @GetMapping("/index")
    public List<TopicIndexVO> getTopicList(
            @UserId(required = false) String userId,
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "5", required = false) Integer size) {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "createTime"));
        query.skip((page - 1) * size);
        query.limit(size);
        if (StringUtils.isNotEmpty(userId)) {
            query.addCriteria(Criteria.where("userId").is(userId));
        }
        List<Topic> topics = mongoOperations.find(query, Topic.class);
        if (!topics.isEmpty()) {
            return topics.stream().map(topic -> {
                TopicIndexVO topicIndexVO = TopicIndexVO.switchTopicVO(topic);
                SimpleUser author = userService.getUserById(topic.getUserId());
                topicIndexVO.setAuthorName(author.getUsername());
                int count = commentService.getCommentCountByTopId(topic.getId());
                topicIndexVO.setCommentNum(count);
                LastCommentInFo lastCommentInfo = commentService.getLastCommentUser(topic.getId());
                SimpleUser lastUser = userService.getUserById(lastCommentInfo.getUserId());
                topicIndexVO.setLastAnswerName(lastUser.getUsername());
                topicIndexVO.setLastCommentTime(lastCommentInfo.getCommentTime());
                return topicIndexVO;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @PostMapping("/one")
    public void postTopic(@UserId String userId,
                          @RequestBody PostTopicVO postTopicVO) {
        Topic topic = new Topic();
        topic.setTitle(postTopicVO.getTitle());
        topic.setContent(postTopicVO.getContent());
        topic.setUserId(userId);
        topic.setCreateTime(new Date());
        topic.setType(postTopicVO.getType());
    }

    @GetMapping("/one/{id}")
    public TopicVO getTopic(@PathVariable("id") String topicId) {
        Topic topic = mongoOperations.findById(topicId, Topic.class);
        if (topic == null) return new TopicVO();
        TopicVO vo = new TopicVO();
        vo.setTitle(topic.getTitle());
        vo.setContent(topic.getContent());
        vo.setAuthorId(topic.getUserId());
        vo.setComments(Collections.emptyList());
        List<Comment> comments = commentService.getCommentsByTopicId(topicId);
        if (!comments.isEmpty()) {
            HashMap<String, CommentVO> firstLevelComment = new HashMap<>();
            for (Comment comment : comments) {
                if (comment.isFirstLevelReply()) {
                    firstLevelComment.put(comment.getId(), CommentVO.switchCommentVO(comment));
                }
            }

            List<CommentVO> commentVOS = comments.stream()
                    .filter(comment -> !comment.isFirstLevelReply())
                    .map(comment -> {
                        CommentVO fatherCommentVO = firstLevelComment.get(comment.getId());
                        return CommentVO.addSunComment(fatherCommentVO, comment);
                    })
                    .collect(Collectors.toList());
            vo.setComments(commentVOS);
        }
        return vo;
    }

}
