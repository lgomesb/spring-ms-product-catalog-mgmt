package com.barbosa.ms.productmgmt.controller;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.barbosa.ms.productmgmt.domain.dto.CategoryResponseDTO;
import com.barbosa.ms.productmgmt.domain.dto.CreateCategoryDTO;
import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;
import com.barbosa.ms.productmgmt.services.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Category", description = "Endpoints for category operations")
@RestController
@RequestMapping(value = "/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
 
    @Operation(summary = "Create category", description = "Create a new category", tags = {"Category"})
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody CreateCategoryDTO dto) throws Exception {

        CategoryRecord record = categoryService.create(new CategoryRecord(null, dto.getName()));
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(record.id())
            .toUri();
        return ResponseEntity.created(location).build();
    }

}
