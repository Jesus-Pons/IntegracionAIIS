package aiss.vimeominer.service;

import aiss.vimeominer.model.video.*;
import aiss.vimeominer.model.vimeo.caption.VimeoCaptionSearch;
import aiss.vimeominer.model.vimeo.channel.VimeoChannel;
import aiss.vimeominer.model.vimeo.comment.VimeoComment;
import aiss.vimeominer.model.vimeo.caption.VimeoCaption;
import aiss.vimeominer.model.vimeo.comment.VimeoCommentSearch;
import aiss.vimeominer.model.vimeo.comment.VimeoUser;
import aiss.vimeominer.model.vimeo.videoSnippet.VimeoPaging;
import aiss.vimeominer.model.vimeo.videoSnippet.VimeoVideo;
import aiss.vimeominer.model.vimeo.videoSnippet.VimeoVideoSnippetSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import java.util.LinkedList;
import java.util.List;

@Service
public class VimeoService {
    @Autowired
    RestTemplate restTemplate;
 //Aqui va el parseo

    public List<Caption> getCaption(String videoId, String token){
        String uri= "https://api.vimeo.com/videos/" + videoId+"/texttracks";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "bearer " + token);
        HttpEntity<VimeoCaptionSearch> request= new HttpEntity<>(null,httpHeaders);
        ResponseEntity<VimeoCaptionSearch> response= restTemplate.exchange(uri, HttpMethod.GET,request, VimeoCaptionSearch.class);
        VimeoCaptionSearch vimeoCaptionSearch = response.getBody();
        List<VimeoCaption> vimeoCaptions = new LinkedList<>(vimeoCaptionSearch.getData());
        VimeoPaging vimeoPaging= vimeoCaptionSearch.getPaging();

        while(vimeoPaging.getNext()!=null){
            uri = "https://api.vimeo.com" + vimeoPaging.getNext();
            response = restTemplate.exchange(uri, HttpMethod.GET,request, VimeoCaptionSearch.class);
            vimeoCaptionSearch = response.getBody();
            vimeoPaging = vimeoCaptionSearch.getPaging();
            vimeoCaptions.addAll(vimeoCaptionSearch.getData());
        }
        List<Caption> res = new LinkedList<>();
        for(VimeoCaption vimeoCaption: vimeoCaptions){
            Caption caption = new Caption();
            caption.setId(vimeoCaption.getId().toString());
            caption.setLanguage(vimeoCaption.getLanguage());
            caption.setName(caption.getName());
            res.add(caption);
        }
        return res;
    }

    public Channel getChannel(String channelId,String token){
        String uri= "https://api.vimeo.com/channels/" + channelId;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "bearer " + token);
        HttpEntity<VimeoChannel> request= new HttpEntity<>(null,httpHeaders);
        ResponseEntity<VimeoChannel> response= restTemplate.exchange(uri, HttpMethod.GET,request, VimeoChannel.class);
        VimeoChannel vimeoChannel = response.getBody();
        Channel channel = new Channel();

        channel.setId(parseaId(vimeoChannel.getUri()));
        channel.setDescription(vimeoChannel.getDescription());
        channel.setCreatedTime(vimeoChannel.getCreatedTime());
        channel.setName(vimeoChannel.getName());

        return channel;
    }

    public List<Comment> getComment(String videoId, String token, int maxComments){
        String uri= "https://api.vimeo.com/videos/" + videoId +"/comments";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "bearer " + token);
        HttpEntity<VimeoCommentSearch> request= new HttpEntity<>(null,httpHeaders);
        List<Comment> comments = new LinkedList<>();
        try {
            ResponseEntity<VimeoCommentSearch> response = restTemplate.exchange(uri, HttpMethod.GET, request, VimeoCommentSearch.class);
            VimeoCommentSearch vimeoCommentSearch = response.getBody();

            List<VimeoComment> vimeoComments = new LinkedList<>();
            for(VimeoComment vimeoComment : vimeoCommentSearch.getData()){
                if(vimeoComments.size()<maxComments){
                    vimeoComments.add(vimeoComment);
                }else{
                    break;
                }
            }

            VimeoPaging vimeoPaging = vimeoCommentSearch.getPaging();
            while (vimeoPaging.getNext() != null && vimeoComments.size()<10) {
                uri = "https://api.vimeo.com" + vimeoPaging.getNext();
                response = restTemplate.exchange(uri, HttpMethod.GET, request, VimeoCommentSearch.class);
                vimeoCommentSearch = response.getBody();
                vimeoPaging = vimeoCommentSearch.getPaging();
                for(VimeoComment vimeoComment : vimeoCommentSearch.getData()){
                    if(vimeoComments.size()<maxComments){
                        vimeoComments.add(vimeoComment);
                    }else{
                        break;
                    }
                }
            }

            for (VimeoComment vimeoComment : vimeoComments) {
                Comment comment = new Comment();
                comment.setId(parseaId(vimeoComment.getUri()));
                comment.setText(vimeoComment.getText());
                comment.setCreatedOn(vimeoComment.getCreatedOn());

                User user = new User();

                VimeoUser vimeoUser = vimeoComment.getUser();
                user.setName(vimeoUser.getName());
                user.setUser_link(vimeoUser.getLink());
                user.setPicture_link(vimeoUser.getPictures().getBaseLink());
                user.setId(null);
                comment.setAuthor(user);

                comments.add(comment);
            }
        }catch (HttpClientErrorException ignored){

        }

        return comments;
    }

    public List<Video> getVideo(String ChannelId, String token, int maxVideos){
        String uri= "https://api.vimeo.com/channels/" + ChannelId +"/videos";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "bearer " + token);
        HttpEntity<VimeoVideo> request= new HttpEntity<>(null,httpHeaders);
        ResponseEntity<VimeoVideoSnippetSearch> response= restTemplate.exchange(uri, HttpMethod.GET,request, VimeoVideoSnippetSearch.class);
        VimeoVideoSnippetSearch vimeoVideoSnippetSearch = response.getBody();
        List<VimeoVideo> vimeoVideos = new LinkedList<>(vimeoVideoSnippetSearch.getData());
        VimeoPaging vimeoPaging = vimeoVideoSnippetSearch.getPaging();
        while(vimeoPaging.getNext()!=null){
            uri = "https://api.vimeo.com" + vimeoPaging.getNext();
            response = restTemplate.exchange(uri, HttpMethod.GET,request, VimeoVideoSnippetSearch.class);
            vimeoVideoSnippetSearch = response.getBody();
            vimeoPaging = vimeoVideoSnippetSearch.getPaging();
            vimeoVideos.addAll(vimeoVideoSnippetSearch.getData());
        }
        List<Video> res = new LinkedList<>();
        for(VimeoVideo vimeoVideo: vimeoVideos){
            Video video = new Video();
            video.setId(parseaId(vimeoVideo.getUri()));
            video.setName(vimeoVideo.getName());
            video.setDescription(vimeoVideo.getDescription());
            video.setReleaseTime(vimeoVideo.getReleaseTime());
            res.add(video);
        }

        return res;
    }

    private String parseaId(String uri){
        String[] spliter =  uri.trim().split("/");
        return spliter[spliter.length-1].trim();
    }



}
