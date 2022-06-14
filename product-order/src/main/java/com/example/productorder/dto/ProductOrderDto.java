package com.example.productorder.dto;

import lombok.Data;

@Data
public class ProductOrderDto {
    private String orderId;
    private String productName; //상품이름
    private String count; //주문개수
    private String userName;//회원명
}
