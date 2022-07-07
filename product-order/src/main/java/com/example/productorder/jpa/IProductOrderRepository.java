package com.example.productorder.jpa;

import org.springframework.data.repository.CrudRepository;

public interface IProductOrderRepository extends CrudRepository<ProductOrderEntity, String> {
}
