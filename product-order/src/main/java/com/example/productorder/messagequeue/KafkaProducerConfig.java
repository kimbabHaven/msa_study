package com.example.productorder.messagequeue;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaProducerConfig {

    private final static String BOOTSTRAP_SERVERS = "127.0.0.1:9092"; //전송하고자 하는 카프카 클러스터 서버의 host와 IP

    //접속할 수 있는 Kafka 정보가 들어 있는 빈
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        // 메시지 키, 메시지 값을 직렬화 하기 위한 직렬화 클래스 선언. String 객체를 전송하기 위해 String을 직렬화하는 클래스인 카프카 라이브러리 StringSerializer를 사용
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        //properties를 생성 파라미터로 추가해 인스턴스를 생성. 이 인스턴스는 ProducerRecord를 전송할 떄 사용
        return new DefaultKafkaProducerFactory<>(properties);
    }

    //데이터를 전달하는 용도의 인스턴스
    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
