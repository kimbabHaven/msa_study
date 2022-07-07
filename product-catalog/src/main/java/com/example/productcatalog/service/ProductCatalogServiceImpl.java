package com.example.productcatalog.service;

import com.example.productcatalog.dto.ProductCatalogDto;
import com.example.productcatalog.jpa.IProductCatalogRepository;
import com.example.productcatalog.jpa.ProductCatalogEntity;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductCatalogServiceImpl implements ProductCatalogService {

    @Autowired
    private IProductCatalogRepository iProductCatalogRepository;

    @Override
    public boolean checkProductQuantity(String productName, int count) {

        ProductCatalogEntity productCatalogEntity = iProductCatalogRepository.findByProductName(productName);
        if (productCatalogEntity.getQuantity() - count < 0 ) {
            return false;
        }
        return true;
    }

    @Override
    public ProductCatalogDto getProduct(String productName) {
        if (productName == null || "".equals(productName)) {
            return null;
        }

        ProductCatalogEntity productCatalogEntity = iProductCatalogRepository.findByProductName(productName);

        if (productCatalogEntity == null) {
            return null;
        }

        ProductCatalogDto catalogDto = new ModelMapper().map(productCatalogEntity, ProductCatalogDto.class);
        return catalogDto;
    }

    @Override
    public ProductCatalogDto insertProduct(ProductCatalogDto productCatalogDto) {
        productCatalogDto.setProductId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProductCatalogEntity catalogEntity = mapper.map(productCatalogDto, ProductCatalogEntity.class);

        iProductCatalogRepository.save(catalogEntity);
        ProductCatalogDto catalogDto = mapper.map(catalogEntity, ProductCatalogDto.class);

        return catalogDto;
    }

    @Override
    public ProductCatalogDto updateProduct(ProductCatalogDto productCatalogDto) {

        ProductCatalogEntity productCatalogEntity = iProductCatalogRepository.findByProductName(productCatalogDto.getProductName());

        //수량 변경
        productCatalogDto.setQuantity(productCatalogEntity.getQuantity() - productCatalogDto.getOrderQuantity());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProductCatalogEntity catalogEntity = mapper.map(productCatalogDto, ProductCatalogEntity.class);

        iProductCatalogRepository.save(catalogEntity);

        ProductCatalogDto catalogDto = mapper.map(catalogEntity, ProductCatalogDto.class);
        return catalogDto;
    }
    @Override
    public Iterable<ProductCatalogEntity> getProductAll() {
        return iProductCatalogRepository.findAll();
    }
}
