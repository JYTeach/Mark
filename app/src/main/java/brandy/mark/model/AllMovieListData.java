package brandy.mark.model;

import java.util.List;

/**
 * Created by lenovo on 2016/12/1.
 */
public class AllMovieListData {
    private String name;
    private String img_url;
    private List<SingleMovieListData> cat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public List<SingleMovieListData> getCat() {
        return cat;
    }

    public void setCat(List<SingleMovieListData> cat) {
        this.cat = cat;
    }
}
