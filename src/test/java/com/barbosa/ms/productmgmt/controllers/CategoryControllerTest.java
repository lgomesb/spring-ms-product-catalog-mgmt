package com.barbosa.ms.productmgmt.controllers;

import com.barbosa.ms.productmgmt.ProductMgmtApplicationTests;
import com.barbosa.ms.productmgmt.controller.CategoryController;
import com.barbosa.ms.productmgmt.domain.records.CategoryRecord;
import com.barbosa.ms.productmgmt.domain.records.ProductRecord;
import com.barbosa.ms.productmgmt.services.CategoryService;
import com.barbosa.ms.productmgmt.services.ProductService;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;



@ActiveProfiles(value = "test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = {ProductMgmtApplicationTests.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class CategoryControllerTest {

    private static UUID uuidCategory;
    private static final String CATEGORY_URI = "/category";

    @LocalServerPort
    private int port;

    @MockBean
    private CategoryService service;

    @MockBean
    private ProductService productService;

    @InjectMocks
    private CategoryController controller;

    private CategoryRecord categoryRecord;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        categoryRecord = CategoryRecord.builder()
                .id(UUID.randomUUID())
                .name("Test-Category-01")
                .build();

    }


    @Test
    @DisplayName("Create a new Category")
    @Order(0)
    void shouldSucceededWhenCallCreate() {

        when(service.create(any(CategoryRecord.class))).thenReturn(categoryRecord);

        Response response = given()
                .port(port)
                .contentType(ContentType.JSON)
                .body("{\"name\": \""+ categoryRecord.name() +"\"}")
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
        assertNotNull(idCategory);
        assertFalse(idCategory.isEmpty());
        uuidCategory = UUID.fromString(idCategory);

    }

    @Test
    @DisplayName("Find a Category by Id")
    @Order(1)
    void shouldSucceededWhenCallFindById() {
        when(service.findById(any(UUID.class))).thenReturn(categoryRecord);

        Response response = given()
            .port(port)
            .contentType(ContentType.JSON)
            .pathParam("id", uuidCategory.toString())
            .when()
            .get(CATEGORY_URI + "/{id}")
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
    @DisplayName("Update a Category")
    @Order(2)
    void shouldSucceededWhenCallUpdate() {

        given()
            .port(port)
            .contentType(ContentType.JSON)
            .pathParam("id", uuidCategory.toString())
            .body("{\"name\": \"Teste-2\"}")
            .log().all()
            .when()
            .put(CATEGORY_URI + "/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.ACCEPTED.value());

    }

    @Test
    @DisplayName("Delete a Category")
    @Order(3)
    void shouldSucceededWhenCallDelete() {

        given()
            .port(port)
            .contentType(ContentType.JSON)
            .pathParam("id", uuidCategory.toString())
            .log().all()
            .when()
            .delete(CATEGORY_URI + "/{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());

    }

    @Test
    @DisplayName("List all Categories")
    @Order(4)
    void shouldSucceededWhenCallListAll() {
        when(service.listAll()).thenReturn(
                Collections.singletonList(new CategoryRecord(uuidCategory, "Test-Category-01")));

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get(CATEGORY_URI.concat("/all"))
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

    }

    @Test
    @DisplayName("List Pageable Categories")
    @Order(5)
    void shouldSucceededWhenCallGetPageableCategories() {
        when(service.search(anyString(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(categoryRecord)));

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get(CATEGORY_URI)
                .then()
                .assertThat()
                .statusCode(HttpStatus.PARTIAL_CONTENT.value());

    }

    @Test
    @DisplayName("Get Products by a Category")
    @Order(6)
    void shouldSucceededWhenCallGetProductsByCategory() {
        ProductRecord productRecord = ProductRecord.builder()
                .id(UUID.randomUUID())
                .name("Test-Product-01")
                .category(new CategoryRecord(uuidCategory, null))
                .build();

        when(productService.findByCategory(any(UUID.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(productRecord)));

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .pathParam("id", uuidCategory.toString())
                .log().all()
                .when()
                .get(CATEGORY_URI + "/{id}/products")
                .then()
                .assertThat()
                .statusCode(HttpStatus.PARTIAL_CONTENT.value());

    }
}
