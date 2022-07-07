package com.example.productcatalog.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestProductCatalog {
    private String productId;

    @NotNull(message = "productName은 필수 입력값입니다.")
    private String productName; //상품명

    @NotNull(message = "quantity은 필수 입력값입니다.")
    private int quantity; //수량

    private String productDesc; // 상품설명
    private String productImage; // 상품 이미지
}
