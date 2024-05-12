package aiss.videominer.controllers;

import aiss.videominer.exception.CommentNotFoundException;
import aiss.videominer.model.Caption;
import aiss.videominer.model.Channel;
import aiss.videominer.model.Comment;
import aiss.videominer.repository.ChannelRepository;
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

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
@Tag(name="Comment",description = "comments from a video")
@RestController
@RequestMapping("/videominer")
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @Operation(
            summary = "Retrieve a List of comments",
            description = "Get all comments",
            tags = {"comment","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Comment.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/comments")
    public List<Comment> getAllComments(@Parameter(description = "order of the requested Comments (- attribute for desc)")@RequestParam(required = false) String order,
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

        Page<Comment> pageProjects;
            pageProjects = commentRepository.findAll(paging);
        return pageProjects.getContent();
    }
    @Operation(
            summary = "Retrieve a comment",
            description = "Get comment by id",
            tags = {"comment","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Comment.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/comments/{commentId}")
    public Comment getCommentById(@Parameter(description = "id of the comment to be search") @PathVariable(value = "commentId") String commentId) throws CommentNotFoundException {
        Optional<Comment> comment= commentRepository.findById(commentId);
        if(!comment.isPresent()){
            throw new CommentNotFoundException();
        }
        return comment.get();
    }

}
