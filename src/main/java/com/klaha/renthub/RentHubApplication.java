package com.klaha.renthub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication
@EnableRedisRepositories(basePackages ="com.klaha.renthub",
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaRepository.class))
@EnableJpaRepositories(basePackages = "com.klaha.renthub",
includeFilters = @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE,
        classes = JpaRepository.class))
public class RentHubApplication {

    public static void main(String[] args) {

        SpringApplication.run(RentHubApplication.class, args);
        
    }

}
