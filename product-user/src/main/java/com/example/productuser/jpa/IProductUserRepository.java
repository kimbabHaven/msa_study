package com.example.productuser.jpa;

import org.springframework.data.repository.CrudRepository;

public interface IProductUserRepository extends CrudRepository<ProductUserEntity, String> {
    public ProductUserEntity findByUserName(String userName);
}
