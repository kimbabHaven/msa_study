package com.example.productcatalog.messagequeue;

import com.example.productcatalog.jpa.IProductCatalogRepository;
import com.example.productcatalog.jpa.ProductCatalogEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KafkaConsumer {
    IProductCatalogRepository repository;

    @Autowired
    public KafkaConsumer(IProductCatalogRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "kafka-test")
    public void update(String kafkaMessage){
        log.info("Kafka Message: -> " + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        try{
            //stirng으로 들어오는 message를 json 타입으로 변환하는 코드
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        }catch (JsonProcessingException ex){
            ex.printStackTrace();
        }

        ProductCatalogEntity productCatalogEntity = repository.findByProductName((String)map.get("productName"));
        if(productCatalogEntity!=null){
            productCatalogEntity.setQuantity(productCatalogEntity.getQuantity() - (Integer)map.get("count"));
            repository.save(productCatalogEntity);
        }
    }
}
