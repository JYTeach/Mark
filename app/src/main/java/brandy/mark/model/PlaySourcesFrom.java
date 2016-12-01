package brandy.mark.model;

import java.util.List;

/**
 * Created by lenovo on 2016/11/30.
 */
public class PlaySourcesFrom {
    private String name;
    private List<PlaySources> playSources;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PlaySources> getPlaySources() {
        return playSources;
    }

    public void setPlaySources(List<PlaySources> playSources) {
        this.playSources = playSources;
    }
}
