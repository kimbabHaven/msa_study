package com.example.productorder.jpa;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product-order")
public class ProductOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String orderId;

    private String productName; //상품이름
    private String count; //주문개수
    private String userName;//회원명
}
