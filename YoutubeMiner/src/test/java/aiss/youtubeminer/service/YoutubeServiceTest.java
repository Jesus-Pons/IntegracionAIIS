package aiss.youtubeminer.service;

import aiss.youtubeminer.exception.ChannelNotFoundException;
import aiss.youtubeminer.model.video.Caption;
import aiss.youtubeminer.model.video.Channel;
import aiss.youtubeminer.model.video.Comment;
import aiss.youtubeminer.model.video.Video;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class YoutubeServiceTest {
    @Autowired
    YoutubeService youtubeService;
    @Test
    @DisplayName("Get Youtube channel")
    void getYoutubeChannel() throws ChannelNotFoundException {
        Channel channel = youtubeService.getYoutubeChannel("AIzaSyAgMVTyzlJWogPIg1dL5BunUOSmbR7q2Rw","UCBR8-60-B28hp2BmDPdntcQ");
        System.out.println(channel);
    }

    @Test
    @DisplayName("Get All videos from a Channel")
    void getVideos() {
        List<Video> videos = youtubeService.getVideos("AIzaSyAgMVTyzlJWogPIg1dL5BunUOSmbR7q2Rw","UCBR8-60-B28hp2BmDPdntcQ",10);
        System.out.println(videos);
    }

    @Test
    @DisplayName("Get All Captions from a Video")
    void getCaptions() {
        List<Caption> captions = youtubeService.getCaptions("AIzaSyAgMVTyzlJWogPIg1dL5BunUOSmbR7q2Rw","-G-XKgjTp14");
        System.out.println(captions);
    }

    @Test
    @DisplayName("Get All Comments from a Video")
    void getComments() {
        List<Comment> comments = youtubeService.getComments("AIzaSyAgMVTyzlJWogPIg1dL5BunUOSmbR7q2Rw","-G-XKgjTp14",10);
        System.out.println(comments);
    }
}