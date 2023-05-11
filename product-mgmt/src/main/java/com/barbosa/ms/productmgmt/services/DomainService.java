package com.barbosa.ms.productmgmt.services;

import org.springframework.http.ResponseEntity;

public interface DomainService {
    ResponseEntity<?> create();
    ResponseEntity<?> findById();
    ResponseEntity<Void> update();
    ResponseEntity<Void> delete();
}
