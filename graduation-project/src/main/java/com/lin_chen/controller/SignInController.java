package com.lin_chen.controller;

import com.lin_chen.filter.Token;
import com.lin_chen.po.JsonResult;
import com.lin_chen.vo.SignPosts;
import com.lin_chen.po.User;
import com.lin_chen.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

/**
 * 登录
 **/
@RestController
@RequestMapping("/api/user")
public class SignInController {
    private final Logger log = LoggerFactory.getLogger(SignInController.class);

    @Autowired
    MongoOperations mongoOperations;

    /**
     * 登陆并返回token
     */
    @PostMapping("/sign_in")
    public Object signIn(@RequestBody SignPosts signInPosts) {
        if (StringUtils.isEmpty(signInPosts.getPassword()) || StringUtils.isEmpty(signInPosts.getPhone()))
            return JsonResult.error("用户名或密码不能为空");
        User user = mongoOperations.findOne(Query.query(Criteria.where("phone").is(signInPosts.getPhone())), User.class);
        String pwd = MD5Util.getMD5(signInPosts.getPassword());
        if (StringUtils.isNotEmpty(pwd) && !pwd.equals(user.getPassword()))
            return JsonResult.error("用户名或密码错误");
        log.info("用户 {} 登录成功", signInPosts.getPhone());
        return JsonResult.success(MapBuilder
                .forTypeSO("token", TokenUtils.encryptionToken(new Token(user.getId())))
                .with("userInfo", user.setPassword(null)).build());
    }

    @PutMapping("/reset_pwd")
    public Object resetPwd(@UserId String userId,
                           @RequestBody SignPosts resetPosts) {
        mongoOperations.updateFirst(Query.query(Criteria.where("_id").is(userId)),
                Update.update("password", MD5Util.getMD5(resetPosts.getPassword())), User.class);
        return JsonResult.success(MapBuilder.of("result", true));
    }
}