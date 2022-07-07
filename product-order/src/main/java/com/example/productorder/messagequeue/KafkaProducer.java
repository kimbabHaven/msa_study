package com.example.productorder.messagequeue;

import com.example.productorder.vo.RequestProductOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public RequestProductOrder send(String topic, RequestProductOrder requestProductOrder) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = "";
        try {
            //requestProductOrder 인스턴스 값은 json 포맷으로 변경
            jsonInString = mapper.writeValueAsString(requestProductOrder);
        } catch(JsonProcessingException ex) {
            ex.printStackTrace();
        }

        //send메소드를 호출하면 ProduceRecord를 생성해 프로듀서 내부에 가지고 있다가 배치 형태로 묶어서 브로커에 전송
        kafkaTemplate.send(topic, jsonInString);
        log.info("Kafka Producer sent data from product-order microservice: " + requestProductOrder);

        return requestProductOrder;
    }
}
