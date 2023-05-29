package com.barbosa.ms.productmgmt.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.barbosa.ms.productmgmt.domain.dto.ProductResponseDTO;
import com.barbosa.ms.productmgmt.domain.dto.ProductRequestDTO;
import com.barbosa.ms.productmgmt.domain.records.ProductRecord;
import com.barbosa.ms.productmgmt.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product", description = "Endpoints for product operations")
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductService service;

    @Operation(summary = "Create product", description = "Create a new product", tags = {"Product"})
    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO dto) throws Exception {

        ProductRecord record = service.create(new ProductRecord(null, dto.getName(), dto.getUUIDCategory()));
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(record.id())
            .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Find product by Id", description = "Find product by id", tags = {"Product"})
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable("id") String id) {
        ProductRecord record = service.findById(UUID.fromString(id));
        ProductResponseDTO response = ProductResponseDTO.builder()
            .id(record.id().toString())
            .name(record.name())
            .idCategory(record.idCategory().toString())
            .build();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Update product by Id", description = "Update product by id", tags = {"Product"})
    @PutMapping ("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody ProductRequestDTO dto) {
        service.update(new ProductRecord(UUID.fromString(id), dto.getName(), dto.getUUIDCategory()));
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Delete product by Id", description = "Delete product by id", tags = {"Product"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
