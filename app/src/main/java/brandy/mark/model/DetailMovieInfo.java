package brandy.mark.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by lenovo on 2016/11/29.
 */
public class DetailMovieInfo implements Parcelable{

    private String movie_id;
    private int db_num;
    private String name;
    private String img_url;
    private String pubdate;
    private String duration;
    private String genres;//所属片类型
    private String dbrating;//豆瓣评分
    private String is_done;
    private String clicktype;//点的哪个按钮  stage_photo、leftbtn、rightbtn、img

    private List<String> urls;//仅当stage_photo时不为null

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public int getDb_num() {
        return db_num;
    }

    public void setDb_num(int db_num) {
        this.db_num = db_num;
    }

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

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getDbrating() {
        return dbrating;
    }

    public void setDbrating(String dbrating) {
        this.dbrating = dbrating;
    }

    public String getIs_done() {
        return is_done;
    }

    public void setIs_done(String is_done) {
        this.is_done = is_done;
    }

    public String getClicktype() {
        return clicktype;
    }

    public void setClicktype(String clicktype) {
        this.clicktype = clicktype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.movie_id);
        dest.writeInt(this.db_num);
        dest.writeString(this.name);
        dest.writeString(this.img_url);
        dest.writeString(this.pubdate);
        dest.writeString(this.duration);
        dest.writeString(this.genres);
        dest.writeString(this.dbrating);
        dest.writeString(this.is_done);
        dest.writeString(this.clicktype);
        dest.writeStringList(this.urls);
    }

    public DetailMovieInfo() {
    }

    protected DetailMovieInfo(Parcel in) {
        this.movie_id = in.readString();
        this.db_num = in.readInt();
        this.name = in.readString();
        this.img_url = in.readString();
        this.pubdate = in.readString();
        this.duration = in.readString();
        this.genres = in.readString();
        this.dbrating = in.readString();
        this.is_done = in.readString();
        this.clicktype = in.readString();
        this.urls = in.createStringArrayList();
    }

    public static final Creator<DetailMovieInfo> CREATOR = new Creator<DetailMovieInfo>() {
        @Override
        public DetailMovieInfo createFromParcel(Parcel source) {
            return new DetailMovieInfo(source);
        }

        @Override
        public DetailMovieInfo[] newArray(int size) {
            return new DetailMovieInfo[size];
        }
    };
}
