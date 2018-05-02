package com.hello.apiserver.api.util;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@SpringBootApplication
@MapperScan(value={"com.hello.apiserver.api.*.mapper"})
public class MybatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource ds) throws Exception{
        //데이터 소스를 활용한 접속
        SqlSessionFactoryBean sessionBean = new SqlSessionFactoryBean();
        sessionBean.setDataSource(ds);
        //mapper.xml 리소스 가져 오기
        String path="classpath:mapper/*Mapper.xml";
        Resource[] res = new PathMatchingResourcePatternResolver().getResources(path);
        //resultType, paramType 에 풀 패키지 경로를 쓰지 않기 위해 이곳에 설정
        sessionBean.setTypeAliasesPackage("com.hello.apiserver.api.*.mapper");
        //리소스를 세션 팩토리에 적용
        sessionBean.setMapperLocations(res);

        return sessionBean.getObject();
    }

}