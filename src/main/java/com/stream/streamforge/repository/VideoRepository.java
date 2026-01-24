package com.stream.streamforge.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stream.streamforge.model.VideoTable;

public interface VideoRepository extends JpaRepository <VideoTable, Long>{
	Optional<VideoTable> findById(String id);
}