## Planned MVP

   - [X] user auth
   - [X] video upoad
   - [X] stream on demand
   - live stream (working on this)
<hr>

## High-Level Design
<img width="531" height="423" alt="Screenshot 2026-01-17 at 12 11 49 AM" src="https://github.com/user-attachments/assets/f177b9f4-fe55-4c08-bab8-6f4445c9618c" />


## Initial DB
      mySQL 9.5
<img width="688" height="542" alt="Screenshot 2026-01-17 at 12 04 40 AM" src="https://github.com/user-attachments/assets/1deecffd-8597-4727-8e07-6ba938bae733" />

## ORM
      Spring Data JPA
## VOD
   <ul>
         <li>FFmpeg is used for video transcoding and to generate multiple bitrate and
      resolution variants required for adaptive bitrate (HLS) streaming. </li>
      <li> The FFmpeg process is executed using Java’s ProcessBuilder, which runs FFmpeg
      as an external OS-level process, since FFmpeg itself is implemented in C/C++. </li>
      </ul>

## Live Stream
Using RTMP for uploading video continuously and FFMPEG for continuously processing the video (enocode it and create HLS output).
For static video, NGNIX can be used (planned) to scale it for viewers (easy 100k), might scale for encoding simultaneously but now right now.

# Controllers

Auth

      /auth/signup   
      /auth/login
Video

      /video/upload
      /video/play

