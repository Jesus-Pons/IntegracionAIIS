package aiss.videominer.controllers;

import aiss.videominer.exception.ChannelNotFoundException;
import aiss.videominer.exception.GlobalExceptionHandler;
import aiss.videominer.model.*;
import aiss.videominer.repository.*;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.AbstractRabbitListenerContainerFactoryConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
@Tag(name= "Channel", description = "Channels from video DB")
@RestController
@RequestMapping("/videominer")
public class ChannelController {

    @Autowired
    ChannelRepository channelRepository;
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    CaptionRepository captionRepository;
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    UserRepository userRepository;
    @Operation(
            summary = "Retrieve a List of channels",
            description = "Get all channels",
            tags = {"channel","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Channel.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/channels")
    public List<Channel> getAllChannels(@Parameter(description = "filter by name")@RequestParam(required = false) String name,
                                        @Parameter(description = "order of the requested channels (- befores attribute for desc)")@RequestParam(required = false) String order,
                                        @Parameter(description = "number of the page you want to see")@RequestParam(defaultValue = "0") int page,
                                        @Parameter(description = "number of channels per page")@RequestParam(defaultValue = "10") int size){
        Pageable paging;
        if(order != null){
            if(order.startsWith("-")){
                paging = PageRequest.of(page,size,Sort.by(order.substring(1)).descending());
            }else{
                paging = PageRequest.of(page,size,Sort.by(order).ascending());
            }

        }else{
            paging = PageRequest.of(page,size);
        }

        Page<Channel> pageProjects;
        if(name==null){
            pageProjects = channelRepository.findAll(paging);
        }else{
            pageProjects = channelRepository.findByName(name,paging);
        }
        return pageProjects.getContent();
    }

    @Operation(
            summary = "Retrieve a channel",
            description = "Get channel by id",
            tags = {"channel","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Channel.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/channels/{channelId}")
    public Channel getChannelById( @Parameter(description = "id of the channel to be search") @PathVariable(value = "channelId") String channelId) throws ChannelNotFoundException{
        Optional<Channel> channel= channelRepository.findById(channelId);
        if(!channel.isPresent()){
            throw new ChannelNotFoundException();
        }
        return channel.get();
    }
    @Operation(
            summary = "Post a channel",
            description = "Posta a new channel",
            tags = {"channel","post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Channel.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/channels")
    public Channel createChannel(@RequestBody @Valid Channel channel){;
        List<Video> videos = channel.getVideos();
        channel.setVideos(new ArrayList<>());

        Channel postChannel = channelRepository.save(channel);

        List<Video> postVideos = new ArrayList<>();
        for(Video video: videos){
            List<Comment> comments = video.getComments();
            List<Caption> captions = video.getCaptions();
            video.setComments(new ArrayList<>());
            for(Comment comment: comments){
                Comment newComment= new Comment();
                newComment.setText(comment.getText());
                User newUser = new User();
                newComment.setId(comment.getId());
                newUser.setUser_link(comment.getAuthor().getUser_link());
                newUser.setPicture_link(comment.getAuthor().getPicture_link());
                newUser.setName(comment.getAuthor().getName());
                newComment.setText(comment.getText());
                newComment.setCreatedOn(comment.getCreatedOn());
                userRepository.save(newUser);
                newComment.setAuthor(newUser);
                commentRepository.save(newComment);
                video.getComments().add(newComment);
            }
            Video postVideo = videoRepository.save( video);

            for(Caption caption : captions){
                captionRepository.save(caption);
            }
            postVideos.add(postVideo);

        }
        postChannel.setVideos(postVideos);
        return postChannel;
    }
}
