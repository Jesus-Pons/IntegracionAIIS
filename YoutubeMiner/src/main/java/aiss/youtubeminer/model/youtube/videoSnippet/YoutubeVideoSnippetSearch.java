
package aiss.youtubeminer.model.youtube.videoSnippet;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class YoutubeVideoSnippetSearch {

    @JsonProperty("nextPageToken")
    private String nextPageToken;
    @JsonProperty("items")
    private List<YoutubeVideoSnippet> items;

    @JsonProperty("nextPageToken")
    public String getNextPageToken() {
        return nextPageToken;
    }

    @JsonProperty("nextPageToken")
    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    @JsonProperty("items")
    public List<YoutubeVideoSnippet> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<YoutubeVideoSnippet> items) {
        this.items = items;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(YoutubeVideoSnippetSearch.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
