package com.example.productuser.service;

import com.example.productuser.dto.ProductUserDto;
import com.example.productuser.jpa.ProductUserEntity;

public interface ProductUserService {
    public ProductUserDto getUserByUserName(String userName);
    public ProductUserDto insertUser(ProductUserDto productUserDto);
    public Iterable<ProductUserEntity> getUserAll();
}
