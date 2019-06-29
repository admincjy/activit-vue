package com.wl.wlp2ploansystem.infrastructures.common.assetManagement;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* 分段文件上传工具函数
* */
public class MultipartFileUploadUtils {

        /**
         * 在HttpServletRequest中获取分段上传文件请求的信息
         * @param request
         * @return
         */
        public static MultipartFileParam parse(HttpServletRequest request) throws Exception {
            MultipartFileParam param = new MultipartFileParam();

            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            param.setMultipart(isMultipart);
            if(isMultipart){
                FileItemFactory factory = new DiskFileItemFactory();
                ServletFileUpload upload = new ServletFileUpload(factory);
                // 得到所有的表单域，它们目前都被当作FileItem
                List<FileItem> fileItems = upload.parseRequest(request);
                for (FileItem fileItem : fileItems) {
                    System.out.println("field name has:"+fileItem.getFieldName());
                    if (!"file".equals(fileItem.getFieldName())){
                        System.out.println("field val has:"+fileItem.getString());
                    }
                    if (fileItem.getFieldName().equals("fid")) {
                        param.setFid(fileItem.getString());
                    }
                    if (fileItem.getFieldName().equals("businessDocId")) {
                        param.setBusinessDocId(fileItem.getString());
                    }
                    if (fileItem.getFieldName().equals("attachmentCategoryId")) {
                        param.setAttachmentCategoryId(fileItem.getString());
                    }
                    if (fileItem.getFieldName().equals("multiple")) {
                        String strMultiple = fileItem.getString();
                        if(!StringUtils.isEmpty(strMultiple)){
                            param.setMultiple(Boolean.parseBoolean(strMultiple));
                        }
                    }
                    if (fileItem.getFieldName().equals("id")) {
                        param.setId(fileItem.getString());
                    } else if (fileItem.getFieldName().equals("name")) {
                        param.setFileName(new String(fileItem.getString().getBytes(
                                "ISO-8859-1"), "UTF-8"));
                    } else if (fileItem.getFieldName().equals("chunks")) {
                        param.setChunks(NumberUtils.toInt(fileItem.getString()));
                    } else if (fileItem.getFieldName().equals("chunk")) {
                        param.setChunk(NumberUtils.toInt(fileItem.getString()));
                    } else if (fileItem.getFieldName().equals("file")) {
                        param.setFileItem(fileItem);
                        param.setSize(fileItem.getSize());
                    } else{
                        param.getParam().put(fileItem.getFieldName(), fileItem.getString());
                    }
                }
            }

            return param;
        }


    }
