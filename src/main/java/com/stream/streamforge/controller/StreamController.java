package com.stream.streamforge.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.stream.streamforge.service.JwtService;
import com.stream.streamforge.service.StreamKeyService;
import com.stream.streamforge.service.StreamService;

@RestController
@RequestMapping("/stream")
public class StreamController {

    private final StreamService streamService;
    private final StreamKeyService streamKeyService;
    private final JwtService jwtService;

    public StreamController(StreamService streamService, StreamKeyService streamKeyService, JwtService jwtService) {
        this.streamService = streamService;
        this.streamKeyService = streamKeyService;
        this.jwtService = jwtService;
    }

    @PostMapping("/start")
    public ResponseEntity<?> startStream(
            @RequestHeader("Authorization") String authHeader) throws IOException, InterruptedException {

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        String streamKey = streamKeyService.getStreamKey(email);

        streamService.startEncoding(streamKey);

        return ResponseEntity.ok("Stream started");
    }

    @PostMapping("/stop/{id}")
    public ResponseEntity<?> stopStream(
            @RequestHeader("Authorization") String authHeader) throws IOException {

        String token = authHeader.substring(7);
        String email = jwtService.extractEmail(token);
        String streamKey = streamKeyService.getStreamKey(email);

        streamService.stopEncoding(streamKey);
        return ResponseEntity.ok("Stream Stopped");
    }
}

