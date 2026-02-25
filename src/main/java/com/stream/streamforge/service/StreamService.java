package com.stream.streamforge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stream.streamforge.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
@Service
public class StreamService {

    private final Map<String, Process> activeStreams = new ConcurrentHashMap<>();

    @Value("${stream.rtmp.url}")
    private String rtmpBaseUrl;

    public void startEncoding(String streamKey) throws IOException, InterruptedException {

        if (activeStreams.containsKey(streamKey)) {
            throw new IllegalStateException("Stream already running");
        }

        Path outputDir = Paths.get("hls", streamKey);

        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }

        String input = rtmpBaseUrl + "/" + streamKey;
        String output = outputDir.resolve("stream.m3u8").toString();
        List<String> command = List.of(
        		"/opt/homebrew/bin/ffmpeg",
                "-i", input,
                "-c:v", "libx264",
                "-preset", "veryfast",
                "-c:a", "aac",
                "-f", "hls",
                "-hls_time", "4",
                "-hls_list_size", "6",
                "-hls_flags", "delete_segments",
                output
        		);
//        ProcessBuilder pb = new ProcessBuilder(
//                "ffmpeg",
//                "-i", input,
//                "-c:v", "libx264",
//                "-preset", "veryfast",
//                "-c:a", "aac",
//                "-f", "hls",
//                "-hls_time", "4",
//                "-hls_list_size", "6",
//                "-hls_flags", "delete_segments",
//                output
//        );
//
//        pb.redirectErrorStream(true);

        Process process = new ProcessBuilder(command)
	            .redirectErrorStream(true)
	            .start();
	    try (BufferedReader reader =
	            new BufferedReader(new InputStreamReader(process.getInputStream()))) {
	    	String line;
	       while ((line = reader.readLine()) != null) {
	           System.out.println(line);
	       }
	   }

	    int exitCode = process.waitFor();
	    System.out.println("Exit code:"+exitCode);

	    if (exitCode != 0) {
	        throw new RuntimeException("FFmpeg failed");
	    }

        activeStreams.put(streamKey, process);
    }

    public void stopEncoding(String streamKey) {

        Process process = activeStreams.get(streamKey);

        if (process != null && process.isAlive()) {
            process.destroy();
        }

        activeStreams.remove(streamKey);
    }
}