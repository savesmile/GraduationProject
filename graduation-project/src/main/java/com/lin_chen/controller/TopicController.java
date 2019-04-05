package com.lin_chen.controller;

import com.lin_chen.po.Comment;
import com.lin_chen.po.JsonResult;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * 帖子相关接口
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
    public Object getTopicList(
            @UserId(required = false) String userId,
            @RequestParam(required = false) String authorId,
            @RequestParam(defaultValue = "0", required = false) Integer page,
            @RequestParam(defaultValue = "5", required = false) Integer size,
            @RequestParam(required = false) String type) {
        Query query = new Query();
        query.with(new Sort(Sort.Direction.DESC, "createTime"));

        if (StringUtils.isNotEmpty(authorId)) {
            query.addCriteria(Criteria.where("userId").is(authorId));
        } else if (StringUtils.isNotEmpty(userId)) {
            query.addCriteria(Criteria.where("userId").is(userId));
        }

        if (StringUtils.isNotEmpty(type) && type.equals("boutique")) {
            query.addCriteria(Criteria.where("isBoutique").is(true));
        }
        int topicCount = (int) mongoOperations.count(query, Topic.class);
        query.skip((page - 1) * size);
        query.limit(size);
        List<Topic> topics = mongoOperations.find(query, Topic.class);
        if (!topics.isEmpty()) {
            List<TopicIndexVO> collect = topics.stream().map(topic -> {
                TopicIndexVO topicIndexVO = TopicIndexVO.switchTopicVO(topic);
                SimpleUser author = userService.getUserById(topic.getUserId());
                topicIndexVO.setAuthorName(author.getUsername());
                topicIndexVO.setAuthorAvatar(author.getAvatar());
                int count = commentService.getCommentCountByTopId(topic.getId());
                topicIndexVO.setCommentNum(count);
                LastCommentInFo lastCommentInfo = commentService.getLastCommentUser(topic.getId());
                if (lastCommentInfo != null) {
                    SimpleUser lastUser = userService.getUserById(lastCommentInfo.getUserId());
                    topicIndexVO.setLastAnswerName(lastUser.getUsername());
                    topicIndexVO.setLastCommentTime(lastCommentInfo.getCommentTime());
                }
                return topicIndexVO;
            }).collect(Collectors.toList());

            int val1 = topicCount / size;
            int val2 = topicCount % size;
            if (val2 != 0) {
                ++val1;
            }
            return JsonResult.success(collect).setPageData(new JsonResult.PageData(val1, page, topicCount));
        }
        return JsonResult.error("无相关数据");
    }

    @PostMapping("/one")
    public JsonResult postTopic(@UserId String userId,
                                @RequestBody PostTopicVO postTopicVO) {
        Topic topic = new Topic();
        topic.setTitle(postTopicVO.getTitle());
        topic.setContent(postTopicVO.getContent());
        topic.setUserId(userId);
        topic.setCreateTime(new Date());
        topic.setType(postTopicVO.getType());
        mongoOperations.save(topic);
        return JsonResult.success();
    }

    @GetMapping("/one/{id}")
    public Object getTopic(@PathVariable("id") String topicId) {
        Topic topic = mongoOperations.findById(topicId, Topic.class);
        if (topic == null) return JsonResult.error("错误的帖子id");
        TopicVO vo = new TopicVO();
        vo.setId(topicId);
        vo.setTitle(topic.getTitle());
        vo.setContent(topic.getContent());
        vo.setAuthorId(topic.getUserId());
        vo.setComments(Collections.emptyList());
        vo.setCreateTime(topic.getCreateTime());
        SimpleUser author = userService.getUserById(topic.getUserId());
        vo.setAuthorAvatar(author.getAvatar());
        vo.setAuthorName(author.getNickName());

        List<Comment> comments = commentService.getCommentsByTopicId(topicId);
        if (!comments.isEmpty()) {
            HashMap<String, CommentVO> firstLevelComment = new HashMap<>();
            for (Comment comment : comments) {
                if (comment.isFirstLevelReply()) {
                    CommentVO commentVO = CommentVO.switchCommentVO(comment);
                    SimpleUser user1 = userService.getUserById(comment.getUserId());
                    commentVO.setAuthorAvatar(user1.getAvatar());
                    commentVO.setAuthorName(user1.getNickName());

                    firstLevelComment.put(comment.getId(), commentVO);
                }
            }

            comments.stream()
                    .filter(comment -> !comment.isFirstLevelReply())
                    .map(comment -> {
                        CommentVO fatherCommentVO = firstLevelComment.get(comment.getFatherCommentId());
                        CommentVO commentVO = CommentVO.switchCommentVO(comment);
                        SimpleUser user2 = userService.getUserById(comment.getUserId());
                        commentVO.setAuthorAvatar(user2.getAvatar());
                        commentVO.setAuthorName(user2.getNickName());

                        CommentVO faCommentVo = CommentVO.addSunComment(fatherCommentVO, commentVO);
                        firstLevelComment.put(faCommentVo.getId(), faCommentVo);

                        return faCommentVo;
                    })
                    .collect(Collectors.toList());

            vo.setComments(new ArrayList<>(firstLevelComment.values()));
        }
        return JsonResult.success(vo);
    }

}
