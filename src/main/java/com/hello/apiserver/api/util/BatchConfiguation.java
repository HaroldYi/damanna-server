package com.hello.apiserver.api.util;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguation {
    @Bean
    public ResourcelessTransactionManager transactionManager() { //MapJobRepositoryFactoryBean 주입하기 위한 필수 Bean
        return new ResourcelessTransactionManager();
    }

    @Bean //비 영구적 인 메모리 DAO 구현을 사용하여 SimpleJobRepository의 생성을 자동화하는 FactoryBean 메타데이터를 기록하고 싶지 않다면, 메모리에 메타데이터를 저장할
    public MapJobRepositoryFactoryBean mapJobRepositoryFactory(ResourcelessTransactionManager txManager)
            throws Exception {
        MapJobRepositoryFactoryBean factory = new MapJobRepositoryFactoryBean(txManager);
        factory.afterPropertiesSet();

        return factory;
    }

    @Bean //데이터 소스 사용 저장소 설정 bean
    public JobRepository jobRepository(MapJobRepositoryFactoryBean factory) throws Exception {
        return factory.getObject();
    }

    @Bean //jobLauncher 를 사용하기 위한 bean //http://opennote46.tistory.com/76
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher launcher = new SimpleJobLauncher();
        launcher.setJobRepository(jobRepository);
        return launcher;
    }
}
