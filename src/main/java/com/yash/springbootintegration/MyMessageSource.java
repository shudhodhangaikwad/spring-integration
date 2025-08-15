package com.yash.springbootintegration;


import com.yash.springbootintegration.model.Data;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;

public class MyMessageSource implements MessageSource<Data> {
    @Override
    public Message<Data> receive() {
        double random = Math.random();

        Map<String, Object> header = new HashMap<>();
        header.put("random", random);
        header.put("name", "Sid");
        header.put("demoKey", "Demo Value");
        Data data = new Data();
        data.setMessage("Hello World: " + random);
        data.setId(random);
        return MessageBuilder
                .withPayload(data)
                .copyHeaders(header)
                .build();
    }
}
