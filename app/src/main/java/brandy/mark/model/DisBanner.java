package brandy.mark.model;

public class DisBanner  {
    private int id;
    private String name;
    private int type;//1启动影单详情(webview)，2启动热门影单(recyclerView)
    private String img_url;//图片资源


    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
