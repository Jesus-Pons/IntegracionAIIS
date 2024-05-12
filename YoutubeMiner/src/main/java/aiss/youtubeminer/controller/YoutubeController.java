package aiss.youtubeminer.controller;
import aiss.youtubeminer.exception.ChannelNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import aiss.youtubeminer.service.*;
import aiss.youtubeminer.model.video.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

@Tag(name="Channel", description = "Youtube Channel management API")
@RestController
@RequestMapping("/youtube")
public class YoutubeController {

    // Inyectar RestTemplate y YoutubeService
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    YoutubeService youtubeService;
    @Operation(
            summary = "Retrieve a Youtube Channel",
            description = "Get channel from youtube with videos, comments, captions and users",
            tags = {"channel","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Channel.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/{id}")
    public Channel get(@Parameter(description = "id of the channel to be search")@PathVariable(value = "id") String channelId, @Parameter(description = "max number of videos you want to search")@RequestParam(defaultValue = "10") int maxVideos, @Parameter(description = "max number of comments you want to search per video")@RequestParam(defaultValue = "10") int maxComments ) throws ChannelNotFoundException {
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
    @Operation(
            summary = "Post a Youtube Channel",
            description = "Post Youtube Channel with own videos, comments, captions and users at videominer",
            tags = {"channel","post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201",content = {@Content(schema = @Schema(implementation = Channel.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}")
    public Channel post(@Parameter(description = "id of the channel you want to post")@PathVariable("id") String channelId, @Parameter(description = "max number of videos you want to post")@RequestParam(defaultValue = "10") int maxVideos,@Parameter(description = "max number of comments you want to post per video")@RequestParam(defaultValue = "10") int maxComments ) throws  ChannelNotFoundException{
        Channel channel = get(channelId,maxVideos,maxComments);
        String uri = "http://localhost:8080/videominer/channels";
        Channel postChannel = restTemplate.postForObject(uri, channel, Channel.class);
        return postChannel;
    }
}
