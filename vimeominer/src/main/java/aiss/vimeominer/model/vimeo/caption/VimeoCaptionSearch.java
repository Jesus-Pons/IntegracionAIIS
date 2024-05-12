
package aiss.vimeominer.model.vimeo.caption;

import java.util.List;

import aiss.vimeominer.model.vimeo.videoSnippet.VimeoPaging;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VimeoCaptionSearch {
    @JsonProperty("data")
    private List<VimeoCaption> data;

    @JsonProperty("paging")
    private VimeoPaging paging;

    @JsonProperty("data")
    public List<VimeoCaption> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<VimeoCaption> data) {
        this.data = data;
    }

    @JsonProperty("paging")
    public VimeoPaging getPaging() {
        return paging;
    }

    @JsonProperty("paging")
    public void setPaging(VimeoPaging paging) {
        this.paging = paging;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(VimeoCaptionSearch.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
