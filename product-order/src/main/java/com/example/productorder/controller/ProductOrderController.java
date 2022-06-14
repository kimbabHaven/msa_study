package com.example.productorder.controller;

import com.example.productorder.dto.ProductOrderDto;
import com.example.productorder.messagequeue.KafkaProducer;
import com.example.productorder.service.ICatalogService;
import com.example.productorder.service.IUserService;
import com.example.productorder.service.ProductOrderServiceImpl;
import com.example.productorder.vo.RequestProductOrder;
import com.example.productorder.vo.ResponseProductOrder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class ProductOrderController {

    @Autowired
    private ProductOrderServiceImpl productOrderServiceImpl;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICatalogService iCatalogService;

    @HystrixCommand
    @RequestMapping(value = "/orderProduct", method = RequestMethod.POST)
    public ResponseEntity<ResponseProductOrder> orderProduct(@RequestBody RequestProductOrder requestProductOrder) {

        //멤버인지 확인
        if(iUserService.checkUser(requestProductOrder.getUserName())) {
            System.out.println(requestProductOrder.getUserName() + "은 가입되어 있는 회원");
        }

        //주문할 수 있는 상품인지 확인
        if(!iCatalogService.checkProductQuantity(requestProductOrder.getProductName(), requestProductOrder.getCount())) {
            System.out.println(requestProductOrder.getProductName() + "은 주문할 수 없는 상품");
            return null;
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProductOrderDto orderDto = mapper.map(requestProductOrder, ProductOrderDto.class);

        //상품 주문
        productOrderServiceImpl.orderProduct(orderDto);

        //kafka
        kafkaProducer.send("hoony-kafka-test", requestProductOrder);

        ResponseProductOrder responseProductOrder = new ResponseProductOrder();
        responseProductOrder.setProductName(requestProductOrder.getProductName());
        ResponseProductOrder responseOrder = mapper.map(orderDto, ResponseProductOrder.class);

        return new ResponseEntity<ResponseProductOrder>(responseOrder, HttpStatus.OK);
    }
}
