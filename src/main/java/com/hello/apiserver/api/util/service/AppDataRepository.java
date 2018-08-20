package com.hello.apiserver.api.util.service;

import com.hello.apiserver.api.util.vo.RegexVo;
import org.springframework.data.repository.CrudRepository;

public interface AppDataRepository extends CrudRepository<RegexVo, Long> {

}
