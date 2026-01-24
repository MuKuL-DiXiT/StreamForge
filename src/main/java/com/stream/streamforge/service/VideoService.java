package com.stream.streamforge.service;

import org.springframework.stereotype.Service;

import com.stream.streamforge.dto.VideoResponse;
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
}
