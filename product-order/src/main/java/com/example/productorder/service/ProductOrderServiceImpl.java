package com.example.productorder.service;

import com.example.productorder.jpa.ProductOrderEntity;
import com.example.productorder.dto.ProductOrderDto;
import com.example.productorder.jpa.IProductOrderRepository;

public class ProductOrderServiceImpl implements ProductOrderService{

    private IProductOrderRepository iProductOrderRepository;

    @Override
    public String orderProduct(ProductOrderDto productOrderDto) {

        ProductOrderEntity productOrderEntity = new ProductOrderEntity();
        productOrderEntity.setProductName(productOrderDto.getProductName());
        productOrderEntity.setCount(productOrderDto.getCount());
        productOrderEntity.setUserName(productOrderDto.getUserName());

        iProductOrderRepository.saveProductOrder(productOrderEntity);

        return productOrderEntity.getOrderId();
    }
}
