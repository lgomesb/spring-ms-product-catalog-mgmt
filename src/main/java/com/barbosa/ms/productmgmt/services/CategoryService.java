package com.barbosa.ms.productmgmt.services;

import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface CategoryService extends DomainService<CategoryRecord>{

    Page<CategoryRecord> search(String name, PageRequest pageRequest);
}
