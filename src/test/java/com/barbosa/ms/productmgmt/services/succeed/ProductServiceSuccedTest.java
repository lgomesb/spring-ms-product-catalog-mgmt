package com.barbosa.ms.productmgmt.services.succeed;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.entities.Product;
import com.barbosa.ms.productmgmt.domain.records.ProductRecord;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.repositories.ProductRepository;
import com.barbosa.ms.productmgmt.services.impl.ProductServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class ProductServiceSuccedTest {
    
    @InjectMocks
    private ProductServiceImpl service;
    
    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    private final Given given = new Given();
    private final When when = new When();
    private final Then then = new Then();
    private Product product;
    private Category category;
    private ProductRecord productRecord;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSuccessWhenCreate() {
        given.categoryInicietedForSuccessfulReturn();
        given.productInicietedForSuccessfulReturn();
        given.productRecordInicietedForSuccessfulReturn();
        when.findCategoryById();
        when.saveProductEntity();
        ProductRecord record = when.callCreateInCategorySerivce();
        then.shouldBeSuccessfulValidationRules(record);
    }

    @Test
    void shouldSuccessWhenFindById() {
        given.categoryInicietedForSuccessfulReturn();
        given.productInicietedForSuccessfulReturn();
        when.findProductById();
        ProductRecord record = when.callProductServiceFindById();
        then.shouldBeSuccessfulValidationRules(record);
    }

    @Test
    void shouldSuccessWhenUpdate() {
        given.categoryInicietedForSuccessfulReturn();
        given.productInicietedForSuccessfulReturn();
        given.productRecordInicietedForSuccessfulReturn();
        when.findCategoryById();
        when.findProductById();
        when.saveProductEntity();
        when.callProductServiceUpdate();
        then.shouldBeSuccessfulArgumentValidationUpdate();
    }

    @Test
    void shouldSuccessWhenDelete() {
        given.categoryInicietedForSuccessfulReturn();
        given.productInicietedForSuccessfulReturn();
        given.productRecordInicietedForSuccessfulReturn();
        when.findProductById();
        when.deleteProductEntity();
        when.callProductServiceDelete();
        then.shouldBeSuccessfulArgumentValidationDelete();
    }

    @Test
    void shouldSuccessWhenListAll() {
        given.categoryInicietedForSuccessfulReturn();
        given.productInicietedForSuccessfulReturn();
        when.findAllProducts();
        List<ProductRecord> records = when.callProductServiceListAll();
        then.shouldBeSuccessfulValidationFindAllProducts(records);
    }

    @Test
    void shouldSuccessWhenSearchProduct() {
        given.categoryInicietedForSuccessfulReturn();
        given.productInicietedForSuccessfulReturn();
        when.searchProduct();
        Page<ProductRecord> productRecords = when.callProductServiceSearch();
        then.shouldBeSuccessfulValidationSearchProducts(productRecords);
    }

    @Test
    void shouldSuccessWhenFindProductByCategory() {
        given.categoryInicietedForSuccessfulReturn();
        given.productInicietedForSuccessfulReturn();
        when.findCategoryById();
        when.findDistinctByCategory();
        Page<ProductRecord> productRecords = when.callFindProductByCategory();
        then.shouldBeSuccessfulValidationFindProductByCategory(productRecords);

    }

    class Given {

        public UUID getUUID() {
            return UUID.randomUUID();
        }

        void categoryInicietedForSuccessfulReturn() {
            category = Category.builder()
                .id(getUUID())
                .name("Category-Test-Success")
                .build();
        }

        void productInicietedForSuccessfulReturn() {
            product = Product.builder()
                        .category(category)
                        .name("Test-Product")
                        .id(getUUID())
                        .build();
        }

        void productRecordInicietedForSuccessfulReturn() {
            productRecord = new ProductRecord(
                product.getId(), 
                product.getName(), 
                product.getCategory().getId()); 
        }

    }

    class When {

        void saveProductEntity() {
            when(repository.save(any(Product.class))).thenReturn(product);
        }

        void callProductServiceDelete() {
            service.delete(product.getId());
        }

        void deleteProductEntity() {
            doNothing().when(repository).delete(any(Product.class));
        }

        void callProductServiceUpdate() {
            service.update(productRecord);
        }

        public ProductRecord callProductServiceFindById() {
            return service.findById(product.getId());
        }

        void findProductById() {
            when(repository.findById(any(UUID.class))).thenReturn(Optional.of(product));
        }

        public ProductRecord callCreateInCategorySerivce() {
            return service.create(productRecord);
        }

        void findCategoryById() {
            when(categoryRepository.findById(any(UUID.class))).thenReturn(Optional.of(category));
        }

        void findAllProducts() {
            when(repository.findAll()).thenReturn(Collections.singletonList(product));
        }

        public List<ProductRecord> callProductServiceListAll() {
            return service.listAll();
        }

        void searchProduct() {
            when(repository.findDistinctByNameContaining(anyString(), any(PageRequest.class)))
                    .thenReturn(new PageImpl<Product>(Collections.singletonList(product)));
        }

        public Page<ProductRecord> callProductServiceSearch() {
            return service.search(product.getName(), PageRequest.of(1, 10));
        }

        void findDistinctByCategory() {
            when(repository.findDistinctByCategory(any(Category.class), any(PageRequest.class)))
                    .thenReturn(new PageImpl<Product>(Collections.singletonList(product)));
        }

        public Page<ProductRecord> callFindProductByCategory() {
            return service.findByCategory(category.getId(), PageRequest.of(1, 10));
        }
    }

    class Then {

        void shouldBeSuccessfulValidationRules(ProductRecord record) {
            assertNotNull(record);
            assertNotNull(record.name());
            assertEquals(record.name(), product.getName());
            assertEquals(record.id(), product.getId());
            assertEquals(record.idCategory(),product.getCategory().getId());
        }

        void shouldBeSuccessfulArgumentValidationUpdate() {
            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
            verify(repository).save(productCaptor.capture());
            assertNotNull(productCaptor.getValue());
            assertNotNull(productCaptor.getValue().getName());
        }

        void shouldBeSuccessfulArgumentValidationDelete() {
            ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
            verify(repository).delete(productCaptor.capture());
            assertNotNull(productCaptor.getValue());
            assertNotNull(productCaptor.getValue().getName());
        }

        void shouldBeSuccessfulValidationFindAllProducts(List<ProductRecord> records) {
            assertNotNull(records);
            assertFalse(records.isEmpty());
        }

        void shouldBeSuccessfulValidationSearchProducts(Page<ProductRecord> productPage) {
            assertNotNull(productPage);
        }

        void shouldBeSuccessfulValidationFindProductByCategory(Page<ProductRecord> productRecords) {
            assertNotNull(productRecords);
        }
    }


}
