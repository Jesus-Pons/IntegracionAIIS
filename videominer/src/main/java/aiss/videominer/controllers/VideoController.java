package aiss.videominer.controllers;

import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Caption;
import aiss.videominer.model.Comment;
import aiss.videominer.model.Video;
import aiss.videominer.repository.CaptionRepository;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.VideoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Tag(name="Videos",description = "Video from a channel")
@RestController
@RequestMapping("/videominer")
public class VideoController {
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    CaptionRepository captionRepository;
    @Operation(
            summary = "Retrieve a List of videos",
            description = "Get videos from a channel",
            tags = {"video","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Video.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/videos")
    public List<Video> getAllVideos(@Parameter(description = "filter by name")@RequestParam(required = false) String name,
                                    @Parameter(description = "order of the requested channels (- befores attribute for desc)")@RequestParam(required = false) String order,
                                    @Parameter(description = "number of the page you want to see")@RequestParam(defaultValue = "0") int page,
                                    @Parameter(description = "number of channels per page")@RequestParam(defaultValue = "10") int size){

        Pageable paging;
        if(order != null){
            if(order.startsWith("-")){
                paging = PageRequest.of(page,size, Sort.by(order.substring(1)).descending());
            }else{
                paging = PageRequest.of(page,size,Sort.by(order).ascending());
            }

        }else{
            paging = PageRequest.of(page,size);
        }

        Page<Video> pageProjects;
        if(name==null){
            pageProjects = videoRepository.findAll(paging);
        }else{
            pageProjects = videoRepository.findByName(name,paging);
        }
        return pageProjects.getContent();
    }
    @Operation(
            summary = "Retrieve a videos",
            description = "Get videos by id",
            tags = {"video","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Video.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/videos/{videoId}")
    public Video getVideoById(@Parameter(description = "id of the video to be search")@PathVariable(value = "videoId") String videoId) throws VideoNotFoundException {
        Optional<Video> video= videoRepository.findById(videoId);
        if(!video.isPresent()){
            throw new VideoNotFoundException();
        }
        return video.get();
    }
    @Operation(
            summary = "Retrieve a List of comments",
            description = "Get all comments from a video",
            tags = {"comment","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Comment.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/videos/{videoId}/comments")
    public List<Comment> getCommentsFromVideo(@Parameter(description = "id of the video that owns comments to be search") @PathVariable(value = "videoId") String videoId,
                                              @Parameter(description = "order of the requested Comments (- attribute for desc)")@RequestParam(required = false) String order,
                                              @Parameter(description = "number of the page you want to see")@RequestParam(defaultValue = "0") int page,
                                              @Parameter(description = "number of channels per page")@RequestParam(defaultValue = "10") int size) throws VideoNotFoundException{
        Optional<Video> video= videoRepository.findById(videoId);
        if(!video.isPresent()){
            throw new VideoNotFoundException();
        }

        List<Comment> comments = new LinkedList<>(video.get().getComments());


        return comments;
    }

    @Operation(
            summary = "Retrieve a List of captions",
            description = "Get all captions from a video",
            tags = {"caption","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Caption.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/videos/{videoId}/captions")
    public List<Caption> getCaptionsFromVideo(@Parameter(description = "id of the video that owns captions to be search")@PathVariable(value = "videoId") String videoId,
                                              @Parameter(description = "filter by language")@RequestParam(required = false) String language,
                                              @Parameter(description = "order of the requested captions (- before attribute for desc)")@RequestParam(required = false) String order,
                                              @Parameter(description = "number of the page you want to see")@RequestParam(defaultValue = "0") int page,
                                              @Parameter(description = "number of channels per page")@RequestParam(defaultValue = "10") int size) throws VideoNotFoundException{
        Optional<Video> video= videoRepository.findById(videoId);
        if(!video.isPresent()){
            throw new VideoNotFoundException();
        }
        List<Caption> captions = new LinkedList<>(video.get().getCaptions());

        return captions;

    }


}
