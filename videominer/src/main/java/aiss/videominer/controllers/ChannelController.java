package aiss.videominer.controllers;

import aiss.videominer.model.*;
import aiss.videominer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.AbstractRabbitListenerContainerFactoryConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/channels")
    public List<Channel> findAllChannels(){
        List<Channel> channels = new LinkedList<>();
        channels = channelRepository.findAll();
        return channels;
    }

    @GetMapping("/channels/{channelId}")
    public Channel getChannelById(@PathVariable(value = "channelId") String channelId){
        Optional<Channel> channel= channelRepository.findById(channelId);
        return channel.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/channels")
    public Channel create(@RequestBody Channel channel){;
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
