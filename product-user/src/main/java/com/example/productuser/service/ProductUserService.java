package com.example.productuser.service;

import com.example.productuser.dto.ProductUserDto;

public interface ProductUserService {

    public ProductUserDto getUserByUserName(String userName);
    public ProductUserDto createUser(ProductUserDto productUserDto);
//    public int createUserTable(ProductUserDto productUserDto);
//    public int insertUser(ProductUserDto productUserDto);

}
