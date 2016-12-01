package brandy.mark.model;

import java.util.List;

/**
 * Created by lenovo on 2016/11/30.
 */
public class DetailMovieAllInfo {
    private int id  ;
    private int db_num;
    private String name;
    private String img_url;
    private String dbrating;
    private String pubdate;
    private String exhibit_pubdate;
    private String duration;
    private String genres;
    private String directors;
    private String writers;
    private String casts;
    private String summary;
    private String trailer_urls;
    private List<String> photos;
    private String db_link;
    private List<MovieLike> links;
    private List<MovieRelate> relate_singles;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDbrating() {
        return dbrating;
    }

    public void setDbrating(String dbrating) {
        this.dbrating = dbrating;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getExhibit_pubdate() {
        return exhibit_pubdate;
    }

    public void setExhibit_pubdate(String exhibit_pubdate) {
        this.exhibit_pubdate = exhibit_pubdate;
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

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers;
    }

    public String getCasts() {
        return casts;
    }

    public void setCasts(String casts) {
        this.casts = casts;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTrailer_urls() {
        return trailer_urls;
    }

    public void setTrailer_urls(String trailer_urls) {
        this.trailer_urls = trailer_urls;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public String getDb_link() {
        return db_link;
    }

    public void setDb_link(String db_link) {
        this.db_link = db_link;
    }

    public List<MovieLike> getLinks() {
        return links;
    }

    public void setLinks(List<MovieLike> links) {
        this.links = links;
    }

    public List<MovieRelate> getRelate_singles() {
        return relate_singles;
    }

    public void setRelate_singles(List<MovieRelate> relate_singles) {
        this.relate_singles = relate_singles;
    }
}
