package brandy.mark.model;

/**
 * Created by lenovo on 2016/11/30.
 */
public class PlaySources {
    private String type = "";
    private int is_free = -1;// 0为免费，1为收费
    private String video_id;
    private String url;
    private String name;
    private String icon_url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIs_free() {
        return is_free;
    }

    public void setIs_free(int is_free) {
        this.is_free = is_free;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }
}
