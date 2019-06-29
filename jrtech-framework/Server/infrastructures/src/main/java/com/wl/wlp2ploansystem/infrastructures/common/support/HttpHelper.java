package com.wl.wlp2ploansystem.infrastructures.common.support;

import com.wl.wlp2ploansystem.infrastructures.common.assetManagement.MultipartFileParam;
import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
/**
 *Http操作工具类
 */
public class HttpHelper {

	/**
	 * 读取 Cookie
	 */
	public static Map<String, String> readCookieMap(HttpServletRequest request) {
		Map<String, String> cookieMap = new HashMap<String, String>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie.getValue());
			}
		}
		return cookieMap;
	}
	/**
	 * 是否Ajax请求
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {

		String header = request.getHeader("X-Requested-With");
		boolean isAjax = "XMLHttpRequest".equals(header) ? true : false;
		return isAjax;
	}
	/**
	 * Http请求返回是否是Json
	 */
	public static boolean isResponseJson(HttpServletRequest request) {

		String requestAccept = request.getHeader("Accept");
		if (!StringUtils.isEmpty(requestAccept) && requestAccept.indexOf("application/json") >= 0) {
			return true;
		}
		return false;
	}
	/**
	 * 多文件上传
	 * @param  saveFolderPath 文件保存目录
	 */
	public  static void  multipartFileUpload(HttpServletRequest request,MultipartFileParam param,String fileAllowedExtentionValues,String saveFolderPath, Consumer<String> uploadCompleted) throws Exception{

		String fileExten = param.getFileExtension();

		if(fileAllowedExtentionValues !=null){
			List<String> fileAllowedExtenList = new ArrayList<>();
			fileAllowedExtenList.addAll(Arrays.asList(fileAllowedExtentionValues.toLowerCase().split(",")));
			if (fileAllowedExtenList.size() != -0 && !fileAllowedExtenList.contains("." + fileExten.toLowerCase())) {
				throw new UserFriendlyException("您上传的文件类型不被允许！");
			}
		}

		String fid = param.getFid();
		//在文件要保存的目录中建立年/月/日文件夹
		LocalDateTime now = LocalDateTime.now();
		String finalDirPath = FileHelper.combarePath(saveFolderPath
				,String.valueOf(now.getYear())
				,String.valueOf(now.getMonth().getValue())
				,String.valueOf(now.getDayOfMonth()));

		String tempDirPath = FileHelper.combarePath(finalDirPath,"temp", fid) ;
		File tmpDir = new File(tempDirPath);
		if (!tmpDir.exists()) {
			tmpDir.mkdirs();
		}
		int existFileCount = tmpDir.list().length;
		//创建文件
		File  tempfile = new File(tempDirPath, String.valueOf(param.getChunk()));
		//copy
		FileUtils.copyInputStreamToFile(param.getFileItem().getInputStream(), tempfile);

		if(existFileCount + 1 == param.getChunks()){

			System.out.println("fid:"+fid);

			String  newFilePath = FileHelper.combarePath(finalDirPath ,fid+'.' + param.getFileExtension());
			/**
			 * 进行文件合并
			 */
			File newFile = new File(newFilePath);
			if(newFile.exists()){
				newFile.delete();
			}
			FileOutputStream outputStream = new FileOutputStream(newFile, true);//文件追加写入

			byte[] byt = new byte[10*1024*1024];
			int len;
			FileInputStream temp = null;//分片文件
			for(int i = 0 ; i<param.getChunks() ; i++){
				temp = new FileInputStream(new File(tempDirPath ,String.valueOf(i)));
				while((len = temp.read(byt))!=-1){
					System.out.println(len);
					outputStream.write(byt, 0, len);
				}
			}
			/**
			 * 当所有追加写入都写完  才可以关闭流
			 */
			outputStream.close();
			temp.close();

			FileHelper.deleteDirectory(tmpDir);
			uploadCompleted.accept(newFilePath);
		}
	}

	/**
	 * 文件下载
	 * @param  filename 文件名称
	 * @param  filepath 文件路径
	 */
	public static  void download(HttpServletRequest request, HttpServletResponse response,String filename,String  filepath)  throws Exception{
		File file = new File(filepath);
		String userAgent = request.getHeader("User-Agent");

		// 针对IE或者以IE为内核的浏览器：
		if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
			filename = java.net.URLEncoder.encode(filename, "UTF-8");
		} else {
			// 非IE浏览器的处理：
			filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		}
		InputStream fis = new BufferedInputStream(new FileInputStream(file));
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename=" + filename);
		response.addHeader("Content-Length", "" + file.length());
		OutputStream out = new BufferedOutputStream(response.getOutputStream());
		response.setContentType("text/html; charset=utf-8");
		byte[] buffer = new byte[1024 * 1024 * 10];
		int i = -1;
		while ((i = fis.read(buffer)) != -1) {
			out.write(buffer, 0, i);

		}
		fis.close();
		out.flush();
		out.close();
	}

	/**
	 * 文件下载
	 * @param  filename 文件名称
	 * @param  content 文件内容
	 */
	public static  void download(HttpServletRequest request, HttpServletResponse response,String filename,byte[] content)  throws Exception{
		String userAgent = request.getHeader("User-Agent");

		// 针对IE或者以IE为内核的浏览器：
		if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
			filename = java.net.URLEncoder.encode(filename, "UTF-8");
		} else {
			// 非IE浏览器的处理：
			filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
		}

		InputStream fis = new BufferedInputStream(new ByteArrayInputStream(content));
		response.reset();
		response.addHeader("Content-Disposition", "attachment;filename=" + filename);
		response.addHeader("Content-Length", "" + content.length);
		OutputStream out = new BufferedOutputStream(response.getOutputStream());
		response.setContentType("text/html; charset=utf-8");
		byte[] buffer = new byte[1024 * 1024 * 10];
		int i = -1;
		while ((i = fis.read(buffer)) != -1) {
			out.write(buffer, 0, i);

		}
		fis.close();
		out.flush();
		out.close();
	}

}
