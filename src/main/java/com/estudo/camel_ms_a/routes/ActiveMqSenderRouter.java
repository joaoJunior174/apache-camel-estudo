package com.estudo.camel_ms_a.routes;

import com.estudo.camel_ms_a.dto.People;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ActiveMqSenderRouter extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        restConfiguration().host("localhost").port(8000);

        from("timer: active-mq-timer?period=10000")
                .transform().constant("{\"name\": \"john\", \"email\":\"john.doe@outlook.com\"}")
                .log("${body}")
                .setHeader("Content-Type", constant("application/json"))
//                .to("activemq:my-active-queue");
                .to("direct:rest-endpoint")
                .to("direct:log-payload");

        from("direct:rest-endpoint")
                .to("rest:post:/rest/v1");


        from("direct:log-payload")
                .unmarshal().json(JsonLibrary.Jackson, People.class)
                .bean(MyLogProcessor.class);
    }
}

class MyLogProcessor {

    private Logger logger = LoggerFactory.getLogger(MyLogProcessor.class);

    public void printLog(People people) {
        logger.info("MyLogProcessor {}", people);
    }
}

