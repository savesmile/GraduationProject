package com.lin_chen.service;

import com.lin_chen.po.Comment;
import com.lin_chen.vo.LastCommentInFo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author F_lin
 * @since 2019/3/23
 **/
@Service
public class CommentService {
    @Autowired
    MongoOperations mongoOperations;

    public int getCommentCountByTopId(String topicId) {
        long count = mongoOperations.count(Query.query(Criteria.where("topicId").is(topicId)), Comment.class);
        return (int) count;
    }

    public LastCommentInFo getLastCommentUser(String topId) {
        Query query = Query.query(Criteria.where("topId").is(topId));
        query.with(new Sort(Sort.Direction.DESC, "commitTime"));
        query.fields().include("userId").include("commitTime");
        query.limit(1);
        Comment one = mongoOperations.findOne(query, Comment.class);
        return new LastCommentInFo().setUserId(one.getUserId()).setCommentTime(one.getCommitTime());
    }

    public List<Comment> getCommentsByTopicId(String topicId) {
        return mongoOperations.find(Query.query(Criteria.where("topicId").is(topicId)), Comment.class);
    }

}