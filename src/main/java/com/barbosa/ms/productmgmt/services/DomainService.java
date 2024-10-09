package com.barbosa.ms.productmgmt.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface DomainService<T> {
    T create(T recordObject);
    T findById(UUID id);
    void update(T recordObject);
    void delete(UUID id);
    Page<T> listAll(PageRequest pageRequest);

}
