package com.barbosa.ms.productmgmt.config;

import com.barbosa.ms.productmgmt.domain.vo.HealthVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HealthConfig {

    @Value("${spring.data.redis.password}")
    private String hostCache;

    @Bean
    public HealthVO healthVO() {
        System.out.println("::::::::::::::::: HOST - CACHE :::::::::::: ");
        System.out.println(hostCache);
        return new HealthVO(Boolean.TRUE);
    }
}
