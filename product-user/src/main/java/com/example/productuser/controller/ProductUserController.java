package com.example.productuser.controller;

import com.example.productuser.dto.ProductUserDto;
import com.example.productuser.service.ProductUserServiceImpl;
import com.example.productuser.vo.RequestProductUser;
import com.example.productuser.vo.ResponseProductUser;
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

public class ProductUserController {

    @Autowired
    private ProductUserServiceImpl productUserServiceImpl;

    @HystrixCommand
    @RequestMapping(value = "/checkUser/{UserName}", method = RequestMethod.GET)
    public boolean checkUser(@PathVariable("UserName") String userName) {

        ProductUserDto userDto = productUserServiceImpl.getUserByUserName(userName);

        if (userDto == null) {
            return false;
        } else {
            return true;
        }
    }

    @HystrixCommand
    @RequestMapping(value = "/productUser", method = RequestMethod.POST)
    public ResponseEntity<ResponseProductUser> productUser(@RequestBody RequestProductUser requestProductUser) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProductUserDto userDto = mapper.map(requestProductUser, ProductUserDto.class);
        productUserServiceImpl.createUser(userDto);

        ResponseProductUser responseProductUser = new ResponseProductUser();
        responseProductUser.setUserName(requestProductUser.getUserName());

        ResponseProductUser responseUser = mapper.map(userDto, ResponseProductUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @HystrixCommand(fallbackMethod = "fallbackFunction")
    @RequestMapping(value = "/fallbackTest", method = RequestMethod.GET)
    public String fallbackTest() throws Throwable{
        throw new Throwable("fallbackTest");
    }

    public String fallbackFunction(){
        return "fallbackFunction() 입니다.";
    }

}
