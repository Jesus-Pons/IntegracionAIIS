package aiss.vimeominer.service;

import aiss.vimeominer.model.video.Caption;
import aiss.vimeominer.model.video.Channel;
import aiss.vimeominer.model.video.Comment;
import aiss.vimeominer.model.video.Video;
import aiss.vimeominer.model.vimeo.channel.VimeoChannel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@SpringBootTest
class VimeoServiceTest {

    @Autowired
    VimeoService service;

    @Test
    @DisplayName("get channels")
    void getChannel() {
        String ChannelId = "1234";
        String token = "ce32ea28daeba3e407df2c63df795708";
        Channel channel = service.getChannel(ChannelId, token);
        System.out.println(channel);
    }
    @Test
    @DisplayName("get videos")
    void getVideo(){
        String ChannelId = "1234";
        String token = "ce32ea28daeba3e407df2c63df795708";
        Integer maxVideos = 10;
        List<Video> videos = service.getVideo(ChannelId, token, maxVideos);
        assertNotEquals(null, videos, "the list is empty");
        System.out.println(videos);

    }

    @Test
    @DisplayName("get captions")
    void getCaption(){
        String ChannelId = "1234";
        String token = "ce32ea28daeba3e407df2c63df795708";
        List<Caption> captions = service.getCaption(ChannelId, token);
        assertNotEquals(null, captions, "the list is empty");
        System.out.println(captions);

    }

    @Test
    @DisplayName("get comments")
    void getComment(){
        String ChannelId = "1234";
        String token = "ce32ea28daeba3e407df2c63df795708";
        Integer maxComments = 10;
        List<Comment> comments = service.getComment(ChannelId, token, maxComments);
        assertNotEquals(null, comments, "the list is empty");
        System.out.println(comments);

    }


}