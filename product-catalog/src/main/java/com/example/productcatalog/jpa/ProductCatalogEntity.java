package com.example.productcatalog.jpa;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class ProductCatalogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String productId;

    private String productName; //상품명
    private int quantity; //수량
    private String productDesc; // 상품설명
    private String productImage; // 상품 이미지
}
