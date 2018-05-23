package com.hello.apiserver.api.meet.service;

import com.hello.apiserver.api.comment.vo.CommentVo;
import com.hello.apiserver.api.meet.vo.LikeSayVo;
import com.hello.apiserver.api.meet.vo.MeetVo;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.List;

public class MeetRepositoryImpl implements MeetRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MeetVo> getMeetList(int page) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MeetVo> criteriaQuery = criteriaBuilder.createQuery(MeetVo.class);

        Root<MeetVo> meetVoRoot = criteriaQuery.from(MeetVo.class);

//        Predicate id = criteriaBuilder.equal(meetVoRoot.get("id"), id);
        Join<MeetVo, CommentVo> join = meetVoRoot.join("comment", JoinType.RIGHT);

        criteriaQuery.select(meetVoRoot)
                     .where(criteriaBuilder.equal(join.get("sortation"), "M"));
//                     .where(criteriaBuilder.equal(id));

        return this.entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public MeetVo getMeet(String id) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MeetVo> criteriaQuery = criteriaBuilder.createQuery(MeetVo.class);

        Root<MeetVo> meetVoRoot = criteriaQuery.from(MeetVo.class);

//        Predicate sortation = criteriaBuilder.equal(meetVoRoot.get("sortation"), "M");
        Predicate pk = criteriaBuilder.equal(meetVoRoot.get("id"), id);
        Join<MeetVo, CommentVo> joinComment = meetVoRoot.join("comment", JoinType.LEFT);
        Join<MeetVo, LikeSayVo> joinLike = meetVoRoot.join("comment", JoinType.LEFT);

        criteriaQuery.select(meetVoRoot)
                .where(criteriaBuilder.equal(joinComment.get("sortation"), "M"))
                .where(criteriaBuilder.equal(joinLike.get("sortation"), "M"))
                .where(pk);

        return this.entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}