package com.stream.streamforge.controller;

import java.io.IOException;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.stream.streamforge.dto.PlayResponse;
import com.stream.streamforge.dto.VideoResponse;
import com.stream.streamforge.service.CloudinaryService;
import com.stream.streamforge.service.VideoService;

@RestController 
@RequestMapping("/video")
public class VideoController {
	public final VideoService videoService;
	public final CloudinaryService cloudinaryService;
	public VideoController(VideoService videoService, CloudinaryService cloudinaryService){
		this.videoService = videoService;
		this.cloudinaryService = cloudinaryService;
	}
	@PostMapping("/upload")
	public VideoResponse videoUpload(@RequestParam("video") MultipartFile video,
			@RequestParam("title") String title,
			@RequestParam("description") String description) throws IOException {
		String videoUrl = cloudinaryService.upload(video);
		return videoService.savetoDB(videoUrl, title, description);
	}
	@GetMapping("/play")
	public PlayResponse playVideo(@RequestParam("videoId") Long videoId) throws RuntimeException, IOException, InterruptedException {
		return videoService.processVideo(videoId);
	}
	
}
