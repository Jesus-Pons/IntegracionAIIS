
package aiss.youtubeminer.model.youtube.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeCommentSearch {

    @JsonProperty("nextPageToken")
    private String nextPageToken;
    @JsonProperty("items")
    private List<YoutubeComment> items;

    @JsonProperty("nextPageToken")
    public String getNextPageToken() {
        return nextPageToken;
    }

    @JsonProperty("nextPageToken")
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    @JsonProperty("items")
    public List<YoutubeComment> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<YoutubeComment> items) {
        this.items = items;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(YoutubeCommentSearch.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("nextPageToken");
        sb.append('=');
        sb.append(((this.nextPageToken == null)?"<null>":this.nextPageToken));
        sb.append(',');
        sb.append("items");
        sb.append('=');
        sb.append(((this.items == null)?"<null>":this.items));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
