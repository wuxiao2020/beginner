package tk.ystyle.entity;

/**
 * 记录一张图片的信息
 * Created by 小奕 on 2014-08-08 13:21.
 */
public class Image {
    /**
     * 图片下标
     */
    private String index;
    /**
     * 图片地址
     */
    private String url;

    public Image() {
    }

    public Image(String index, String url) {
        this.index = index;
        this.url = url;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Image{" +
                "index=" + index +
                ", url='" + url + '\'' +
                '}';
    }
}
