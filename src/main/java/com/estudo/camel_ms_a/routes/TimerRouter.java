package com.estudo.camel_ms_a.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class TimerRouter extends RouteBuilder {

    @Autowired
    private GetCurrentTimeBean getCurrentTimeBean;

    @Override
    public void configure() throws Exception {
        from("timer: first-timer")
//                .transform().constant("My constante message")
                .bean(getCurrentTimeBean)
                .process(new SimpleLogginProcess())
                .to("log: first-timer");
    }
}

@Component
class GetCurrentTimeBean {
    public String getCuttentTime() {
        return "Time noww is: " + LocalDateTime.now();
    }
}

    class SimpleLogginProcessComponent {
    private Logger logger = LoggerFactory.getLogger(SimpleLogginProcessComponent.class);

    public void process(String message) {
        logger.info("SimpleLogginProcessComponent {}", message);
    }
}

class SimpleLogginProcess implements Processor {
    private Logger logger = LoggerFactory.getLogger(SimpleLogginProcessComponent.class);

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info("SimpleLogginProcess {}", exchange.getMessage().getBody());
    }
}
