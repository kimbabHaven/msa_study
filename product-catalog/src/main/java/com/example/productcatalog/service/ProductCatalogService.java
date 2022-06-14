package com.example.productcatalog.service;

import com.example.productcatalog.dto.ProductCatalogDto;

public interface ProductCatalogService {

    public boolean checkProductQuantity(String productName, int count);
    public ProductCatalogDto getProduct(String productName);
    public ProductCatalogDto insertProduct(ProductCatalogDto catalogDto);
    public ProductCatalogDto updateProduct(ProductCatalogDto catalogDto);

}
