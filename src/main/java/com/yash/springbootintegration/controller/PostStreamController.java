package com.yash.springbootintegration.controller;

import com.yash.springbootintegration.model.Post;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
public class PostStreamController {
    private final Sinks.Many<Post> sink = Sinks.many().multicast().onBackpressureBuffer();



    @GetMapping(value = "/stream-posts", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Post>> streamPosts() {
        //apiRequestChannel.send(MessageBuilder.withPayload("").build());
        return sink.asFlux()
                .map(post -> ServerSentEvent.builder(post)
                        .event("post")
                        .id(String.valueOf(post.getId()))
                        .build())
                .timeout(Duration.ofMinutes(1)); // auto-close after 1 minute
    }

    public void sendPost(Post post) {
        sink.tryEmitNext(post);
    }

    public void sendError(String message) {
        // You can emit an error event
        sink.tryEmitError(new RuntimeException(message));
    }


}
