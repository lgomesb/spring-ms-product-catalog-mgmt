package com.barbosa.ms.productmgmt.config;

import com.barbosa.ms.productmgmt.domain.vo.HealthVO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HealthConfig {

    @Bean
    public HealthVO healthVO() {
        return new HealthVO(Boolean.TRUE);
    }
}
