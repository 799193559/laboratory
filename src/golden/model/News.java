package golden.model;

public class News {
    private Integer newsId;

    private String title;

    private String username;

    private String photosource;

    private String news;

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotosource() {
        return photosource;
    }

    public void setPhotosource(String photosource) {
        this.photosource = photosource;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }
}