package com.hello.apiserver.api.like.service;

import com.hello.apiserver.api.like.vo.LikeSayVo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class LikeRepositoryImpl implements LikeRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<LikeSayVo> findLike(String sayId, String sortation) {
        CriteriaBuilder criteriaBuilder = this.entityManager.getCriteriaBuilder();
        CriteriaQuery<LikeSayVo> criteriaQuery = criteriaBuilder.createQuery(LikeSayVo.class);

        Root<LikeSayVo> meetVoRoot = criteriaQuery.from(LikeSayVo.class);

        Predicate sortationCond = criteriaBuilder.equal(meetVoRoot.get("sortation"), "M");

        criteriaQuery.select(meetVoRoot)
                     .where(sortationCond);

        return this.entityManager.createQuery(criteriaQuery).getResultList();
    }
}