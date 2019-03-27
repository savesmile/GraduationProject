package com.lin_chen.service;

import com.lin_chen.po.Topic;
import com.lin_chen.po.User;
import com.lin_chen.vo.PostTopicVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * @author F_lin
 * @since 2019/3/23
 **/
@Component
public class TopicService {

    @Autowired
    MongoOperations mongoOperations;

    public boolean addFine(String topicId){
        return true;
    }

    public boolean updateTopic(String topicId, Topic topic){
        return mongoOperations.updateFirst(Query.query(Criteria.where("_id").is(topicId)),
                Update.update("content", topic.getContent())
                        .set("title",topic.getTitle()),
                Topic.class).getModifiedCount() > 0;
    }

}
