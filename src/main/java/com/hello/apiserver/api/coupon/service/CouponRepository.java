package com.hello.apiserver.api.coupon.service;

import com.hello.apiserver.api.coupon.vo.CouponVo;
import org.springframework.data.repository.CrudRepository;

public interface CouponRepository extends CrudRepository<CouponVo, Long> {
    CouponVo findById(String id);
}
