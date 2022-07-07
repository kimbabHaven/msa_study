package com.example.productcatalog.vo;

import lombok.Data;

@Data
public class ResponseProductCatalog {
    private String productId;
    private String productName; //상품명
    private int quantity; //수량
    private String productDesc; // 상품설명
    private String productImage; // 상품 이미지

    private String errorMessage; //에러메시지
}
