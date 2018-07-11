package com.hello.apiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;

@SpringBootApplication
@EnableAutoConfiguration
public class HelloserverApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(HelloserverApplication.class, args);
//        FileInputStream serviceAccount = new FileInputStream(new PathMatchingResourcePatternResolver().getResource("classpath:/firebase/jejumate-e540b-firebase-adminsdk-q10ta-163789a2d2.json").getFile().getAbsolutePath());
//
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .setDatabaseUrl("https://<DATABASE_NAME>.firebaseio.com/")
//                .build();
//
//        FirebaseApp.initializeApp(options);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(HelloserverApplication.class);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setQueueCapacity(50);
        taskExecutor.setMaxPoolSize(20);

        return taskExecutor;
    }
}
