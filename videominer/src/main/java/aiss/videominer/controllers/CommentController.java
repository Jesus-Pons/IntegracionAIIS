package aiss.videominer.controllers;

import aiss.videominer.exception.CommentNotFoundException;
import aiss.videominer.model.Channel;
import aiss.videominer.model.Comment;
import aiss.videominer.repository.ChannelRepository;
import aiss.videominer.repository.CommentRepository;
import aiss.videominer.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/videominer")
public class CommentController {
    @Autowired
    CommentRepository commentRepository;

    @GetMapping("/comments")
    public List<Comment> getAllComments(){
        List<Comment> comments = new LinkedList<>();
        comments = commentRepository.findAll();
        return comments;
    }

    @GetMapping("/comments/{commentId}")
    public Comment getCommentById(@PathVariable(value = "commentId") String commentId) throws CommentNotFoundException {
        Optional<Comment> comment= commentRepository.findById(commentId);
        if(!comment.isPresent()){
            throw new CommentNotFoundException();
        }
        return comment.get();
    }

}
