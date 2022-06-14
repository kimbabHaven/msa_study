package com.example.productuser.vo;

import lombok.Data;

@Data
public class ResponseProductUser {
    private String userId;
    private String userName; //회원이름
    private String email; //회원 이메일
    private String password; // 비밀번호
}
