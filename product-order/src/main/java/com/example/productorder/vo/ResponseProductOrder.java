package com.example.productorder.vo;

import lombok.Data;

@Data
public class ResponseProductOrder {
    private String orderId;
    private String productName; //상품이름
    private String count; //주문개수
    private String userName;//회원명
}
