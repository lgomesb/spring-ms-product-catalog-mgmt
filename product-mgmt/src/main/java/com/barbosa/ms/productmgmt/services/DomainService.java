package com.barbosa.ms.productmgmt.services;

import java.util.UUID;

import com.barbosa.ms.productmgmt.domain.dto.DataDTO;
import com.barbosa.ms.productmgmt.domain.dto.ResponseDTO;

public interface DomainService {
    <T extends DataDTO> ResponseDTO create(T dto);
    ResponseDTO findById(UUID id);
    <T extends DataDTO> void update(UUID id, T dto);
    void delete();
}
