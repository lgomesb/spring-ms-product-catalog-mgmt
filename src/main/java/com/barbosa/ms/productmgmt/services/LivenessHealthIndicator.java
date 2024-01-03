package com.barbosa.ms.productmgmt.services;

import com.barbosa.ms.productmgmt.domain.vo.HealthVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnEnabledHealthIndicator("random")
public class LivenessHealthIndicator implements HealthIndicator {

    @Autowired
    private HealthVO healthVO;

    @Override
    public Health health() {
        Health.Builder status = Health.down();
        if (healthVO.isValue()) {
            status = Health.up();
        }
        return status.build();
    }

}
