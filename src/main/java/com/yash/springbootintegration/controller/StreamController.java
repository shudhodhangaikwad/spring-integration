package com.yash.springbootintegration.controller;

import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StreamController {

    private final MessageChannel apiRequestChannel;

    public StreamController(MessageChannel apiRequestChannel) {
        this.apiRequestChannel = apiRequestChannel;
    }

    @GetMapping("/start")
    public String start(){
        return "Starting Stream"+apiRequestChannel.send(MessageBuilder.withPayload("").build());
    }

}
