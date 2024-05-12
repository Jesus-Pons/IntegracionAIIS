package aiss.youtubeminer.controller;
import aiss.youtubeminer.exception.ChannelNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import aiss.youtubeminer.service.*;
import aiss.youtubeminer.model.video.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/youtube")
public class YoutubeController {

    // Inyectar RestTemplate y YoutubeService
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    YoutubeService youtubeService;

    @GetMapping("/{id}")
    public Channel get(@PathVariable(value = "id") String channelId, @RequestParam(defaultValue = "10") int maxVideos,@RequestParam(defaultValue = "10") int maxComments ) throws ChannelNotFoundException {
        String token = "AIzaSyAgMVTyzlJWogPIg1dL5BunUOSmbR7q2Rw";


            Channel res = youtubeService.getYoutubeChannel(token, channelId);
            List<Video> videos = youtubeService.getVideos(token, channelId, maxVideos);
            for (Video video : videos) {
                List<Caption> captions = youtubeService.getCaptions(token, video.getId());
                video.setCaptions(captions);
                List<Comment> comments = youtubeService.getComments(token, video.getId(), maxComments);
                video.setComments(comments);
            }
            res.setVideos(videos);
            return res;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}")
    public Channel post(@PathVariable("id") String channelId, @RequestParam(defaultValue = "10") int maxVideos,@RequestParam(defaultValue = "10") int maxComments ) throws  ChannelNotFoundException{
        Channel channel = get(channelId,maxVideos,maxComments);
        String uri = "http://localhost:8080/videominer/channels";
        Channel postChannel = restTemplate.postForObject(uri, channel, Channel.class);
        return postChannel;
    }
}
