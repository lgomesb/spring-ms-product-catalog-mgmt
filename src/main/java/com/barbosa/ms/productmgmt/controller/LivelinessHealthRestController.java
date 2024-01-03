package com.barbosa.ms.productmgmt.controller;

import com.barbosa.ms.productmgmt.domain.vo.HealthVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LivelinessHealthRestController {

    private final HealthVO healthVO;

    @Autowired
    public LivelinessHealthRestController(HealthVO healthVO) {
        this.healthVO = healthVO;
    }

    @GetMapping("/switch")
    public ResponseEntity<String> getStatusHealth() {
        String retVal = String.valueOf(healthVO.isValue());
        return ResponseEntity.ok(retVal);
    }

    @PostMapping("/switch")
    public ResponseEntity<String> switchHealth() {
        if(healthVO.isValue()) {
            healthVO.setValue(Boolean.FALSE);
        } else {
            healthVO.setValue(Boolean.TRUE);
        }
        String retVal = String.valueOf(healthVO.isValue());
        return ResponseEntity.ok(retVal);
    }
}
