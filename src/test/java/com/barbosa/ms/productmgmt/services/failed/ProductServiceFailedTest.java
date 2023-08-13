package com.barbosa.ms.productmgmt.services.failed;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;

import com.barbosa.ms.productmgmt.domain.entities.Category;
import com.barbosa.ms.productmgmt.domain.entities.Product;
import com.barbosa.ms.productmgmt.domain.records.ProductRecord;
import com.barbosa.ms.productmgmt.repositories.CategoryRepository;
import com.barbosa.ms.productmgmt.repositories.ProductRepository;
import com.barbosa.ms.productmgmt.services.impl.ProductServiceImpl;

public class ProductServiceFailedTest {
    
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
    public void shouldFailWhenCreate() {
        given.categoryInicietedForFailedReturn();
        given.productInicietedForFailedReturn();
        given.productRecordInicietedForFailedReturn();
        when.findCategoryById();
        when.saveProductEntity();
        then.shouldBeFailedWhenCallCreateProduct();
    }

    @Test
    public void shouldFailWhenFindById() {
        given.categoryInicietedForFailedReturn();
        given.productInicietedForFailedReturn();
        when.findProductById();
        then.shouldBeFailedWhenCallProductFindById();
    }

    @Test
    public void shouldFailWhenUpdate() {
        given.categoryInicietedForFailedReturn();
        given.productInicietedForFailedReturn();
        given.productRecordInicietedForFailedReturn();
        when.findCategoryById();
        when.findProductById();
        then.shouldBeFailedWhenCallProductUpdate();
    }

    @Test
    public void shouldFailWhenDelete() {
        given.categoryInicietedForFailedReturn();
        given.productInicietedForFailedReturn();
        given.productRecordInicietedForFailedReturn();
        when.findProductById();
        when.deleteProductEntity();
        then.shouldBeFailedWhenCallProductDelete();
    }

    class Given {

        public UUID getUUID() {
            return UUID.randomUUID();
        }

        public void categoryInicietedForFailedReturn() {
            category = Category.builder()
                .id(getUUID())
                .name("Category-Test-Fail")
                .build();
        }

        public void productInicietedForFailedReturn() {
            product = Product.builder()
                        .category(category)
                        .name(null)
                        .id(getUUID())
                        .build();
        }

        public void productRecordInicietedForFailedReturn() {
            productRecord = new ProductRecord(
                product.getId(), 
                product.getName(), 
                product.getCategory().getId()); 
        }

    }

    class When {

        public void saveProductEntity() {
            doThrow(new DataIntegrityViolationException("Error inserting product"))
                .when(repository)
                .save(any(Product.class));
        }
        public ProductRecord callCreateInProductSerivce() {
            return service.create(productRecord);
        }

        public void callProductServiceDelete() {
            service.delete(product.getId());
        }

        public void deleteProductEntity() {
            doNothing().when(repository).delete(any(Product.class));
        }

        public void callProductServiceUpdate() {
            service.update(productRecord);
        }

        public ProductRecord callProductServiceFindById() {
            return service.findById(product.getId());
        }

        public void findProductById() {
            doThrow(new ObjectNotFoundException("Product", product.getId()))
                .when(repository)
                .findById(any(UUID.class));            
        }

        public void findCategoryById() {
            when(categoryRepository.findById(any(UUID.class))).thenReturn(Optional.of(category));
        }

    }

    class Then {

        public void shouldBeFailedWhenCallCreateProduct() {
            Assertions.assertThrows(DataIntegrityViolationException.class, () -> { 
                when.callCreateInProductSerivce(); 
            });
        }

        public void shouldBeFailedWhenCallProductFindById() {
            assertThrows(ObjectNotFoundException.class, () -> { 
                when.callProductServiceFindById();
            });
            
        }

        public void shouldBeFailedWhenCallProductUpdate() {
            assertThrows(ObjectNotFoundException.class, () -> {
                when.callProductServiceUpdate();
            });
        }

        public void shouldBeFailedWhenCallProductDelete() {
            assertThrows(ObjectNotFoundException.class, () -> {
                when.callProductServiceDelete();
            });
        }

    }


}
