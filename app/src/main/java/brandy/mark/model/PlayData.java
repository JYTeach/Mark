package brandy.mark.model;

import java.util.List;

/**
 * Created by lenovo on 2016/11/30.
 */
public class PlayData {
    private List<PlaySources> videos;
    private List<PlaySources> sourses;
    private List<PlaySources> others;

    public List<PlaySources> getVideos() {
        return videos;
    }

    public void setVideos(List<PlaySources> videos) {
        this.videos = videos;
    }

    public List<PlaySources> getSourses() {
        return sourses;
    }

    public void setSourses(List<PlaySources> sourses) {
        this.sourses = sourses;
    }

    public List<PlaySources> getOthers() {
        return others;
    }

    public void setOthers(List<PlaySources> others) {
        this.others = others;
    }
}
