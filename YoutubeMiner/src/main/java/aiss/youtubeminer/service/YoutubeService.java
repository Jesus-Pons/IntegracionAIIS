package aiss.youtubeminer.service;

import aiss.youtubeminer.exception.ChannelNotFoundException;
import aiss.youtubeminer.model.video.*;
import aiss.youtubeminer.model.youtube.caption.YoutubeCaption;
import aiss.youtubeminer.model.youtube.caption.YoutubeCaptionSearch;
import aiss.youtubeminer.model.youtube.channel.YoutubeChannel;
import aiss.youtubeminer.model.youtube.channel.YoutubeChannelSearch;
import aiss.youtubeminer.model.youtube.channel.YoutubeChannelSnippet;
import aiss.youtubeminer.model.youtube.comment.YoutubeComment;
import aiss.youtubeminer.model.youtube.comment.YoutubeCommentSearch;
import aiss.youtubeminer.model.youtube.comment.YoutubeTopLevelComment;
import aiss.youtubeminer.model.youtube.videoSnippet.YoutubeVideoSnippet;
import aiss.youtubeminer.model.youtube.videoSnippet.YoutubeVideoSnippetSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;

@Service
public class YoutubeService {

    @Autowired
    RestTemplate restTemplate;
    //String token = "AIzaSyAFt_onMV_5T2m8bNt1ab6sftTNRaOOClQ";

    // Método para obtener información de un canal dado su ID
    public Channel getYoutubeChannel(String token, String id) throws ChannelNotFoundException{

        // Construir la URI para la solicitud GET a la api de yt
        String uri = "https://www.googleapis.com/youtube/v3/channels?key="+token+"&part=snippet&id=" + id;
        // Realizar la solicitud GET y obtener la respuesta
        ResponseEntity<YoutubeChannelSearch> entity = restTemplate.getForEntity(uri, YoutubeChannelSearch.class);
        YoutubeChannelSearch youtubeChannelSearch = entity.getBody();
        if(youtubeChannelSearch.getItems()==null){
            throw new ChannelNotFoundException();
        }
        YoutubeChannel youtubeChannel = youtubeChannelSearch.getItems().get(0);

        Channel res = new Channel();
        res.setId(youtubeChannel.getId());
        YoutubeChannelSnippet youtubeChannelSnippet= youtubeChannel.getSnippet();
        res.setName(youtubeChannelSnippet.getTitle());
        res.setDescription(youtubeChannelSnippet.getDescription());
        res.setCreatedTime(youtubeChannelSnippet.getPublishedAt());
        return res;
    }


    public List<Video> getVideos(String token, String channelId, int maxVideos) {
        // Construir la URI
        String uri = "https://www.googleapis.com/youtube/v3/search?key="+token+"&part=snippet&&maxResults=50"+"&channelId="+channelId+"&type=video";
        // Realizar la solicitud GET y obtener la respuesta
        ResponseEntity<YoutubeVideoSnippetSearch> entity = restTemplate.exchange(uri, HttpMethod.GET, null, YoutubeVideoSnippetSearch.class);
        YoutubeVideoSnippetSearch youtubeVideoSnippetSearch = entity.getBody();
        //Obtenenmos y asignamos los subtitulos y los comentario al video
        List<YoutubeVideoSnippet> youtubeVideoSnippets = new LinkedList<>();
        for(YoutubeVideoSnippet youtubeVideoSnippet: youtubeVideoSnippetSearch.getItems()){
            if(youtubeVideoSnippets.size()<maxVideos){
                youtubeVideoSnippets.add(youtubeVideoSnippet);
            }
        }
        while(youtubeVideoSnippetSearch.getNextPageToken()!=null && youtubeVideoSnippets.size()<maxVideos){
            String nextPageToken = youtubeVideoSnippetSearch.getNextPageToken();
            uri = "https://www.googleapis.com/youtube/v3/search?key="+token+"&part=snippet&maxResults=550"+"&channelId="+channelId+"&type=video" + "&pageToken=" + nextPageToken;
            entity = restTemplate.exchange(uri, HttpMethod.GET, null, YoutubeVideoSnippetSearch.class);
            youtubeVideoSnippetSearch = entity.getBody();

            for(YoutubeVideoSnippet youtubeVideoSnippet: youtubeVideoSnippetSearch.getItems()){
                if(youtubeVideoSnippets.size()<maxVideos){
                    youtubeVideoSnippets.add(youtubeVideoSnippet);
                }else{
                    break;
                }
            }
        }
        List<Video> res = new LinkedList<>();
        for(YoutubeVideoSnippet youtubeVideoSnippet: youtubeVideoSnippets){
            Video video = new Video();
            video.setId(youtubeVideoSnippet.getId().getVideoId());
            video.setName(youtubeVideoSnippet.getSnippet().getTitle());
            video.setReleaseTime(youtubeVideoSnippet.getSnippet().getPublishedAt());
            video.setDescription(youtubeVideoSnippet.getSnippet().getDescription());
            res.add(video);
        }


        return res;
    }

    // Método para obtener subtítulos de un video dado su ID
    public List<Caption> getCaptions(String token, String videoId) {
        // Construir la URI
        String uri = "https://www.googleapis.com/youtube/v3/captions?key="+token+"&part=snippet"+"&videoId=" + videoId;
        // Realizar la solicitud GET y obtener la respuesta
        ResponseEntity<YoutubeCaptionSearch> entity = restTemplate.getForEntity(uri, YoutubeCaptionSearch.class);
        YoutubeCaptionSearch youtubeCaptionSearch = entity.getBody();
        List<YoutubeCaption> youtubeCaptions = new LinkedList<>(youtubeCaptionSearch.getItems());
        List<Caption> res = new LinkedList<>();
        for(YoutubeCaption youtubeCaption: youtubeCaptions){
            Caption caption= new Caption();

            caption.setId(youtubeCaption.getId());
            caption.setName(youtubeCaption.getSnippet().getName());
            caption.setLanguage(youtubeCaption.getSnippet().getLanguage());
            res.add(caption);
        }
        return res;
    }

    // Método para obtener comentarios de un video dado su ID
    public List<Comment> getComments(String token, String videoId, int maxComments) {
        // Construir la URI para la solicitud GET
        String uri = "https://www.googleapis.com/youtube/v3/commentThreads?key=" + token + "&maxResults=100" + "&part=snippet&videoId=" + videoId;
        List<Comment> res = new LinkedList<>();
        try {
            ResponseEntity<YoutubeCommentSearch> response = restTemplate.exchange(uri, HttpMethod.GET, null, YoutubeCommentSearch.class);
            YoutubeCommentSearch youtubeCommentSearch = response.getBody();
            List<YoutubeTopLevelComment> youtubeTopLevelComments = new LinkedList<>();
            for(YoutubeComment youtubeComment: youtubeCommentSearch.getItems()){
                if(youtubeTopLevelComments.size()<maxComments){
                    youtubeTopLevelComments.add(youtubeComment.getCommentSnippet().getTopLevelComment());
                }
            }
            while (youtubeCommentSearch.getNextPageToken() != null && youtubeTopLevelComments.size()<maxComments) {
                String nextPageToken = youtubeCommentSearch.getNextPageToken();
                uri = "https://www.googleapis.com/youtube/v3/commentThreads?key=" + token + "&part=snippet&videoId=" + videoId + "&maxResults=100" + "&pageToken=" + nextPageToken;
                response = restTemplate.getForEntity(uri, YoutubeCommentSearch.class);
                youtubeCommentSearch = response.getBody();
                for (YoutubeComment youtubeComment : youtubeCommentSearch.getItems()) {
                    if(youtubeTopLevelComments.size()<maxComments){
                    youtubeTopLevelComments.add(youtubeComment.getCommentSnippet().getTopLevelComment());
                    }else{
                        break;
                    }
                }
            }


            for (YoutubeTopLevelComment youtubeTopLevelComment : youtubeTopLevelComments) {
                Comment comment = new Comment();
                comment.setId(youtubeTopLevelComment.getId());
                comment.setText(youtubeTopLevelComment.getSnippet().getTextOriginal());
                comment.setCreatedOn(youtubeTopLevelComment.getSnippet().getPublishedAt());

                User user = new User();

                user.setName(youtubeTopLevelComment.getSnippet().getAuthorDisplayName());
                user.setUser_link(youtubeTopLevelComment.getSnippet().getAuthorChannelUrl());
                user.setPicture_link(youtubeTopLevelComment.getSnippet().getAuthorProfileImageUrl());
                user.setId(null);
                comment.setAuthor(user);

                res.add(comment);
            }
        } catch(HttpClientErrorException e){
            e.printStackTrace();
        }

        return res;
    }
}
