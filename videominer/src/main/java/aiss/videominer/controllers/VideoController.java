package aiss.videominer.controllers;

import aiss.videominer.exception.VideoNotFoundException;
import aiss.videominer.model.Caption;
import aiss.videominer.model.Channel;
import aiss.videominer.model.Comment;
import aiss.videominer.model.Video;
import aiss.videominer.repository.ChannelRepository;
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
public class VideoController {
    @Autowired
    VideoRepository videoRepository;

    @GetMapping("/videos")
    public List<Video> getAllVideos(){
        List<Video> videos = new LinkedList<>();
        videos = videoRepository.findAll();
        return videos;
    }

    @GetMapping("/videos/{videoId}")
    public Video getVideoById(@PathVariable(value = "videoId") String videoId) throws VideoNotFoundException {
        Optional<Video> video= videoRepository.findById(videoId);
        if(!video.isPresent()){
            throw new VideoNotFoundException();
        }
        return video.get();
    }

    @GetMapping("/videos/{videoId}/comments")
    public List<Comment> getCommentsFromVideo(@PathVariable(value = "videoId") String videoId) throws VideoNotFoundException{
        Optional<Video> video= videoRepository.findById(videoId);
        if(!video.isPresent()){
            throw new VideoNotFoundException();
        }

        List<Comment> comments = video.get().getComments();
        return comments;
    }

    @GetMapping("/videos/{videoId}/captions")
    public List<Caption> getCaptionsFromVideo(@PathVariable(value = "videoId") String videoId) throws VideoNotFoundException{
        Optional<Video> video= videoRepository.findById(videoId);
        if(!video.isPresent()){
            throw new VideoNotFoundException();
        }
        List<Caption> captions = video.get().getCaptions();
        return captions;
    }


}
