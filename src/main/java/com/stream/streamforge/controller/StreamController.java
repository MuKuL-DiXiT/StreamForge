package com.stream.streamforge.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.*;

import com.stream.streamforge.service.StreamService;

@RestController
@RequestMapping("/streams")
public class StreamController {

    private final StreamService streamService;

    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    @PostMapping("/start")
    public String start() throws IOException {
        return streamService.startStream();
    }

    @PostMapping("/stop/{id}")
    public void stop(@PathVariable String id) {
        streamService.stopStream(id);
    }

    @GetMapping("/status/{id}")
    public boolean status(@PathVariable String id) {
        return streamService.isStreamActive(id);
    }
}

