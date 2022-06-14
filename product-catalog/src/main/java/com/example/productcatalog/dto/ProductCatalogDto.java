package com.example.productcatalog.dto;

import lombok.Data;

@Data
public class ProductCatalogDto {
    private String productId;
    private String productName; //상품명
    private int quantity; //수량
    private String productDesc; // 상품설명
    private String productImage; // 상품 이미지

    private int orderQuantity; //주문수량
}
