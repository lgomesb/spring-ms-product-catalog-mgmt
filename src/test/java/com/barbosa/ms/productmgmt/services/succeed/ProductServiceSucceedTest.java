package com.barbosa.ms.productmgmt.services.succeed;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.entities.Product;
import com.barbosa.ms.productmgmt.domain.records.ProductRecord;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.repositories.ProductRepository;
import com.barbosa.ms.productmgmt.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceSucceedTest {
    
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



    @Test
    void shouldSuccessWhenCreate() {
        given.categoryInitiatedForSuccessfulReturn();
        given.productInitiatedForSuccessfulReturn();
        given.productRecordInitiatedForSuccessfulReturn();
        when.findCategoryById();
        when.saveProductEntity();
        ProductRecord recordResult = when.callCreateInCategoryService();
        then.shouldBeSuccessfulValidationRules(recordResult);
    }

    @Test
    void shouldSuccessWhenFindById() {
        given.categoryInitiatedForSuccessfulReturn();
        given.productInitiatedForSuccessfulReturn();
        when.findProductById();
        ProductRecord recordResult = when.callProductServiceFindById();
        then.shouldBeSuccessfulValidationRules(recordResult);
    }

    @Test
    void shouldSuccessWhenUpdate() {
        given.categoryInitiatedForSuccessfulReturn();
        given.productInitiatedForSuccessfulReturn();
        given.productRecordInitiatedForSuccessfulReturn();
        when.findCategoryById();
        when.findProductById();
        when.saveProductEntity();
        when.callProductServiceUpdate();
        then.shouldBeSuccessfulArgumentValidationUpdate();
    }

    @Test
    void shouldSuccessWhenDelete() {
        given.categoryInitiatedForSuccessfulReturn();
        given.productInitiatedForSuccessfulReturn();
        given.productRecordInitiatedForSuccessfulReturn();
        when.findProductById();
        when.deleteProductEntity();
        when.callProductServiceDelete();
        then.shouldBeSuccessfulArgumentValidationDelete();
    }

    @Test
    void shouldSuccessWhenListAll() {
        given.categoryInitiatedForSuccessfulReturn();
        given.productInitiatedForSuccessfulReturn();
        when.findAllProducts();
        List<ProductRecord> records = when.callProductServiceListAll();
        then.shouldBeSuccessfulValidationFindAllProducts(records);
    }

    @Test
    void shouldSuccessWhenSearchProduct() {
        given.categoryInitiatedForSuccessfulReturn();
        given.productInitiatedForSuccessfulReturn();
        when.searchProduct();
        Page<ProductRecord> productRecords = when.callProductServiceSearch();
        then.shouldBeSuccessfulValidationSearchProducts(productRecords);
    }

    @Test
    void shouldSuccessWhenFindProductByCategory() {
        given.categoryInitiatedForSuccessfulReturn();
        given.productInitiatedForSuccessfulReturn();
        when.findCategoryById();
        when.findDistinctByCategory();
        Page<ProductRecord> productRecords = when.callFindProductByCategory();
        then.shouldBeSuccessfulValidationFindProductByCategory(productRecords);

    }

    class Given {

        public UUID getUUID() {
            return UUID.randomUUID();
        }

        void categoryInitiatedForSuccessfulReturn() {
            category = Category.builder()
                .id(getUUID())
                .name("Category-Test-Success")
                .build();
        }

        void productInitiatedForSuccessfulReturn() {
            product = Product.builder()
                        .category(category)
                        .name("Test-Product")
                        .id(getUUID())
                        .build();
        }

        void productRecordInitiatedForSuccessfulReturn() {
            productRecord = ProductRecord.fromEntity(product);
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

        public ProductRecord callCreateInCategoryService() {
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
                    .thenReturn(new PageImpl<>(Collections.singletonList(product)));
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

        void shouldBeSuccessfulValidationRules(ProductRecord productRecord) {
            assertNotNull(productRecord);
            assertNotNull(productRecord.name());
            assertEquals(productRecord.name(), product.getName());
            assertEquals(productRecord.id(), product.getId());
            assertEquals(productRecord.category().id(),product.getCategory().getId());
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
