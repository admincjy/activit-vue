package com.wl.wlp2ploansystem.infrastructures.common.support;

import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


/**
 * 图片处理工具类
 */
public class ImageHelper {

    public static   boolean isImageType(String imagePath){
        File imgFile = new File(imagePath);
        String types = Arrays.toString(ImageIO.getReaderFormatNames());
        String suffix = null;
        // 获取图片后缀
        if(imgFile.getName().indexOf(".") > -1) {
            suffix = imgFile.getName().substring(imgFile.getName().lastIndexOf(".") + 1);
        }// 类型和图片后缀全部小写，然后判断后缀是否合法
        if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()) < 0){
            return  false;
        }
        return  true;
    }
    /**
     * 指定大小进行缩放
     * 若图片横比width小，高比height小，不变 若图片横比width小，高比height大，高缩小到height，图片比例不变
     * 若图片横比width大，高比height小，横缩小到width，图片比例不变
     * 若图片横比width大，高比height大，图片按比例缩小，横为width或高为height
     *
     * @param source
     *            输入源
     * @param output
     *            输出源
     * @param width
     *            宽
     * @param height
     *            高
     */
    public static void ImageThumb(String source, String output, int width, int height) throws IOException {
        Thumbnails.of(source).size(width, height).toFile(output);
    }
}
