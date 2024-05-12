
package aiss.youtubeminer.model.youtube.videoSnippet;


import aiss.youtubeminer.model.youtube.caption.YoutubeCaption;
import aiss.youtubeminer.model.youtube.comment.YoutubeComment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeVideoSnippet {

    @JsonProperty("id")
    private YoutubeVideoSnippetId id;
    @JsonProperty("snippet")
    private YoutubeVideoSnippetDetails snippet;

    // These attributes have been manually added
    @JsonProperty("comments")
    private List<YoutubeComment> youtubeComments;

    @JsonProperty("captions")
    private List<YoutubeCaption> youtubeCaptions;

    public YoutubeVideoSnippet() {
        this.youtubeComments = new ArrayList<>();
        this.youtubeCaptions = new ArrayList<>();
    }

    @JsonProperty("comments")
    public List<YoutubeComment> getComments() {
        return youtubeComments;
    }

    @JsonProperty("comments")
    public void setComments(List<YoutubeComment> youtubeComments) {
        this.youtubeComments = youtubeComments;
    }

    @JsonProperty("captions")
    public List<YoutubeCaption> getCaptions() { return youtubeCaptions; }

    @JsonProperty("captions")
    public void setCaptions(List<YoutubeCaption> youtubeCaptions) {
        this.youtubeCaptions = youtubeCaptions;
    }

    @JsonProperty("id")
    public YoutubeVideoSnippetId getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(YoutubeVideoSnippetId id) {
        this.id = id;
    }

    @JsonProperty("snippet")
    public YoutubeVideoSnippetDetails getSnippet() {
        return snippet;
    }

    @JsonProperty("snippet")
    public void setSnippet(YoutubeVideoSnippetDetails snippet) {
        this.snippet = snippet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(YoutubeVideoSnippet.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("snippet");
        sb.append('=');
        sb.append(((this.snippet == null)?"<null>":this.snippet));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
