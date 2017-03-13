package tk.ystyle.entity;

/**
 * 图片下载地址与文件保存位置的信息
 * Created by 小奕 on 2014-08-08 16:50.
 */
public class ImageAndFile {
    /**
     * 图片下载地址
     */
    private String url;
    /**
     * 文件保存位置
     */
    private String filename;

    public ImageAndFile(String url, String filename) {
        this.url = url;
        this.filename = filename;
    }

    public ImageAndFile() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "{" +
                "地址：'" + url + '\'' +
                ", 文件名：'" + filename + '\'' +
                '}';
    }
}
