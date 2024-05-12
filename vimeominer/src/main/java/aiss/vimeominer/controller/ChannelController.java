package aiss.vimeominer.controller;

import aiss.vimeominer.exception.ChannelNotFoundException;
import aiss.vimeominer.model.video.Caption;
import aiss.vimeominer.model.video.Channel;
import aiss.vimeominer.model.video.Comment;
import aiss.vimeominer.model.video.Video;
import aiss.vimeominer.service.VimeoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@Tag(name = "Channel",description = "Vimeo Channel management API")
@RestController
@RequestMapping("/vimeo")
public class ChannelController {

    @Autowired
    VimeoService vimeoService;

    @Autowired
    RestTemplate restTemplate;
    @Operation(
            summary = "Retrieve a Vimeo Channel",
            description = "Get channel from vimeo with videos, comments, captions and users",
            tags = {"channel","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Channel.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/{id}")
    public Channel get(@Parameter(description = "id of the channel to be search")@PathVariable(value="id") String channelId, @Parameter(description = "max number of videos you want to search")@RequestParam(defaultValue = "10") int maxVideos, @Parameter(description = "max number of comments you want to search per video")@RequestParam(defaultValue = "10") int maxComments) throws ChannelNotFoundException{
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
    @Operation(
            summary = "Post a Vimeo Channel",
            description = "Post Vimeo Channel with own videos, comments, captions and users at videominer",
            tags = {"channel","post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201",content = {@Content(schema = @Schema(implementation = Channel.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}")
    public Channel post(@Parameter(description = "id of the channel you want to post")@PathVariable(value="id") String channelId, @Parameter(description = "max number of videos you want to post")@RequestParam(defaultValue = "10") int maxVideos,@Parameter(description = "max number of comments you want to post per video")@RequestParam(defaultValue = "10") int maxComments) throws  ChannelNotFoundException{
        Channel channel = get(channelId,maxVideos,maxComments);
        String uri = "http://localhost:8080/videominer/channels";
        Channel postChannel = restTemplate.postForObject(uri,channel, Channel.class);
        return postChannel;
    }

}