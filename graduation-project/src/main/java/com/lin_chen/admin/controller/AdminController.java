package com.lin_chen.admin.controller;

import com.lin_chen.filter.Token;
import com.lin_chen.po.JsonResult;
import com.lin_chen.po.User;
import com.lin_chen.po.admin;
import com.lin_chen.util.MD5Util;
import com.lin_chen.util.MapBuilder;
import com.lin_chen.util.StringUtils;
import com.lin_chen.util.TokenUtils;
import com.lin_chen.vo.SignPosts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限验证相关的接口
 **/
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    MongoOperations mongoOperations;

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

}
