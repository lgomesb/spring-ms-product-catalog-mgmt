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

