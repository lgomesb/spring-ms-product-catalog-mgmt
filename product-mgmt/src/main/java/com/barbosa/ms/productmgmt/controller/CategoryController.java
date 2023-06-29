package com.barbosa.ms.productmgmt.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

import com.barbosa.ms.productmgmt.domain.dto.CategoryResponseDTO;
import com.barbosa.ms.productmgmt.domain.dto.CategoryRequestDTO;
import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;
import com.barbosa.ms.productmgmt.services.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Category", description = "Endpoints for category operations")
@RestController
@RequestMapping(value = "/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @Operation(summary = "Create category", description = "Create a new category", tags = {"Category"})
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> create(@RequestBody @Valid CategoryRequestDTO dto) throws Exception {

        CategoryRecord record = service.create(new CategoryRecord(null, dto.getName()));
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(record.id())
            .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Find category by Id", description = "Find category by id", tags = {"Category"})
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable("id") String id) {
        CategoryRecord record = service.findById(UUID.fromString(id));
        return ResponseEntity.ok().body(new CategoryResponseDTO(record.id(), record.name()));
    }

    @Operation(summary = "Update category by Id", description = "Update category by id", tags = {"Category"})
    @PutMapping ("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody CategoryRequestDTO dto) {
        service.update(new CategoryRecord(UUID.fromString(id), dto.getName()));
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Delete category by Id", description = "Delete category by id", tags = {"Category"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all categories", description = "List all categories in the database", tags = {"Category"})
    @GetMapping()
    public ResponseEntity<List<CategoryResponseDTO>> listAll() {
        List<CategoryResponseDTO> categories = service.listAll()
            .stream()
            .map(CategoryResponseDTO::create)
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(categories);
    }

}
