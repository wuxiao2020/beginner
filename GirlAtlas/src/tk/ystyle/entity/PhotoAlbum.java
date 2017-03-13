package tk.ystyle.entity;

import java.util.List;

/**
 * 相册信息
 * Created by 小奕 on 2014-08-08 13:21.
 */
public class PhotoAlbum {
    /**
     * 标题
     */
    private String title;
    /**
     * 相册地址
     */
    private String url;
    /**
     * 相册封面图片地址
     */
    private String mainImg;
    /**
     * 相册图片张数
     */
    private String number;
    /**
     * 相册创建时间
     */
    private String createTime;
    /**
     * 相册的图片信息
     */
    private List<Image> images;

    public PhotoAlbum(String title, String url, String mainImg, String number, String createTime, List<Image> images) {
        this.title = title;
        this.url = url;
        this.mainImg = mainImg;
        this.number = number;
        this.createTime = createTime;
        this.images = images;
    }

    public PhotoAlbum() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMainImg() {
        return mainImg;
    }

    public void setMainImg(String mainImg) {
        this.mainImg = mainImg;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "PhotoAlbum{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", mainImg='" + mainImg + '\'' +
                ", number='" + number + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
