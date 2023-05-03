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
- Manter o cadastro de produtos em uma base de dados própria, e possibilite as operações de cadastro, consulta, alteração de registros. 

```mermaid
mindmap
root((Product Mgmt))
    Create
    Read
    Update
    Delete
```


### **3. Entity Relationship**
```mermaid
---
title: Product Catalog Management
---
erDiagram
    PRODUCT ||--|| CATEGORY : contains
    PRODUCT {
        uuid id
        string name
        boolean status
        datetime created_on
        string created_by
        datetime modified_on
        string created_by
    }
    CATEGORY {
        uuid id
        string name
        boolean status
        datetime created_on
        string created_by
        datetime modified_on
        string created_by
    }