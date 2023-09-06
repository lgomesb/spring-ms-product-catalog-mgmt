package com.barbosa.ms.productmgmt.controller;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.barbosa.ms.productmgmt.domain.dto.ProductResponseDTO;
import com.barbosa.ms.productmgmt.domain.dto.ProductRequestDTO;
import com.barbosa.ms.productmgmt.domain.records.ProductRecord;
import com.barbosa.ms.productmgmt.exception.StandardError;
import com.barbosa.ms.productmgmt.services.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product", description = "Endpoints for product operations")
@RestController
@RequestMapping(value = "/")
public class ProductController {

    @Autowired
    private ProductService service;

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
    public ResponseEntity<ProductResponseDTO> create(@RequestBody ProductRequestDTO dto) throws Exception {

        ProductRecord productRecord = service.create(new ProductRecord(null, dto.getName(), dto.getUUIDCategory()));

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
                .idCategory(productRecord.idCategory().toString())
                .build();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Update product by Id", description = "Update product by id", tags = { "Product" })
    @PutMapping("product/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody ProductRequestDTO dto) {
        service.update(new ProductRecord(UUID.fromString(id), dto.getName(), dto.getUUIDCategory()));
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

        String nameDecoded = ProductController.decodeParam(name);
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
        Page<ProductRecord> records = service.search(nameDecoded, pageRequest);
        Page<ProductResponseDTO> products = records.map(ProductResponseDTO::create);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(products);
    }

    private static String decodeParam(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

}
