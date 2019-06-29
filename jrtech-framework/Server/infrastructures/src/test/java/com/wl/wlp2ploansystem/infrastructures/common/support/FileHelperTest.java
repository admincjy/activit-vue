package com.wl.wlp2ploansystem.infrastructures.common.support;

import org.junit.Test;

public class FileHelperTest {
    @Test
    public void getFileSuffix() throws Exception {

        String filePath = "D:\\fileupload\\files\\2018\\OCTOBER\\23\\1054558530473672706.jpg";
        String thumbnailsPath = FileHelper.combarePath(FileHelper.getParentFile(filePath),FileHelper.getFileNameNotSuffix(filePath) +"_thumbnails." + FileHelper.getFileSuffix(filePath));

        System.out.println(thumbnailsPath);
    }

}