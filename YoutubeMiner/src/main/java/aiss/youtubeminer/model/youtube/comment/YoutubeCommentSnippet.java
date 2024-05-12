
package aiss.youtubeminer.model.youtube.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeCommentSnippet {

    private YoutubeTopLevelComment youtubeTopLevelComment;

    @JsonProperty("topLevelComment")
    public YoutubeTopLevelComment getTopLevelComment() {
        return youtubeTopLevelComment;
    }

    @JsonProperty("topLevelComment")
    public void setTopLevelComment(YoutubeTopLevelComment youtubeTopLevelComment) {
        this.youtubeTopLevelComment = youtubeTopLevelComment;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(YoutubeCommentSnippet.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("topLevelComment");
        sb.append('=');
        sb.append(((this.youtubeTopLevelComment == null)?"<null>":this.youtubeTopLevelComment));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
