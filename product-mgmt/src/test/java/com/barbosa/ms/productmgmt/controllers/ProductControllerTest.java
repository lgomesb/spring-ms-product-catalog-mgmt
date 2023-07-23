package com.barbosa.ms.productmgmt.controllers;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

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
public class ProductControllerTest {

    private static UUID UUID_CATEGORY;
    private static UUID UUID_PRODUCT;
    private static final String CATEGORY_URI = "/category";
    private static final String PRODUCT_URI = "/product";
    private static final String HTTP_LOCALHOST = "http://localhost:";

    @Value(value="${local.server.port}")
    private int port;

    @InjectMocks
    private ProductController controller;
    
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(0)
    public void shouldSuccededWhenCallCreateCategory() {

        Response response = given()
            .baseUri(HTTP_LOCALHOST + port)
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
        String idCategory = response.getHeader("Location").replace(HTTP_LOCALHOST + port + CATEGORY_URI + "/", "");
        assertNotNull(idCategory);
        assertTrue(!idCategory.isEmpty());
        UUID_CATEGORY = UUID.fromString(idCategory);

    }

    @Test
    @Order(1)
    public void shouldSuccededWhenCallCreate() {

        Response response = given()
            .baseUri(HTTP_LOCALHOST + port)
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
        String idProduct = response.getHeader("Location").replace(HTTP_LOCALHOST + port + PRODUCT_URI + "/", "");
        assertNotNull(idProduct);
        assertTrue(!idProduct.isEmpty());
        UUID_PRODUCT = UUID.fromString(idProduct);

    }

    @Test
    @Order(2)
    public void shouldSuccededWhenCallFindById() {

        Response response = given()
            .baseUri(HTTP_LOCALHOST + port)
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
    public void shouldSuccededWhenCallUpdate() {

        given()
            .baseUri(HTTP_LOCALHOST + port)
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
    public void shouldSuccededWhenCallDelete() {

        given()
            .baseUri(HTTP_LOCALHOST + port)
            .contentType(ContentType.JSON)
            .pathParam("id", UUID_PRODUCT.toString())
            .log().all()
            .when()
            .delete(PRODUCT_URI + "/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());

    }
}
