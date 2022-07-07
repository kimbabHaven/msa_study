package com.example.productcatalog.jpa;

import org.springframework.data.repository.CrudRepository;

public interface IProductCatalogRepository extends CrudRepository<ProductCatalogEntity, String> {
    public ProductCatalogEntity findByProductName(String productName);
}
