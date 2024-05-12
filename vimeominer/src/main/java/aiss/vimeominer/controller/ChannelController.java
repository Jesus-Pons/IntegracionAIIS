package aiss.vimeominer.controller;

import aiss.vimeominer.exception.ChannelNotFoundException;
import aiss.vimeominer.model.video.Caption;
import aiss.vimeominer.model.video.Channel;
import aiss.vimeominer.model.video.Comment;
import aiss.vimeominer.model.video.Video;
import aiss.vimeominer.service.VimeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
@RequestMapping("/vimeo")
public class ChannelController {

    @Autowired
    VimeoService vimeoService;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/{id}")
    public Channel get(@PathVariable(value="id") String channelId, @RequestParam(defaultValue = "10") int maxVideos,@RequestParam(defaultValue = "10") int maxComments) throws ChannelNotFoundException{
        try {
            String token = "ce32ea28daeba3e407df2c63df795708";
            Channel res = vimeoService.getChannel(channelId, token);

        List<Video> videos = vimeoService.getVideo(channelId,token,maxVideos);

        for(Video video:videos){
            List<Caption> captions = vimeoService.getCaption(video.getId(),token);
            video.setCaptions(captions);
            List<Comment> comments = vimeoService.getComment(video.getId(),token,maxComments);
            video.setComments(comments);
        }

        res.setVideos(videos);

        return res;
        }catch (HttpClientErrorException e){
            throw new ChannelNotFoundException();
        }
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}")
    public Channel post(@PathVariable(value="id") String channelId, @RequestParam(defaultValue = "10") int maxVideos,@RequestParam(defaultValue = "10") int maxComments) throws  ChannelNotFoundException{
        Channel channel = get(channelId,maxVideos,maxComments);
        String uri = "http://localhost:8080/videominer/channels";
        Channel postChannel = restTemplate.postForObject(uri,channel, Channel.class);
        return postChannel;
    }

}