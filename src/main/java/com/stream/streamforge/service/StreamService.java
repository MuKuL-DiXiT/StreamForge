package com.stream.streamforge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StreamService {

    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    @Value("${stream.root}")
    private String streamRoot;

    @Value("${rtmp.base-url}")
    private String rtmpBaseUrl; 
    // example: rtmp://localhost:1935/live

    private final Map<String, Process> activeStreams = new ConcurrentHashMap<>();

    /**
     * Start a new live stream
     */
    public String startStream() throws IOException {

        String streamId = UUID.randomUUID().toString();
        String rtmpUrl = rtmpBaseUrl + "/" + streamId;

        File outputDir = new File(streamRoot, streamId);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        List<String> command =  List.of(
                ffmpegPath,
                "-i", rtmpUrl,

                "-c:v", "libx264",
                "-preset", "veryfast",
                "-g", "60",
                "-keyint_min", "60",
                "-sc_threshold", "0",

                "-hls_time", "2",
                "-hls_list_size", "6",
                "-hls_flags", "delete_segments",

                "-f", "hls",
                new File(outputDir, "live.m3u8").getAbsolutePath()
        );

        Process process = new ProcessBuilder(command)
        	    .redirectErrorStream(true)
        	    .start();

        	activeStreams.put(streamId, process);


        return streamId;
    }

    /**
     * Stop a running stream
     */
    public void stopStream(String streamId) {
        Process process = activeStreams.remove(streamId);

        if (process != null && process.isAlive()) {
            process.destroy();
        }

        deleteStreamDirectory(streamId);
    }

    /**
     * Check if stream is running
     */
    public boolean isStreamActive(String streamId) {
        Process process = activeStreams.get(streamId);
        return process != null && process.isAlive();
    }

    private void deleteStreamDirectory(String streamId) {
        File dir = new File(streamRoot, streamId);
        if (dir.exists()) {
            deleteRecursively(dir);
        }
    }

    private void deleteRecursively(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteRecursively(child);
            }
        }
        file.delete();
    }
}
