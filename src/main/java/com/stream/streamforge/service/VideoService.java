package com.stream.streamforge.service;

import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.stream.streamforge.dto.PlayResponse;
import com.stream.streamforge.dto.VideoResponse;
import com.stream.streamforge.model.VideoStatus;
import com.stream.streamforge.model.VideoTable;
import com.stream.streamforge.repository.VideoRepository;

@Service
public class VideoService {
	public final VideoRepository videoRepo;
	public VideoService(VideoRepository videoRepo) {
		this.videoRepo = videoRepo;
	}
	public VideoResponse savetoDB(String videoUrl, String title, String description) {
		VideoTable videoTable = new VideoTable();
		videoTable.setDescription(description);
		videoTable.setTitle(title);
		videoTable.setVideoUrl(videoUrl);
		Long id = (long) 1; // to be changed when middleware for storing the  for login is set
		videoTable.setOwnerId(id);
		videoRepo.save(videoTable);
		VideoResponse videoResponse = new VideoResponse();
		videoResponse.setTitle(title);
		videoResponse.setVideoUrl(videoUrl);
		return videoResponse;
		
	}
	public Path downloadVideo(Long videoId, String videoUrl) throws IOException {
		Path output = Paths.get("videos/tmp/video", videoId+".mp4");
		URI uri = URI.create(videoUrl);
		URL url = uri.toURL();
		try (InputStream in = url.openStream()){
			Files.copy(in, output, StandardCopyOption.REPLACE_EXISTING);
		}
		return output;
	}
	public Path createHlsDir(Long videoId) throws IOException {
	    Path hlsDir = Paths.get("videos/storage/hls", videoId.toString());
	    Files.createDirectories(hlsDir);
	    return hlsDir;
	}
	private void runFfmpeg(Path input, Path outputDir) throws IOException, InterruptedException {

		List<String> command = List.of(
			    "/opt/homebrew/bin/ffmpeg",
			    "-i", input.toString(),

			    "-filter_complex",
			    "[0:v]split=2[v360][v720];" +
			    "[v360]scale=640:360[v360out];" +
			    "[v720]scale=1280:720[v720out]",

			    "-map", "[v360out]",
			    "-map", "0:a",
			    "-map", "[v720out]",
			    "-map", "0:a",

			    "-c:v", "libx264",
			    "-preset", "veryfast",
			    "-c:a", "aac",

			    "-b:v:0", "800k",
			    "-b:v:1", "2500k",

			    "-hls_time", "4",
			    "-hls_list_size", "0",
			    "-hls_playlist_type", "vod",

			    "-var_stream_map", "v:0,a:0 v:1,a:1",
			    "-hls_segment_filename", outputDir + "/output_%v_%03d.ts",
			    "-master_pl_name", "master.m3u8",
			    outputDir + "/output_%v.m3u8"
			);


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
	}
	@Async
	public PlayResponse processVideo(Long videoId) throws RuntimeException, IOException, InterruptedException{
		VideoTable video = videoRepo.findById(videoId)
		        .orElseThrow(() -> new RuntimeException("Video not found"));
	   
	        video.setStatus(VideoStatus.PROCESSING);
	        videoRepo.save(video);

	        Path input = downloadVideo(video.getId(), video.getVideoUrl());
	        Path hlsDir = createHlsDir(video.getId());

	        runFfmpeg(input, hlsDir);

	        video.setStatus(VideoStatus.READY);
	        videoRepo.save(video);
	        String pathToReturn = "videos/storage/hls" + videoId.toString();
	        PlayResponse play = new PlayResponse();
	        play.setVideoUrl(pathToReturn);
	        return play;
	}
}

