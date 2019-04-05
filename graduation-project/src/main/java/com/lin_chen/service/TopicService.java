package com.lin_chen.service;

import com.lin_chen.po.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

/**
 * 帖子相关逻辑
 **/
@Component
public class TopicService {

    @Autowired
    MongoOperations mongoOperations;


    public boolean updateTopic(String topicId, Topic topic) {
        return mongoOperations.updateFirst(Query.query(Criteria.where("_id").is(topicId)),
                Update.update("content", topic.getContent())
                        .set("title", topic.getTitle()),
                Topic.class).getModifiedCount() > 0;
    }

}
