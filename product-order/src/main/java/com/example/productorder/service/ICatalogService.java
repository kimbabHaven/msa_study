package com.example.productorder.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("product-catalog")
public interface ICatalogService {

    @RequestMapping(value = "/checkProductQuantity", method = RequestMethod.GET)
    boolean checkProductQuantity(@PathVariable("productName") String productName, @PathVariable("count") String count);

}
