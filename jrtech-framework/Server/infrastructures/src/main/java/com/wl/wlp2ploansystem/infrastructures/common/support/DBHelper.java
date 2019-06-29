package com.wl.wlp2ploansystem.infrastructures.common.support;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;

/**
 *数据库操作工具类
 */
public class DBHelper {

    private static final Logger logger = LoggerFactory.getLogger(DBHelper.class);
    /**
     * Java代码实现MySQL数据库导出
     *
     * @author GaoHuanjie
     * @param hostIP MySQL数据库所在服务器地址IP
     * @param userName 进入数据库所需要的用户名
     * @param password 进入数据库所需要的密码
     * @param savePath 数据库导出文件保存路径
     * @param fileName 数据库导出文件文件名
     * @param databaseName 要导出的数据库名
     * @return 返回true表示导出成功，否则返回false。
     */
    public static void bak(String script, String hostIP, String port, String userName, String password, String savePath, String fileName, String databaseName) throws IOException, InterruptedException {
        File saveFile = new File(savePath);
        if (!saveFile.exists()) {// 如果目录不存在
            saveFile.mkdirs();// 创建文件夹
        }
        String saveFilePath = FileHelper.combarePath(savePath, fileName);
        File saveFileFile = new File(saveFilePath);

        if(!saveFileFile.exists()){
            saveFileFile.createNewFile();
        }

        String newScipt = MessageFormat.format(script, hostIP, port, databaseName);

        String[] command = newScipt.split("\\s+");
        ProcessBuilder processBuilder = new ProcessBuilder(Arrays.asList(command));
        processBuilder.redirectErrorStream(true); // merge stdout, stderr of process
        Process process = processBuilder.start();
        InputStreamReader isr = new  InputStreamReader(process.getInputStream());
        BufferedReader br = new BufferedReader(isr);

        StringBuilder builder = new StringBuilder();
        String lineRead;
        while ((lineRead = br.readLine()) != null) {
            FileUtils.writeLines(saveFileFile,"UTF-8", Collections.singletonList(lineRead),true);
            builder.append(lineRead);
        }
        int processWaitFor = process.waitFor();
        if (processWaitFor != 0) {// 0 表示线程正常终止。
            throw  new RuntimeException("数据库备份失败，waitFor："+ processWaitFor +";str;"+builder.toString());
        }

        process.destroy();
    }
}
