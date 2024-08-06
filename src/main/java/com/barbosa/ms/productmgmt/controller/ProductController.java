package com.barbosa.ms.productmgmt.controller;

import com.barbosa.ms.productmgmt.domain.dto.CategoryResponseDTO;
import com.barbosa.ms.productmgmt.domain.dto.ProductRequestDTO;
import com.barbosa.ms.productmgmt.domain.dto.ProductResponseDTO;
import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;
import com.barbosa.ms.productmgmt.domain.records.ProductRecord;
import com.barbosa.ms.productmgmt.exception.StandardError;
import com.barbosa.ms.productmgmt.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Tag(name = "Product", description = "Endpoints for product operations")
@RestController
@RequestMapping(value = "/")
@CrossOrigin("http://localhost:4200")
public class ProductController {

    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @Operation(summary = "Create product", description = "Create a new product", tags = { "Product" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successful.", content = {
                    @Content(schema = @Schema(implementation = ProductResponseDTO.class)) } ),
            @ApiResponse(responseCode = "404", description = "Product not found.", content = {
                    @Content(schema = @Schema(implementation = StandardError.class)) } ),
            @ApiResponse(responseCode = "422", description = "Validation error.", content = {
                    @Content(schema = @Schema(implementation = StandardError.class)) } ),
            @ApiResponse(responseCode = "500", description = "Internal server error.", content = {
                    @Content(schema = @Schema(implementation = StandardError.class)) } )
    })
    @PostMapping(value = "product")
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO dto) {
        ProductRecord productRecord = ProductRecord.builder()
                .name(dto.getName())
                .category(CategoryRecord.builder()
                        .id(dto.getUUIDCategory())
                        .build())
                .build();
        productRecord = service.create(productRecord);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productRecord.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Find product by Id", description = "Find product by id", tags = { "Product" })
    @GetMapping("product/{id}")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable("id") String id) {
        ProductRecord productRecord = service.findById(UUID.fromString(id));
        ProductResponseDTO response = ProductResponseDTO.builder()
                .id(productRecord.id().toString())
                .name(productRecord.name())
                .category(CategoryResponseDTO.fromRecord(productRecord.category()))
                .build();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Update product by Id", description = "Update product by id", tags = { "Product" })
    @PutMapping("product/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody ProductRequestDTO dto) {
        ProductRecord productRecord = ProductRecord.builder()
                .name(dto.getName())
                .category(CategoryRecord.builder()
                        .id(dto.getUUIDCategory())
                        .build())
                .build();

        service.update(productRecord);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Delete product by Id", description = "Delete product by id", tags = { "Product" })
    @DeleteMapping("product/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Product list pageable", description = "List product in the database", tags = { "Product" })
    @GetMapping("product")
    public ResponseEntity<Page<ProductResponseDTO>> search(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "5") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        PageRequest pageRequest = PageRequest.of(page,
                linesPerPage,
                Direction.valueOf(direction.toUpperCase()),
                orderBy);
        Page<ProductRecord> records = service.search(decodeParam(name), pageRequest);
        Page<ProductResponseDTO> products = records.map(ProductResponseDTO::fromRecord);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(products);
    }

    private static String decodeParam(String s) {
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }

}
