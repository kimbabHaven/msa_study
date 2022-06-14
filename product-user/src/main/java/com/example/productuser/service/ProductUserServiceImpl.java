package com.example.productuser.service;

import com.example.productuser.dto.ProductUserDto;
import com.example.productuser.jpa.IProductUserRepository;
import com.example.productuser.jpa.ProductUserEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.UUID;

public class ProductUserServiceImpl implements ProductUserService {

    private IProductUserRepository iProductUserRepository;

    @Override
    public ProductUserDto getUserByUserName(String userName) {

        ProductUserEntity productUserEntity = iProductUserRepository.findByUserName(userName);

        if (productUserEntity == null) {
            return null;
        }

        ProductUserDto userDto = new ModelMapper().map(productUserEntity, ProductUserDto.class);
        return userDto;
    }
    @Override
    public ProductUserDto createUser(ProductUserDto productUserDto) {
        productUserDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProductUserEntity userEntity = mapper.map(productUserDto, ProductUserEntity.class);

        iProductUserRepository.insertUser(userEntity);

        ProductUserDto userDto = mapper.map(userEntity, ProductUserDto.class);

        return userDto;
    }

//    @Override
//    public String selectUserName(ProductUserDto productOrderDto) {
//
//        ProductUserEntity productUserEntity = new ProductUserEntity();
//        productUserEntity.setUserName(productOrderDto.getUserName());
//        productUserEntity.setProductName(productOrderDto.getProductName());
//        productUserEntity.setCount(productOrderDto.getCount());
//        productUserEntity.setUserName(productOrderDto.getUserName());
//
//        iProductUserRepository.saveProductOrder(productUserEntity);
//
//        return productUserEntity.getOrderId();
//    }
}
