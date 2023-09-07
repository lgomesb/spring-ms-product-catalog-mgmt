package com.barbosa.ms.productmgmt.controllers;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.UUID;

import com.barbosa.ms.productmgmt.domain.records.ProductRecord;
import com.barbosa.ms.productmgmt.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import com.barbosa.ms.productmgmt.ProductMgmtApplicationTests;
import com.barbosa.ms.productmgmt.controller.ProductController;

import io.restassured.http.ContentType;
import io.restassured.response.Response;


@ActiveProfiles(value = "test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = {ProductMgmtApplicationTests.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class ProductControllerTest {

    private static UUID UUID_CATEGORY;
    private static UUID UUID_PRODUCT;
    private static final String CATEGORY_URI = "/category";
    private static final String PRODUCT_URI = "/product";

    @LocalServerPort
    private int port;

    @InjectMocks
    private ProductController controller;

    @MockBean
    private ProductService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(0)
    void shouldSucceededWhenCallCreateCategory() {

        Response response = given()
            .port(port)
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Teste\"}")
            .log().all()
            .when()
            .post(CATEGORY_URI)
            .then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .response();
            
        assertNotNull(response);
        String idCategory = response.getHeader("Location");
        idCategory = idCategory.substring(idCategory.lastIndexOf("/")+1);
        assertFalse(idCategory.isEmpty());
        UUID_CATEGORY = UUID.fromString(idCategory);

    }

    @Test
    @Order(1)
    void shouldSucceededWhenCallCreate() {

        when(service.create(any(ProductRecord.class)))
                .thenReturn(new ProductRecord(UUID.randomUUID(), "Teste", UUID_CATEGORY));

        Response response = given()
            .port(port)
            .contentType(ContentType.JSON)
            .body(String.format("{\"name\": \"Teste\", \"idCategory\":\"%s\"}", UUID_CATEGORY.toString()))
            .log().all()
            .when()
            .post(PRODUCT_URI)
            .then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .response();
            
        assertNotNull(response);
        String idProduct = response.getHeader("Location");
        idProduct = idProduct.substring(idProduct.lastIndexOf("/")+1);
        assertNotNull(idProduct);
        assertFalse(idProduct.isEmpty());
        UUID_PRODUCT = UUID.fromString(idProduct);

    }

    @Test
    @Order(2)
    void shouldSucceededWhenCallFindById() {

        when(service.findById(any(UUID.class)))
                .thenReturn(new ProductRecord(UUID.randomUUID(), "Teste", UUID_CATEGORY));

        Response response = given()
            .port(port)
            .contentType(ContentType.JSON)
            .pathParam("id", UUID_PRODUCT.toString())
            .when()
            .get(PRODUCT_URI + "/{id}")
            .then()
            .log().all()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .response();

        assertNotNull(response);
        assertNotNull(response.getBody().jsonPath().getString("id"));

    }

    @Test
    @Order(3)
    void shouldSucceededWhenCallUpdate() {

        given()
            .port(port)
            .contentType(ContentType.JSON)
            .pathParam("id", UUID_PRODUCT.toString())
            .body(String.format("{\"name\": \"Teste-Update\", \"idCategory\":\"%s\"}", UUID_CATEGORY.toString()))
            .log().all()
            .when()
            .put(PRODUCT_URI + "/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.ACCEPTED.value());

    }

    @Test
    @Order(4)
    void shouldSucceededWhenCallDelete() {

        given()
            .port(port)
            .contentType(ContentType.JSON)
            .pathParam("id", UUID_PRODUCT.toString())
            .log().all()
            .when()
            .delete(PRODUCT_URI + "/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());

    }

    @Test
    @Order(5)
    void shouldSucceededWhenCallSearch() {
        when(service.search(anyString(), any(PageRequest.class)))
                .thenReturn(new PageImpl<ProductRecord>(
                        Collections.singletonList(
                                new ProductRecord(UUID.randomUUID(), "Test-Product-01", UUID_CATEGORY))));

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .queryParam("name", "Teste")
                .log().all()
                .when()
                .get(PRODUCT_URI)
                .then()
                .assertThat()
                .statusCode(HttpStatus.PARTIAL_CONTENT.value());
    }
}
