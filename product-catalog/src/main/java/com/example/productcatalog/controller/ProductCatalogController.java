package com.example.productcatalog.controller;

import com.example.productcatalog.dto.ProductCatalogDto;
import com.example.productcatalog.jpa.ProductCatalogEntity;
import com.example.productcatalog.service.ProductCatalogServiceImpl;
import com.example.productcatalog.vo.RequestProductCatalog;
import com.example.productcatalog.vo.ResponseProductCatalog;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
//@RequestMapping("/product-catalog")
public class ProductCatalogController {

    @Autowired
    private ProductCatalogServiceImpl productCatalogServiceImpl;

    @RequestMapping(value = "/product-catalog/checkProductCatalogPort", method = RequestMethod.GET)
    public String check(HttpServletRequest request) {
        log.info("server port = {}", request.getServerPort());
        return String.format(" product-catalog port : %S", request.getServerPort());
    }

    @HystrixCommand
    @RequestMapping(value = "/product-catalog/checkProductQuantity", method = RequestMethod.GET)
    public boolean checkProductQuantity(@RequestParam("productName") String productName, @RequestParam("count") int count) {
        return productCatalogServiceImpl.checkProductQuantity(productName, count);
    }

    @HystrixCommand(fallbackMethod = "fallbackProductNameFunction")
    @RequestMapping(value = "/product-catalog/product/{productName}", method = RequestMethod.GET)
    public ResponseEntity<ResponseProductCatalog> checkProductQuantity(@PathVariable("productName") String productName) {

        ProductCatalogDto catalogDto = productCatalogServiceImpl.getProduct(productName);

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ResponseProductCatalog responseCatalog = mapper.map(catalogDto, ResponseProductCatalog.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseCatalog);
    }

    public ResponseEntity<ResponseProductCatalog> fallbackProductNameFunction(String productName){
        ProductCatalogDto catalogDto = new ProductCatalogDto();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ResponseProductCatalog responseCatalog = mapper.map(catalogDto, ResponseProductCatalog.class);

        responseCatalog.setErrorMessage(productName + "또 에러남. 빡침~~~");
        return ResponseEntity.status(HttpStatus.OK).body(responseCatalog);
    }

    @HystrixCommand
    @RequestMapping(value = "/product-catalog/addProduct", method = RequestMethod.POST)
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
    @RequestMapping(value = "/product-catalog/updateProduct", method = RequestMethod.POST)
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

    @HystrixCommand
    @RequestMapping(value = "/product-catalog/productList", method = RequestMethod.GET)
    public ResponseEntity<List<ResponseProductCatalog>> getProductList() {

        Iterable<ProductCatalogEntity> catalogEntities = productCatalogServiceImpl.getProductAll();
        List<ResponseProductCatalog> catalogList = new ArrayList<>();
        catalogEntities.forEach(v -> {
            catalogList.add(new ModelMapper().map(v, ResponseProductCatalog.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(catalogList);
    }

}
