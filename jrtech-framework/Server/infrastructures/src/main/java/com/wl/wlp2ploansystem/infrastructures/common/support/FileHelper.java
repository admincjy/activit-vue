package com.wl.wlp2ploansystem.infrastructures.common.support;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 *文件操作工具类
 */
public class FileHelper {


    /**
     *路径合并
     */
    public  static String combarePath(String... paths) {

        Collection<String> innPaths = new ArrayList<String>();

        for (String path : paths){
            String innerPath = path;
            if(path.startsWith(File.separator)){
                innerPath = innerPath.substring(1,path.length());
            }
            if(path.endsWith(File.separator)){
                innerPath = innerPath.substring(0,path.length()-1);
            }

            innPaths.add(innerPath);
        }
        return String.join(File.separator, innPaths);
    }
    public  static String combarePath(Collection<String> paths) {
        return String.join(File.separator, paths);
    }
    /**
     * 创建指定的目录。
     * 如果指定的目录的父目录不存在则创建其目录书上所有需要的父目录。
     * <b>注意：可能会在返回false的时候创建部分父目录。</b>
     * @param fileName 要创建的目录的目录名
     * @return 完全创建成功时返回true，否则返回false。
     * @since  1.0
     */
    public static boolean makeDirectory(String fileName) {
        File file = new File(fileName);
        if(file.exists()){
            return true;
        }
        return file.mkdirs();
    }
    /**
     * 清空指定目录中的文件。
     * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
     * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
     * @param directory 要清空的目录
     * @return 目录下的所有文件都被成功删除时返回true，否则返回false.
     * @since  1.0
     */
    public static boolean emptyDirectory(File directory) {
        boolean result = true;
        File[] entries = directory.listFiles();
        if(entries == null){
            return  true;
        }
        for (int i = 0; i < entries.length; i++) {
            if (!entries[i].delete()) {
                result = false;
            }
        }
        return result;
    }

    /**
     * 清空指定目录中的文件。
     * 这个方法将尽可能删除所有的文件，但是只要有一个文件没有被删除都会返回false。
     * 另外这个方法不会迭代删除，即不会删除子目录及其内容。
     * @param directoryName 要清空的目录的目录名
     * @return 目录下的所有文件都被成功删除时返回true，否则返回false。
     * @since  1.0
     */
    public static boolean emptyDirectory(String directoryName) {
        File dir = new File(directoryName);
        if(!dir.exists()){
            return  true;
        }
        return emptyDirectory(dir);
    }

    /**
     * 删除指定目录及其中的所有内容。
     * @param dirName 要删除的目录的目录名
     * @return 删除成功时返回true，否则返回false。
     * @since  1.0
     */
    public static boolean deleteDirectory(String dirName) {
        return deleteDirectory(new File(dirName));
    }

    /**
     * 删除指定目录及其中的所有内容。
     * @param dir 要删除的目录
     * @return 删除成功时返回true，否则返回false。
     * @since  1.0
     */
    public static boolean deleteDirectory(File dir) {
        if ( (dir == null) || !dir.isDirectory()) {
            throw new IllegalArgumentException("Argument " + dir +
                    " is not a directory. ");
        }

        File[] entries = dir.listFiles();
        int sz = entries.length;

        for (int i = 0; i < sz; i++) {
            if (entries[i].isDirectory()) {
                if (!deleteDirectory(entries[i])) {
                    return false;
                }
            }
            else {
                if (!entries[i].delete()) {
                    return false;
                }
            }
        }

        if (!dir.delete()) {
            return false;
        }
        return true;
    }


    /**
     * 列出目录中的所有内容，包括其子目录中的内容。
     * @param file 要列出的目录
     * @param filter 过滤器
     * @return 目录内容的文件数组。
     * @since  1.0
     */
    public static File[] listAll(File file) {

        return  file.listFiles();
    }


    /**
     * 从文件路径得到文件名。
     * @param filePath 文件的路径，可以是相对路径也可以是绝对路径
     * @return 对应的文件名
     * @since  1.0
     */
    public static String getFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }


    public static String getFileNameNotSuffix(String filePath) {
        String fileName = getFileName(filePath);
        return  fileName.substring(0,fileName.lastIndexOf("."));
    }
    /**
     * @param filePath
     * @return 文件后缀名
     */
    public static String getFileSuffix(String filePath) {
        String suffix = filePath.substring(filePath.lastIndexOf(".") + 1);

        return  suffix;
    }

    /**
     * 从文件名得到文件绝对路径。
     * @param fileName 文件名
     * @return 对应的文件路径
     * @since  1.0
     */
    public static String getFilePath(String fileName) {
        File file = new File(fileName);
        return file.getAbsolutePath();
    }
    public static String getParentFile(String fileName) {
        File file = new File(fileName);
        return file.getParent();
    }
    public static boolean deleteFile(String path) {
        if (StringUtils.isEmpty(path)){
            throw new IllegalArgumentException("Argument " + path +
                    " is not a file. ");
        }

        File file = new File(path);
        return file.delete();

    }
    public static void saveFileFromInputStream(InputStream stream, String path) throws IOException
    {
        FileOutputStream fs=new FileOutputStream(path);
        byte[] buffer =new byte[1024*1024];
        int bytesum = 0;
        int byteread = 0;
        while ((byteread=stream.read(buffer))!=-1)
        {
            bytesum+=byteread;
            fs.write(buffer,0,byteread);
            fs.flush();
        }
        fs.close();
        stream.close();
    }
    public  static  byte[] getFileContent(String realPath) throws  IOException{

        String encoding = "UTF-8";
        File file = new File(realPath);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];

        FileInputStream in = new FileInputStream(file);
        in.read(filecontent);
        in.close();

        return filecontent;
    }

    /**
     * 根据字节流压缩文件
     * @param filePaths
     * @throws IOException
     */
    public static void batchZip(Map<byte[],String> filePaths, String zipname) throws IOException {

		try {
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipname));
			zos.setEncoding("GBK");
			//循环读取文件路径集合，获取每一个文件的路径
			for(byte[] key : filePaths.keySet()){
				//创建输入流读取文件
				BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(key));

				//将文件写入zip内，即将文件进行打包
				zos.putNextEntry(new ZipEntry(filePaths.get(key)));

				//写入文件的方法，同上
				int size = 0;
				byte[] buffer = new byte[1024];  //设置读取数据缓存大小
				while ((size = bis.read(buffer)) > 0) {
					zos.write(buffer, 0, size);
				}
				bis.close();
			}
				//关闭输入输出流
			zos.flush();
			zos.close();
        } catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * 根据文件夹压缩文件
     * @param sourcePath 需要压缩的文件路径
	 * @param zipPath 压缩后的路径
     * @throws IOException
     */
    public static void fileToZip(String sourcePath, String zipPath){
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(zipPath);
            zos = new ZipOutputStream(fos);
            zos.setEncoding("gbk");//此处修改字节码方式。
            //createXmlFile(sourcePath,"293.xml");
            writeZip(new File(sourcePath), "", zos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private static void writeZip(File file, String parentPath, ZipOutputStream zos) {
        if(file.exists()){
            if(file.isDirectory()){//处理文件夹
                parentPath+=file.getName()+File.separator;
                File [] files=file.listFiles();
                if(files.length != 0)
                {
                    for(File f:files){
                        writeZip(f, parentPath, zos);
                    }
                }
                else
                {       //空目录则创建当前目录
                        try {
                            zos.putNextEntry(new ZipEntry(parentPath));
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                }
            }else{
                FileInputStream fis=null;
                try {
                    fis=new FileInputStream(file);
                    ZipEntry ze = new ZipEntry(parentPath + file.getName());
                    zos.putNextEntry(ze);
                    byte [] content=new byte[1024];
                    int len;
                    while((len=fis.read(content))!=-1){
                        zos.write(content,0,len);
                        zos.flush();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    try {
                        if(fis!=null){
                            fis.close();
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 清空文件和文件目录
     *
     */
    public static boolean delAllFile(String path) throws Exception {
    	boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
         	return flag;
        }
        if (!file.isDirectory()) {
         	return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
          	if (path.endsWith(File.separator)) {
             	temp = new File(path + tempList[i]);
          	} else {
              	temp = new File(path + File.separator + tempList[i]);
          	}
          	if (temp.isFile()) {
             	temp.delete();
          	}
          	if (temp.isDirectory()) {
             	delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
             	delFolder(path + "/" + tempList[i]);//再删除空文件夹
             	flag = true;
          	}
       	}
       	return flag;
    }

    public static void delFolder(String folderPath) {
		 try {
			delAllFile(folderPath); //删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); //删除空文件夹
		 } catch (Exception e) {
		   e.printStackTrace();
		 }
	}

}
