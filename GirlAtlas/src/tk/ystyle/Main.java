package tk.ystyle;

import tk.ystyle.entity.ImageAndFile;
import tk.ystyle.entity.PhotoAlbum;
import tk.ystyle.service.GirlAtlasService;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        GirlAtlasService service = new GirlAtlasService();//实例化相册下载器
        service.login("lxy5266@live.com", "hhhhhhh", "on");//登陆
        List<String> urls = service.getAllPagesURL();//获得所有相册页面地址
        for (String url : urls) {
            System.out.println("正在下载页面中:"+url);
            List<PhotoAlbum> photoAlbums = service.getPhotoAlbums(url);//获得当前页面的相册
            for (PhotoAlbum photoAlbum : photoAlbums) {
                System.out.println("正在处理相册:"+photoAlbum);
                photoAlbum = service.getAPhotoAlbumImages(photoAlbum);//加载相册的图片
                List<ImageAndFile> imageAndFiles = service.downloadImagesByPhotoAlbum(photoAlbum);//整理下载连接及保存位置
                for (ImageAndFile file : imageAndFiles) {
                    System.out.println("正在下载图片:"+file);
                    service.downLoadImage(file);//下载图片
                }
                System.out.println();
            }
            System.out.println();
            System.out.println();
        }
    }
}
