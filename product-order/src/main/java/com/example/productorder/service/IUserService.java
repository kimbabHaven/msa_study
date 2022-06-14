package com.example.productorder.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("product-user")
public interface IUserService {

    @RequestMapping(value = "/checkUser/{UserName}", method = RequestMethod.GET)
    boolean checkUser(@PathVariable("UserName") String userName);

}
