
package aiss.vimeominer.model.vimeo.comment;

import java.util.List;

import aiss.vimeominer.model.vimeo.videoSnippet.VimeoPaging;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VimeoCommentSearch {

    @JsonProperty("paging")
    private VimeoPaging vimeoPaging;
    @JsonProperty("data")
    private List<VimeoComment> data;

    @JsonProperty("paging")
    public VimeoPaging getPaging() {
        return vimeoPaging;
    }

    @JsonProperty("paging")
    public void setPaging(VimeoPaging vimeoPaging) {
        this.vimeoPaging = vimeoPaging;
    }

    @JsonProperty("data")
    public List<VimeoComment> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<VimeoComment> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(VimeoCommentSearch.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("paging");
        sb.append('=');
        sb.append(((this.vimeoPaging == null)?"<null>":this.vimeoPaging));
        sb.append(',');
        sb.append("data");
        sb.append('=');
        sb.append(((this.data == null)?"<null>":this.data));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
