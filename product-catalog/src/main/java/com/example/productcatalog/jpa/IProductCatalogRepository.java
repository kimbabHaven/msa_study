package com.example.productcatalog.jpa;

public interface IProductCatalogRepository {

    public ProductCatalogEntity findByProductName(String productName);
    public int insertProduct(ProductCatalogEntity productCatalogEntity);
    public int updateProduct(ProductCatalogEntity productCatalogEntity);

}
