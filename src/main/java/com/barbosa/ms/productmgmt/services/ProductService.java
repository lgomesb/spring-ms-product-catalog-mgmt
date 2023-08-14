package com.barbosa.ms.productmgmt.services;

import org.springframework.data.domain.Page;

import com.barbosa.ms.productmgmt.domain.records.ProductRecord;

public interface ProductService extends DomainService<ProductRecord> {

    Page<ProductRecord> search(String name, Integer page, Integer linesPerPage, String orderBy,
            String direction);
    
}
