package com.lin_chen.admin.controller;

import com.alibaba.fastjson.JSONObject;
import com.lin_chen.filter.Token;
import com.lin_chen.po.*;
import com.lin_chen.service.CommentService;
import com.lin_chen.service.TopicService;
import com.lin_chen.service.UserService;
import com.lin_chen.util.MD5Util;
import com.lin_chen.util.MapBuilder;
import com.lin_chen.util.StringUtils;
import com.lin_chen.util.TokenUtils;
import com.lin_chen.vo.CommentVO;
import com.lin_chen.vo.SignPosts;
import com.lin_chen.vo.SimpleUser;
import com.lin_chen.vo.TopicVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理系统相关接口
 **/
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    UserService userService;
    @Autowired
    TopicService topicService;
    @Autowired
    CommentService commentService;

    /**
     * 管理员登陆
     */
    @RequestMapping("/login")
    public JsonResult login(@RequestBody SignPosts signInPosts) {
        if (StringUtils.isEmpty(signInPosts.getPassword()) || StringUtils.isEmpty(signInPosts.getUsername()))
            return JsonResult.error("用户名或密码不能为空");
        admin admin = mongoOperations.findOne(Query.query(Criteria.where("username").is(signInPosts.getUsername())), admin.class);
        String pwd = MD5Util.getMD5(signInPosts.getPassword());
        if (admin == null || (StringUtils.isNotEmpty(pwd) && !pwd.equals(admin.getPassword()))) {
            return JsonResult.error("用户名或密码错误");
        }
        return JsonResult.success(MapBuilder
                .forTypeSO("token", TokenUtils.encryptionToken(new Token(admin.getId())))
                .with("info", admin.setPassword(null)).build());
    }

    /**
     * 获取所有用户
     */
    @GetMapping("/get-users")
    public Object getUserLimit(@RequestParam(defaultValue = "0", required = false) Integer page,
                               @RequestParam(defaultValue = "5", required = false) Integer limit) {
        long count = mongoOperations.count(new Query(), User.class);
        List<User> users = mongoOperations.find(new Query().skip((page - 1) * limit).limit(limit), User.class);
        return AdminResult.success(users, count);
    }

    /**
     * 获取所有帖子
     */
    @GetMapping("/get-topics")
    public Object getTopicsLimit(@RequestParam(defaultValue = "0", required = false) Integer page,
                                 @RequestParam(defaultValue = "5", required = false) Integer limit) {
        long count = mongoOperations.count(new Query(), Topic.class);
        List<Topic> topics = mongoOperations.find(new Query().skip((page - 1) * limit).limit(limit), Topic.class);

        List<TopicVO> result = topics.stream().map(topic -> {
            SimpleUser user = userService.getUserById(topic.getUserId());
            TopicVO vo = new TopicVO();
            vo.setId(topic.getId());
            vo.setCreateTime(topic.getCreateTime());
            vo.setAuthorName(user.getNickName());
            vo.setTitle(topic.getTitle());
            vo.setBoutique(topic.isBoutique());
            vo.setContent(topic.getContent());
            return vo;
        }).collect(Collectors.toList());

        return AdminResult.success(result, count);
    }

    /**
     * 获取所有评论
     */
    @GetMapping("/get-comments")
    public Object getCommentsLimit(@RequestParam(defaultValue = "0", required = false) Integer page,
                                   @RequestParam(defaultValue = "5", required = false) Integer limit) {
        long count = mongoOperations.count(new Query(), Comment.class);
        List<Comment> comments = mongoOperations.find(new Query().skip((page - 1) * limit).limit(limit), Comment.class);
        List<CommentVO> result = comments.stream().map(comment -> {
            CommentVO vo = new CommentVO();
            vo.setId(comment.getId());
            vo.setCreateTime(comment.getCommitTime());
            SimpleUser user = userService.getUserById(comment.getUserId());
            Topic topic = mongoOperations.findOne(Query.query(Criteria.where("_id").is(comment.getTopicId())), Topic.class);
            vo.setAuthorName(user == null ? null : user.getNickName());
            vo.setBelongTopic(topic == null ? null : topic.getTitle());
            vo.setContent(comment.getContent());
            return vo;
        }).collect(Collectors.toList());
        return AdminResult.success(result, count);
    }

    /**
     * 删除 用户/主题/回复
     */
    @DeleteMapping("/delete/{module}/{id}")
    public Object getCommentsLimit(@PathVariable("module") String module,
                                   @PathVariable("id") String id) {

        switch (module) {
            case "user":
                mongoOperations.remove(Query.query(Criteria.where("_id").is(id)), User.class);
                break;
            case "topic":
                mongoOperations.remove(Query.query(Criteria.where("_id").is(id)), Topic.class);
                break;
            case "comment":
                mongoOperations.remove(Query.query(Criteria.where("_id").is(id)), Comment.class);
                break;
        }
        return AdminResult.success();
    }

    /**
     * 更新 /用户/主题/回复 相关信息
     */
    @PostMapping("/update/{module}")
    public Object postData(@RequestBody JSONObject postBody,
                           @PathVariable("module") String module) {
        boolean flag = false;
        switch (module) {
            case "user":
                User user = switchToUser(postBody);
                flag = userService.updateUser(user.getId(), user);
                break;
            case "topic":
                Topic topic = switchToTopic(postBody);
                flag = topicService.updateTopic(topic.getId(), topic);
                break;
            case "comment":
                Comment comment = switchToComment(postBody);
                flag = commentService.updateComment(comment.getId(), comment);
                break;
        }

        return flag ? AdminResult.success() : AdminResult.error("修改失败");
    }

    /**
     * 加精/取消加精帖子
     */
    @PutMapping("/add_fine_topic/{id}/{option}")
    public Object postData(@PathVariable("id") String id, @PathVariable("option") String option) {
        boolean flag = mongoOperations.updateFirst(Query.query(Criteria.where("_id").is(id)),
                Update.update("isBoutique", option.equals("addFine")),
                Topic.class).getModifiedCount() > 0;
        return flag ? AdminResult.success() : AdminResult.error("操作失败");
    }


    private User switchToUser(JSONObject postBody) {
        User user = new User();
        user.setId(postBody.getString("id"));
        user.setUsername(postBody.getString("username"));
        user.setNickName(postBody.getString("nickName"));
        user.setSex(postBody.getInteger("sex"));
        return user;
    }

    private Comment switchToComment(JSONObject postBody) {
        Comment comment = new Comment();
        comment.setId(postBody.getString("id"));
        comment.setContent(postBody.getString("content"));
        return comment;
    }

    private Topic switchToTopic(JSONObject postBody) {
        Topic topic = new Topic();
        topic.setId(postBody.getString("id"));
        topic.setType(postBody.getString("type"));
        topic.setTitle(postBody.getString("title"));
        topic.setContent(postBody.getString("content"));
        return topic;
    }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AdminResult {
        private Integer code;
        private String msg;
        private Object data;
        private Number count;

        public AdminResult(Integer code, Object data, Number count) {
            this.code = code;
            this.data = data;
            this.count = count;
        }

        public static AdminResult success(Object data, Number count) {
            return new AdminResult(0, data, count);
        }

        public static AdminResult success() {
            return new AdminResult(0, null, 0);
        }

        public static AdminResult error(String msg) {
            return new AdminResult(0, msg, null, 0);
        }

    }

}
