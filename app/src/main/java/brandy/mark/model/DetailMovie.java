package brandy.mark.model;

/**
 * Created by lenovo on 2016/11/29.
 */
public class DetailMovie {
    private int id;
    private String name;
    private int show_type;
    private String img_url;
    private int likes;
    private int comments;
    private int shares;
    private String content;
    private String is_liked;
    private int is_foot;

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

    public int getShow_type() {
        return show_type;
    }

    public void setShow_type(int show_type) {
        this.show_type = show_type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIs_liked() {
        return is_liked;
    }

    public void setIs_liked(String is_liked) {
        this.is_liked = is_liked;
    }

    public int getIs_foot() {
        return is_foot;
    }

    public void setIs_foot(int is_foot) {
        this.is_foot = is_foot;
    }
}
