package com.lin_chen.service;

import com.lin_chen.po.Comment;
import com.lin_chen.vo.LastCommentInFo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论相关逻辑
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
        if (one == null) return null;
        return new LastCommentInFo().setUserId(one.getUserId()).setCommentTime(one.getCommitTime());
    }

    public List<Comment> getCommentsByTopicId(String topicId) {
        Query query = Query.query(Criteria.where("topicId").is(topicId));
        query.with(new Sort(Sort.Direction.DESC, "_id"));
        return mongoOperations.find(query, Comment.class);
    }


    public boolean updateComment(String commentId, Comment comment) {
        return mongoOperations.updateFirst(Query.query(Criteria.where("_id").is(commentId)),
                Update.update("content", comment.getContent()),
                Comment.class).getModifiedCount() > 0;
    }

}
