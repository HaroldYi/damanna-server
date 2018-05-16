package com.hello.apiserver.api.meet.service;

import com.hello.apiserver.api.meet.vo.MeetVo;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class MeetRepositoryImpl implements MeetRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<MeetVo> getMeetList(int page) {

        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<MeetVo> criteriaQuery = criteriaBuilder.createQuery(MeetVo.class);

        Root<MeetVo> meetVoRoot = criteriaQuery.from(MeetVo.class);

//        Predicate sortation = criteriaBuilder.equal(meetVoRoot.get("sortation"), "M");

        criteriaQuery.select(meetVoRoot);
//                     .where(sortation);

        return this.entityManager.createQuery(criteriaQuery).getResultList();
    }
}