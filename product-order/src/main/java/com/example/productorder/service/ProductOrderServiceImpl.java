package com.example.productorder.service;

import com.example.productorder.jpa.ProductOrderEntity;
import com.example.productorder.dto.ProductOrderDto;
import com.example.productorder.jpa.IProductOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductOrderServiceImpl implements ProductOrderService{

    @Autowired
    private IProductOrderRepository iProductOrderRepository;

    @Override
    public String orderProduct(ProductOrderDto productOrderDto) {

        ProductOrderEntity productOrderEntity = new ProductOrderEntity();
        productOrderEntity.setOrderId(UUID.randomUUID().toString());
        productOrderEntity.setProductName(productOrderDto.getProductName());
        productOrderEntity.setCount(productOrderDto.getCount());
        productOrderEntity.setUserName(productOrderDto.getUserName());

        iProductOrderRepository.save(productOrderEntity);

        return productOrderEntity.getOrderId();
    }
}
