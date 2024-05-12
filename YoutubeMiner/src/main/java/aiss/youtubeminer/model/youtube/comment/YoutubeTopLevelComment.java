
package aiss.youtubeminer.model.youtube.comment;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeTopLevelComment {

    @JsonProperty("id")
    private String id;
    @JsonProperty("snippet")
    private YoutubeCommentSnippet__1 snippet;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("snippet")
    public YoutubeCommentSnippet__1 getSnippet() {
        return snippet;
    }

    @JsonProperty("snippet")
    public void setSnippet(YoutubeCommentSnippet__1 snippet) {
        this.snippet = snippet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(YoutubeTopLevelComment.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
