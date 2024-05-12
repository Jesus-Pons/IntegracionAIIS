package aiss.videominer.controllers;

import aiss.videominer.model.Channel;
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
    public List<Video> findAllChannels(){
        List<Video> videos = new LinkedList<>();
        videos = videoRepository.findAll();
        return videos;
    }

    @GetMapping("/videos/{videoId}")
    public Video getChannelById(@PathVariable(value = "videoId") String videoId){
        Optional<Video> video= videoRepository.findById(videoId);
        return video.get();
    }
}
