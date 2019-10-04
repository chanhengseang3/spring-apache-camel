package com.codisystem.camel.configuration;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ConsumerRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        from("kafka:Topic2?brokers=localhost:9092")
                .log("Message received from Kafka : ${body}");
    }


}
