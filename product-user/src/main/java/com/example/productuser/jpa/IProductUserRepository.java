package com.example.productuser.jpa;

public interface IProductUserRepository {

    public ProductUserEntity findByUserName(String userName);
    public int createUserTable(ProductUserEntity productUserEntity);
    public int insertUser(ProductUserEntity productUserEntity);

}
