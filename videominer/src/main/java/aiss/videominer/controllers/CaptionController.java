package aiss.videominer.controllers;

import aiss.videominer.model.Caption;
import aiss.videominer.model.Channel;
import aiss.videominer.repository.CaptionRepository;
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
public class CaptionController {
    @Autowired
    CaptionRepository captionRepository;

    @GetMapping("/captions")
    public List<Caption> findAllCaptions(){
        List<Caption> captions = new LinkedList<>();
        captions = captionRepository.findAll();
        return captions;
    }

    @GetMapping("/captions/{captionId}")
    public Caption getCaptionById(@PathVariable(value = "captionId") String captionId){
        Optional<Caption> caption= captionRepository.findById(captionId);
        return caption.get();
    }
}
