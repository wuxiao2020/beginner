package tk.ystyle.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tk.ystyle.entity.Image;
import tk.ystyle.entity.ImageAndFile;
import tk.ystyle.entity.PhotoAlbum;

import javax.imageio.stream.FileImageOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 下载相册的处理逻辑
 * Created by 小奕 on 2014-08-08 13:06.
 */
public class GirlAtlasService {
    /**
     * 缓存
     */
    Map<String, String> cookies;

    public String getBaseUrl() {
        return "http://girl-atlas.com/";
    }

    public Map<String, String> getCookies() {
        return cookies.size() > 0 ? cookies : login("lxy5266@live.com", "hhhhhhhhh", "on");
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }


    /**
     * 登陆
     *
     * @param userName   用户名
     * @param passwd     密码
     * @param rememberMe 记住我
     * @return
     */
    public Map<String, String> login(String userName, String passwd, String rememberMe) {
        Connection.Response res = null;
        try {
            res = Jsoup.connect(getBaseUrl() + "j_spring_security_check")
                    .data(
                            "j_username", userName,
                            "j_password", passwd,
                            "_spring_security_remember_me", rememberMe
                    )
                    .method(Connection.Method.POST)
                    .timeout(1000 * 30)
                    .userAgent("Mozilla/5.0 (ArchLinux Linux 3.16) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.0 Chrome/30.0.1599.101 Safari/537.36")
                    .execute();
            setCookies(res.cookies());
            return cookies;
        } catch (Exception e) {
            System.out.println("登陆失败！！！");
            return null;
        }
    }

    /**
     * 获得相册
     *
     * @param nextURL 相册地址
     * @return
     */
    public List<PhotoAlbum> getPhotoAlbums(String nextURL) {
        List<PhotoAlbum> photoAlbums = new ArrayList<PhotoAlbum>();
        try {
            Document doc = getDocument(nextURL);
            Elements elements = doc.select("div.column.grid_6.grid");
            for (Element element : elements) {
                PhotoAlbum photoAlbum = new PhotoAlbum();
                String title = element.attr("title");
                String url = element.select("div.bubble.play > a").first().attr("href");
                String mainImg = element.select("  div.column.grid_6.cell > a").first().attr("photo");
                String number = element.select("div.column.grid_6.bar > .byline > a").first().text();
                String createTime = element.select("div.column.grid_6.bar > .byline > .date").first().text();
                photoAlbum.setTitle(title);
                photoAlbum.setUrl(url);
                photoAlbum.setMainImg(mainImg);
                photoAlbum.setNumber(number);
                photoAlbum.setCreateTime(createTime);
                photoAlbums.add(photoAlbum);
                System.out.println("获取信息完成:"+photoAlbum);
            }
        } catch (Exception e) {
            System.out.println("获取相册失败！！！地址：" + nextURL);
            return photoAlbums;
        }
        return photoAlbums;
    }

    /**
     * 下载页面
     *
     * @param nextURL 地址
     * @return
     * @throws Exception
     */
    private Document getDocument(String nextURL) throws Exception {
        return Jsoup.connect(nextURL)
                .cookies(getCookies())
                .timeout(30000)
                .userAgent("Mozilla/5.0 (ArchLinux Linux 3.16) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.0 Chrome/30.0.1599.101 Safari/537.36")
                .get();
    }

    /**
     * 获得下一页的地址
     *
     * @param url 地址
     * @return
     */
    public String getNextPagesUrl(String url) {
        String nextURL = null;
        try {
            Document doc = getDocument(url);
            Element element = doc.select("a.btn-form.next").first();

            if (element != null) {
                nextURL = element.absUrl("href");//.attr("href");
            }
            return nextURL;
        } catch (Exception e) {
            System.out.println("获取下一页的URL失败！！！地址：" + url);
            return nextURL;
        }
    }

    /**
     * 获得整个网站所有相册页面的地址
     *
     * @return
     */
    public List<String> getAllPagesURL() {
        List<String> urls = new ArrayList<>();
        String nextUrl = getBaseUrl();
        do {
            urls.add(nextUrl);
            System.out.println("获得页面:"+nextUrl);
            nextUrl = getNextPagesUrl(nextUrl);
        } while (nextUrl != null && !"".equals(nextUrl));
        return urls;
    }

    /**
     * 获得相册里的地址
     *
     * @param photoAlbum 相册
     * @return
     */
    public PhotoAlbum getAPhotoAlbumImages(PhotoAlbum photoAlbum) {
        String url = photoAlbum.getUrl();
        try {
            Document doc = getDocument(url);
            Elements elements = doc.select("li.slide");
            List<Image> images = new ArrayList<>();
            for (Element element : elements) {
                String index = element.attr("index");
                String src = element.select("img").first().attr("src");
                if (src == null || "".equals(src)) {
                    src = element.select("img").first().attr("delay");
                }
                Image image = new Image(index, src);
                images.add(image);
            }
            System.out.println("获取图片信息完成:"+photoAlbum);
            photoAlbum.setImages(images);
            return photoAlbum;
        } catch (Exception e) {
            System.out.println("获取图片失败！！！" + photoAlbum);
            return photoAlbum;
        }
    }

    /**
     * 下载图片
     *
     * @param imageAndFile
     */
    public void downLoadImage(ImageAndFile imageAndFile) {
        try {
            Connection.Response res = Jsoup.connect(imageAndFile.getUrl())
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (ArchLinux Linux 3.16) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.0 Chrome/30.0.1599.101 Safari/537.36")
                    .method(Connection.Method.GET)
                    .ignoreContentType(true)
                    .execute();
            byte[] image = res.bodyAsBytes();
            File file = new File(imageAndFile.getFilename());
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            FileImageOutputStream outputStream = new FileImageOutputStream(file);
            outputStream.write(image);
        } catch (Exception e) {
            System.out.println("下载图片失败！！！" + imageAndFile);
        }
    }

    /**
     * 整理一个相册的图片地址
     *
     * @param photoAlbum 相册
     * @return
     */
    public List<ImageAndFile> downloadImagesByPhotoAlbum(PhotoAlbum photoAlbum) {
        List<ImageAndFile> urls = new ArrayList<>();
        String title = photoAlbum.getTitle();
        String category = null;
        if (title.indexOf("[") != -1 || title.lastIndexOf("]") != -1) {
            category = title.substring(title.indexOf("[") + 1, title.lastIndexOf("]"));
        } else {
            category = photoAlbum.getCreateTime();
        }

        String url = photoAlbum.getMainImg();
        ImageAndFile imageAndFile = new ImageAndFile(url, "./GirlAtlas/" + category + "/" + title + "/index.jpg");
        urls.add(imageAndFile);
        List<Image> images = photoAlbum.getImages();
        for (Image image : images) {
            StringBuffer str = new StringBuffer("./GirlAtlas/");
            str.append(category);
            str.append("/");
            str.append(title);
            str.append("/");
            str.append(image.getIndex());
            str.append(".jpg");
            ImageAndFile imageFile = new ImageAndFile(image.getUrl(), str.toString());
            urls.add(imageFile);
        }
        System.out.println("文件下载地址与保存位置设置完成!"+photoAlbum);
        return urls;
    }
}
