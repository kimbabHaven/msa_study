package com.example.productorder.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("product-catalog")
public interface ICatalogService {

    @RequestMapping(value = "/product-catalog/checkProductQuantity", method = RequestMethod.GET)
    boolean checkProductQuantity(@RequestParam("productName") String productName, @RequestParam("count") String count);

}
