package com.lin_chen.service;

import com.lin_chen.po.User;
import com.lin_chen.vo.SimpleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * 用户相关逻辑
 **/
@Service
public class UserService {

    @Autowired
    MongoOperations mongoOperations;

    public SimpleUser getUserById(String userId) {
        User user = mongoOperations.findById(userId, User.class);
        return new SimpleUser(user);
    }

    public boolean updateUser(String userId, User user) {
        return mongoOperations.updateFirst(Query.query(Criteria.where("_id").is(userId)),
                Update.update("username", user.getUsername())
                        .set("nickName", user.getNickName())
                        .set("sex", user.getSex()),
                User.class).getModifiedCount() > 0;
    }

}
