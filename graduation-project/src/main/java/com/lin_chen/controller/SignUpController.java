package com.lin_chen.controller;

import com.lin_chen.filter.Token;
import com.lin_chen.po.JsonResult;
import com.lin_chen.vo.SignPosts;
import com.lin_chen.po.User;
import com.lin_chen.util.MD5Util;
import com.lin_chen.util.MapBuilder;
import com.lin_chen.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册
 **/
@RestController
@RequestMapping("/api/user/sign-up")
public class SignUpController {

    private static final String DEFAULT_AVATAR = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1552742446263&di=9188d9643a8549b77add4fceeb204faa&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201709%2F06%2F20170906180625_YNysd.jpeg";
    @Autowired
    MongoOperations mongoOperations;

    /**
     * 注册并返回token
     *
     * @param signUpPosts
     * @return
     */
    @PostMapping
    public Object signUp(@RequestBody SignPosts signUpPosts) {
        if (mongoOperations.exists(Query.query(Criteria.where("phone").is(signUpPosts.getPhone())), User.class)) {
            return JsonResult.error("该用户名已经存在!");
        }
        mongoOperations.save(new User()
                .setAvatar(DEFAULT_AVATAR)
                .setNickName("用户" + signUpPosts.getPhone())
                .setPhone(signUpPosts.getPhone())
                .setPassword(MD5Util.getMD5(signUpPosts.getPassword())));
        User user = mongoOperations.findOne(Query.query(Criteria.where("phone").is(signUpPosts.getPhone())), User.class);
        if (user == null) {
            return JsonResult.error("user不存在");
        }
        return JsonResult.success(MapBuilder.of("token", TokenUtils.encryptionToken(new Token(user.getId()))));
    }
}
