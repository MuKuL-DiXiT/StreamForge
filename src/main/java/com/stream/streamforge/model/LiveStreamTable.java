package com.stream.streamforge.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "live_stream")
public class LiveStreamTable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @Column(name = "owner_id", nullable = false)
  private Long ownerId;

  @Column(name = "stream_key", unique = true, nullable = false)
  private String streamKey;

  @Column(name = "hls_url", columnDefinition = "TEXT")
  private String hlsUrl;

  @Enumerated(EnumType.STRING)
  private Status status;

  private LocalDateTime createdAt = LocalDateTime.now();

  public enum Status {
    offline, live, ended
  }

  // Getters and Setters
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public Long getOwnerId() { return ownerId; }
  public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

  public String getStreamKey() { return streamKey; }
  public void setStreamKey(String streamKey) { this.streamKey = streamKey; }

  public String getHlsUrl() { return hlsUrl; }
  public void setHlsUrl(String hlsUrl) { this.hlsUrl = hlsUrl; }

  public Status getStatus() { return status; }
  public void setStatus(Status status) { this.status = status; }

  public LocalDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
