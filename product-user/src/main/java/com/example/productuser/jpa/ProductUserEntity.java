package com.example.productuser.jpa;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "product_user")
public class ProductUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String userId;

    private String userName; //회원이름
    private String email; //회원 이메일
    private String password; //회원 비밀번호
}
