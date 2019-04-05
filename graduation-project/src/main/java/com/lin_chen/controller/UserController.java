package com.lin_chen.controller;

import com.lin_chen.po.JsonResult;
import com.lin_chen.po.User;
import com.lin_chen.util.UserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.web.bind.annotation.*;

/**
 * 用户相关接口
 **/
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    MongoOperations mongoOperations;

    /**
     * 获取我的信息
     */
    @GetMapping("/info")
    public Object getMyInfo(@UserId String userId) {
        return JsonResult.success(mongoOperations.findById(userId, User.class));
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/info")
    public Object updateUserInfo(@UserId String userId,
                                 @RequestBody User user) {
        user.setId(userId);
        mongoOperations.updateFirst(
                Query.query(Criteria.where("_id").is(userId)),
                Update.update("sex", user.getSex())
                        .set("nickName", user.getNickName())
                        .set("avatar", user.getAvatar()),
                User.class);
        return JsonResult.success(user);
    }

}
