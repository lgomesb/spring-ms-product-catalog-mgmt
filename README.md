# Product Management Microservice
##### _v 1.0.0_
<i style="font-size:14px">  </i>


### **1. Description**

This is an application responsible for maintaining a product database. 

- #### Tecnologies and Frameworks
    |![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java$logoColor=white) | JDK 17|
    |---|---|
    |![Spring Boot](https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot) | `v 3.0.6` |


### **2. Use Case**
- Maintain the product registration in its own database, and allow registration operations, consultation, change of records.
```mermaid
flowchart LR
    USER1(("`fa:fa-user
            Use Case`"))
    CATEGORY_CREATE(["Create Category"])
    CATEGORY_UPDATE(["Update Category"])
    CATEGORY_FIND(["Find Category"])
    CATEGORY_DELETE(["Delete Category"])

    PRODUCT_CREATE(["Create Product"])
    PRODUCT_UPDATE(["Update Product"])
    PRODUCT_FIND(["Find Product"])
    PRODUCT_DELETE(["Delete Product"])

    direction RL
    USER1 -.- CATEGORY_CREATE
    USER1 -.- CATEGORY_UPDATE
    USER1 -.- CATEGORY_FIND
    USER1 -.- CATEGORY_DELETE
    direction LR
    PRODUCT_CREATE -.- USER1
    PRODUCT_UPDATE -.- USER1
    PRODUCT_FIND -.- USER1
    PRODUCT_DELETE -.- USER1
    
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
    MSProduct{{\n MS Product Mgmt \n\n}}
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

