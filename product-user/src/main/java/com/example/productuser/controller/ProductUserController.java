package com.example.productuser.controller;

import com.example.productuser.dto.ProductUserDto;
import com.example.productuser.jpa.ProductUserEntity;
import com.example.productuser.service.ProductUserServiceImpl;
import com.example.productuser.vo.RequestProductUser;
import com.example.productuser.vo.ResponseProductUser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
public class ProductUserController {

    @Autowired
    private ProductUserServiceImpl productUserServiceImpl;

    @HystrixCommand
    @RequestMapping(value = "/product-user/main", method = RequestMethod.GET)
    public String main(HttpServletRequest request, Model model) {
        log.debug(" ### session ID : {} ========", request.getSession().getId());

        //TODO
        //로그인 정보가 남아 있는 경우
        if (productUserServiceImpl == null) {
            return "main";
        }
        return "login";
    }

    @HystrixCommand
    @RequestMapping(value = "/product-user/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @HystrixCommand
    @RequestMapping(value = "/product-user/checkUser/{UserName}", method = RequestMethod.GET)
    public boolean checkUser(@PathVariable("UserName") String userName) {

        ProductUserDto userDto = productUserServiceImpl.getUserByUserName(userName);

        if (userDto == null) {
            return false;
        } else {
            return true;
        }
    }

    @HystrixCommand
    @RequestMapping(value = "/product-user/productUser", method = RequestMethod.POST)
    public ResponseEntity<ResponseProductUser> productUser(@RequestBody RequestProductUser requestProductUser) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProductUserDto userDto = mapper.map(requestProductUser, ProductUserDto.class);
        productUserServiceImpl.insertUser(userDto);

        ResponseProductUser responseProductUser = new ResponseProductUser();
        responseProductUser.setUserName(requestProductUser.getUserName());

        ResponseProductUser responseUser = mapper.map(userDto, ResponseProductUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }

    @HystrixCommand
    @RequestMapping(value = "/product-user/userList", method = RequestMethod.GET)
    public ResponseEntity<List<ResponseProductUser>> getProductList() {

        Iterable<ProductUserEntity> userEntities = productUserServiceImpl.getUserAll();
        List<ResponseProductUser> userList = new ArrayList<>();
        userEntities.forEach(v -> {
            userList.add(new ModelMapper().map(v, ResponseProductUser.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(userList);
    }

    /**
     * ADMIN 권한 소유자만 PUT METHOD API 호출 가능하도록 설정 후 테스트
     */
    @RequestMapping(value = "/product-user/authName/{name}", method = RequestMethod.PUT)
    public String member(@PathVariable("name") String name) {
        return "[MEMBER-TEST] " + name + " TEST MAPPING.";
    }

    @HystrixCommand(fallbackMethod = "fallbackFunction")
    @RequestMapping(value = "/product-user/fallbackTest", method = RequestMethod.GET)
    public String fallbackTest() throws Throwable{
        throw new Throwable("fallbackTest");
    }

    public String fallbackFunction(){
        return "fallbackFunction() 입니다.";
    }

}
