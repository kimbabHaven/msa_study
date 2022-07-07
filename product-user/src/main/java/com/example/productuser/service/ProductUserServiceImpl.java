package com.example.productuser.service;

import com.example.productuser.dto.ProductUserDto;
import com.example.productuser.jpa.IProductUserRepository;
import com.example.productuser.jpa.ProductUserEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductUserServiceImpl implements ProductUserService {

    @Autowired
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
    public ProductUserDto insertUser(ProductUserDto productUserDto) {
        productUserDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProductUserEntity userEntity = mapper.map(productUserDto, ProductUserEntity.class);

        iProductUserRepository.save(userEntity);

        ProductUserDto userDto = mapper.map(userEntity, ProductUserDto.class);

        return userDto;
    }

    @Override
    public Iterable<ProductUserEntity> getUserAll() {
        return iProductUserRepository.findAll();
    }
}
