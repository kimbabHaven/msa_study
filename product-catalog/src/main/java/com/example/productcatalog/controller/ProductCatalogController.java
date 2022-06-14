package com.example.productcatalog.controller;

import com.example.productcatalog.dto.ProductCatalogDto;
import com.example.productcatalog.service.ProductCatalogServiceImpl;
import com.example.productcatalog.vo.RequestProductCatalog;
import com.example.productcatalog.vo.ResponseProductCatalog;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class ProductCatalogController {

    @Autowired
    private ProductCatalogServiceImpl productCatalogServiceImpl;

    @HystrixCommand
    @RequestMapping(value = "/checkProductQuantity", method = RequestMethod.GET)
    public boolean checkProductQuantity(@PathVariable("productName") String productName, @PathVariable("count") int count) {
        return productCatalogServiceImpl.checkProductQuantity(productName, count);
    }

    @HystrixCommand
    @RequestMapping(value = "/product/{productName}", method = RequestMethod.GET)
    public ResponseEntity<ResponseProductCatalog> checkProductQuantity(@PathVariable("productName") String productName) {

        ProductCatalogDto catalogDto = productCatalogServiceImpl.getProduct(productName);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ResponseProductCatalog responseCatalog = mapper.map(catalogDto, ResponseProductCatalog.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseCatalog);
    }

    @HystrixCommand
    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public ResponseEntity<ResponseProductCatalog> addProduct(@RequestBody RequestProductCatalog requestProductCatalog) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProductCatalogDto catalogDto = mapper.map(requestProductCatalog, ProductCatalogDto.class);
        productCatalogServiceImpl.insertProduct(catalogDto);

        ResponseProductCatalog responseProductCatalog = new ResponseProductCatalog();
        responseProductCatalog.setProductName(requestProductCatalog.getProductName());

        ResponseProductCatalog responseCatalog = mapper.map(catalogDto, ResponseProductCatalog.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseCatalog);
    }

    @HystrixCommand
    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    public ResponseEntity<ResponseProductCatalog> updateProduct(@RequestBody RequestProductCatalog requestProductCatalog) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProductCatalogDto catalogDto = mapper.map(requestProductCatalog, ProductCatalogDto.class);
        productCatalogServiceImpl.updateProduct(catalogDto);

        ResponseProductCatalog responseProductCatalog = new ResponseProductCatalog();
        responseProductCatalog.setProductName(requestProductCatalog.getProductName());

        ResponseProductCatalog responseCatalog = mapper.map(catalogDto, ResponseProductCatalog.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseCatalog);
    }

}
