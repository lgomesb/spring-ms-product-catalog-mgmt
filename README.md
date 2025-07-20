# Product Catalog Management
##### _v 1.0.0_
<i style="font-size:14px">  </i>


### **1. Description**

This is an application responsible for maintaining a product database. 

- #### Tecnologies and Frameworks
    |![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java$logoColor=white) | JDK 17|
    |---|---|
    |![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot) | `v 3.0.6` |
    | ![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge) | latest      |
    | ![H2 Database](https://img.shields.io/badge/H2_Database-09476B?style=for-the-badge&logo=h2database) | latest      |
    | ![Flyway](https://img.shields.io/badge/Flyway-CC0200?style=for-the-badge&logo=flyway)             | latest      |
    | ![Lombok](https://img.shields.io/badge/Lombok-B6A272?style=for-the-badge&logo=lombok)             | latest      |
    | ModelMapper        | 3.0.0       |
    | ![OpenAPI](https://img.shields.io/badge/OpenAPI-000?style=for-the-badge&logo=openapiinitiative) | 2.1.0       |


### **2. Use Case**
- Maintain the product registration in its own database, and allow registration operations, consultation, change of records.
```mermaid
flowchart LR

    subgraph body [" "]
        subgraph user_category["CATEGORY"]
            direction LR
            USER1(("USE CASE"))
            subgraph Category[" "]
                CATEGORY_CREATE(["Create"])
                CATEGORY_FIND(["Find by Id"])
                CATEGORY_UPDATE(["Update"])
                CATEGORY_DELETE(["Delete"])
                CATEGORY_SEARCH(["Search by Name"])
            end
        end

        subgraph user_product["PRODUCT"]
            direction LR
            USER2(("USE CASE"))
            subgraph product[" "]
                PRODUCT_CREATE(["Create"])
                PRODUCT_FIND(["Find by Id"])
                PRODUCT_UPDATE(["Update"])
                PRODUCT_DELETE(["Delete"])
                PRODUCT_SEARCH(["Search by Name"])
            end
        end
   
    end

USER1 --> CATEGORY_CREATE
USER1 --> CATEGORY_UPDATE
USER1 --> CATEGORY_FIND
USER1 --> CATEGORY_DELETE
USER1 --> CATEGORY_SEARCH

USER2 --- PRODUCT_CREATE
USER2 --- PRODUCT_UPDATE
USER2 --- PRODUCT_FIND
USER2 --- PRODUCT_DELETE
USER2 --- PRODUCT_SEARCH


```

#### 2.1 Compoments

```mermaid
flowchart LR
    subgraph Domain
        MSProduct --> DB
    end
    subgraph ER
        Product --> Category
    end
    MSProduct{{MS Product Mgmt}}
    DB[(Database)]
    DB -.-> ER
    
    
```
#### 2.2 Entity Relationship

```mermaid
---
title: ER - Product Catalog Management
---
erDiagram
    CATEGORY ||--|{ PRODUCT_CATEGORY : contains
    PRODUCT  ||--|{ PRODUCT_CATEGORY : contains


    CATEGORY {
        uuid id PK
        string(100) name UK ""
        date created_on
        varchar2 created_by
        date update_on
    }
    PRODUCT {
        uuid id PK
        string(100) name UK
        date created_on
        varchar2 created_by
        date update_on
    }
    PRODUCT_CATEGORY {
        uuid product_id
        uuid category_id
    }
    
```
[Mermaid](https://mermaid.live/edit#pako:eNrNUsFqwzAM_RXhc33Z0beRlh3GaMjaw4YhaLaXmiZycO1BSPLvc5q2W8sGO042yNLTk2VLPVNOGyYY51xSsKE2AlYFcMi901EFyDBg7Sp4QsLKNIaCpGOw8UuLlcdGEiTJ7jerh3XxAsPA-dBDXqyX22xTXvwClKOAlg4z4RQAfyJM6-qWfrYnidFqSDt__PJ9oFc79HdA2BjYfkM0BgPKm6R06egHyhl8625YsZ3UhTRev-PfVVT--lnt3NvS6htApWyV890FSRnZgjXGN2h1GpNjJsnCLk2CZCIdNfq9ZJLGFIcxuOeOFBPBR7Ngc3mnMWHiHetD8rZIr86d7fETT8jFAg)


### **3. Class Diagram**
- Below is an example model of the organization of microservice classes. 


```mermaid

classDiagram

    class Category {
        - UUID id
        - String name
        - Boolean status
        - Datetime createdOn
        - String createBy
        - Datetime modifiedOn
        - String modifiedBy
        + Category build()
    }

    class Product {
        - UUID id
        - String name
        - Boolean status
        - Datetime createdOn
        - String createBy
        - Datetime modifiedOn
        - String modifiedBy
        - Category category
        + Product build()
    }    

    class CategoryRepository {
        <<inteface>>

    }

    class ProductRepository {
        <<inteface>>
    }

    class JpaRepositories {
        <<inteface>>
        + Object save()
        + Object delete()
        + Object findById()
        + Object findAll()
    }
        
    class ProductController {
        - ProductService service
        + ResponseEntity createProduct(productDTO)
        + ResponseEntity updateProduct(idProduct, productDTO)
        + ResponseEntity deleteProduct(idProduct)
        + ResponseEntity findProduct()
    }

    class CategoryController {
        - CategoryService categoryService
        + ResponseEntity createCategory(categoryDTO)
        + ResponseEntity updateCategory(idCategory, categoryDTO)
        + ResponseEntity deleteCategory(idCategory)
        + ResponseEntity findCategory()
        }

    

    class CategoryService
    class ProductService
    Product o--> Category
    CategoryRepository -- Category
    ProductRepository  -- Product
    CategoryService -- CategoryRepository
    ProductService  -- ProductRepository
    JpaRepositories <|-- ProductRepository
    JpaRepositories <|-- CategoryRepository
    ProductController o-- ProductService
    CategoryController o-- CategoryService

```


## 4. How to Run

### Development Environment

```sh
./mvnw spring-boot:run
```

The service will be available at: [http://localhost:8080/product-order](http://localhost:8080/product-order)

### Product Environment (Docker)

```sh
docker-compose up --build
```

The service will be available at: [http://localhost:9090/product-order](http://localhost:9090/product-order)

## 5. Main Endpoints

- `POST   /`           - Create product
- `GET    /{id}`       - Find product by ID
- `PUT    /{id}`       - Update product
- `PATCH  /{id}`       - Change product status
- `DELETE /{id}`       - Delete product
- `GET    /`           - List all products

## 6. Tests

To run automated tests:

```sh
./mvnw test
```

## 7. OpenAPI Documentation

Acess the documentation at:  
[http://localhost:8080/product-order/swagger-ui.html](http://localhost:8080/product-order/swagger-ui.html)  
or
[http://localhost:9090/product-order/swagger-ui.html](http://localhost:9090/product-order/swagger-ui.html) (produção)

---

> Projetc developed by Leandro Barbosa.
    
```

