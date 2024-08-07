package com.barbosa.ms.productmgmt.services.failed;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.entities.Product;
import com.barbosa.ms.productmgmt.domain.records.ProductRecord;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.repositories.ProductRepository;
import com.barbosa.ms.productmgmt.services.impl.ProductServiceImpl;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceFailedTest {
    
    @InjectMocks
    private ProductServiceImpl service;
    
    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    private Given given = new Given();
    private When when = new When();
    private Then then = new Then();
    private Product product;
    private Category category;
    private ProductRecord productRecord;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFailWhenCreate() {
        given.categoryInitiatedForFailedReturn();
        given.productInitiatedForFailedReturn();
        given.productRecordInitiatedForFailedReturn();
        when.findCategoryById();
        when.saveProductEntity();
        then.shouldBeFailedWhenCallCreateProduct();
    }

    @Test
    void shouldFailWhenFindById() {
        given.categoryInitiatedForFailedReturn();
        given.productInitiatedForFailedReturn();
        when.findProductById();
        then.shouldBeFailedWhenCallProductFindById();
    }

    @Test
    void shouldFailWhenUpdate() {
        given.categoryInitiatedForFailedReturn();
        given.productInitiatedForFailedReturn();
        given.productRecordInitiatedForFailedReturn();
        when.findCategoryById();
        when.findProductById();
        then.shouldBeFailedWhenCallProductUpdate();
    }

    @Test
    void shouldFailWhenDelete() {
        given.categoryInitiatedForFailedReturn();
        given.productInitiatedForFailedReturn();
        given.productRecordInitiatedForFailedReturn();
        when.findProductById();
        when.deleteProductEntity();
        then.shouldBeFailedWhenCallProductDelete();
    }

    class Given {

        public UUID getUUID() {
            return UUID.randomUUID();
        }

        void categoryInitiatedForFailedReturn() {
            category = Category.builder()
                .id(getUUID())
                .name("Category-Test-Fail")
                .build();
        }

        void productInitiatedForFailedReturn() {
            product = Product.builder()
                        .category(category)
                        .name(null)
                        .id(getUUID())
                        .build();
        }

        void productRecordInitiatedForFailedReturn() {
            productRecord = ProductRecord.fromEntity(product);
        }

    }

    class When {

        void saveProductEntity() {
            doThrow(new DataIntegrityViolationException("Error inserting product"))
                .when(repository)
                .save(any(Product.class));
        }
        public ProductRecord callCreateInProductSerivce() {
            return service.create(productRecord);
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
            doThrow(new ObjectNotFoundException("Product", product.getId()))
                .when(repository)
                .findById(any(UUID.class));            
        }

        void findCategoryById() {
            when(categoryRepository.findById(any(UUID.class))).thenReturn(Optional.of(category));
        }

    }

    class Then {

        void shouldBeFailedWhenCallCreateProduct() {
            Assertions.assertThrows(DataIntegrityViolationException.class, () -> { 
                when.callCreateInProductSerivce(); 
            });
        }

        void shouldBeFailedWhenCallProductFindById() {
            assertThrows(ObjectNotFoundException.class, () -> { 
                when.callProductServiceFindById();
            });
            
        }

        void shouldBeFailedWhenCallProductUpdate() {
            assertThrows(ObjectNotFoundException.class, () -> {
                when.callProductServiceUpdate();
            });
        }

        void shouldBeFailedWhenCallProductDelete() {
            assertThrows(ObjectNotFoundException.class, () -> {
                when.callProductServiceDelete();
            });
        }

    }


}
