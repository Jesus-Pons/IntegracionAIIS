
package aiss.vimeominer.model.vimeo.comment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VimeoComment {

    @JsonProperty("uri")
    private String uri;
    @JsonProperty("text")
    private String text;
    @JsonProperty("created_on")
    private String createdOn;
    @JsonProperty("user")
    private VimeoUser vimeoUser;

    @JsonProperty("uri")
    public String getUri() {
        return uri;
    }

    @JsonProperty("uri")
    public void setUri(String uri) {
        this.uri = uri;
    }
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("created_on")
    public String getCreatedOn() {
        return createdOn;
    }

    @JsonProperty("created_on")
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
    @JsonProperty("user")
    public VimeoUser getUser() {
        return vimeoUser;
    }

    @JsonProperty("user")
    public void setUser(VimeoUser vimeoUser) {
        this.vimeoUser = vimeoUser;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(VimeoComment.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("uri");
        sb.append('=');
        sb.append(((this.uri == null)?"<null>":this.uri));
        sb.append(',');
        sb.append("text");
        sb.append('=');
        sb.append(((this.text == null)?"<null>":this.text));
        sb.append(',');
        sb.append("createdOn");
        sb.append('=');
        sb.append(((this.createdOn == null)?"<null>":this.createdOn));
        sb.append(',');
        sb.append("user");
        sb.append('=');
        sb.append(((this.vimeoUser == null)?"<null>":this.vimeoUser));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
