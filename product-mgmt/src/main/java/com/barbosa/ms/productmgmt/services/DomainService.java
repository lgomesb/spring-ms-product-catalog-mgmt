package com.barbosa.ms.productmgmt.services;

import java.util.UUID;

public interface DomainService<T> {
    T create(T record);
    T findById(UUID id);
    void update(T record);
    void delete();
}
