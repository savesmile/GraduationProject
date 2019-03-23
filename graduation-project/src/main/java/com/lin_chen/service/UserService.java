package com.lin_chen.service;

import com.lin_chen.po.User;
import com.lin_chen.vo.SimpleUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

/**
 * @author F_lin
 * @since 2019/3/23
 **/
@Service
public class UserService {
    @Autowired
    MongoOperations mongoOperations;

    public SimpleUser getUserById(String userId) {
        User user = mongoOperations.findById(userId, User.class);
        return new SimpleUser(user);
    }


}
