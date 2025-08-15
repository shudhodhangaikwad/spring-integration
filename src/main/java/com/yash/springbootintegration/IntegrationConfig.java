package com.yash.springbootintegration;

import com.yash.springbootintegration.controller.PostStreamController;
import com.yash.springbootintegration.model.Post;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

import java.util.Arrays;

@Configuration
public class IntegrationConfig {

//    @Bean
//    public CommandLineRunner runner(MessageChannel apiRequestChannel) {
//        return args -> apiRequestChannel.send(MessageBuilder.withPayload("").build());
//    }

    @Bean
    public MessageChannel apiRequestChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel singleChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageChannel fullChannel() {
        return new DirectChannel();
    }

    // Flow 1: Fetch all posts and split into individual messages
    @Bean
    public StandardIntegrationFlow apiFlow() {
        return IntegrationFlow
                .from("apiRequestChannel")
                .handle(Http.outboundGateway("https://jsonplaceholder.typicode.com/posts")
                        .httpMethod(HttpMethod.GET)
                        .expectedResponseType(Post[].class))
                .transform(Post[].class, Arrays::asList)
                .split()
                .channel(singleChannel())
                .get();
    }

    // Flow 2: For each Post, fetch full details by ID
    @Bean
    public IntegrationFlow singlePost() {
        return IntegrationFlow
                .from(singleChannel())
                .handle(Http.outboundGateway(m ->
                                "https://jsonplaceholder.typicode.com/posts/" + ((Post) m.getPayload()).getId())
                        .httpMethod(HttpMethod.GET)
                        .expectedResponseType(Post.class))
                .channel(fullChannel())
                .get();
    }

    // Flow 3: Process final full Post objects
    @Bean
    public IntegrationFlow fullPost(PostStreamController controller) {
        return IntegrationFlow
                .from(fullChannel())
                .handle(m ->  controller.sendPost((Post) m.getPayload()))
                .get();
    }
}
