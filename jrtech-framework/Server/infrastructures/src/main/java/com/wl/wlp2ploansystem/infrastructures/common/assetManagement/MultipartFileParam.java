package com.wl.wlp2ploansystem.infrastructures.common.assetManagement;

import org.apache.commons.fileupload.FileItem;

import java.util.HashMap;

/**
 * 分段文件上传参数
 */
public class MultipartFileParam {

    /**
     * 该请求是否是multipart
     */
    private boolean isMultipart;

    /**
     * 附件类别id
     */
    private String attachmentCategoryId;

    /**
     * 任务ID
     */
    private String id;

    /**
     * 总分片数量
     */
    private int chunks = 1;

     /**
     * 当前为第几块分片
     */
    private int chunk = 0;

     /**
     * 当前分片大小
     */
    private long size = 0L;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 分片对象
     */
    private FileItem fileItem;

    /**
     * 请求中附带的自定义参数
     */
    private HashMap<String, String> param = new HashMap<>();

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getBusinessDocId() {
        return businessDocId;
    }

    public void setBusinessDocId(String businessDocId) {
        this.businessDocId = businessDocId;
    }

    public String getAttachmentCategoryId() {
        return attachmentCategoryId;
    }

    public void setAttachmentCategoryId(String attachmentCategoryId) {
        this.attachmentCategoryId = attachmentCategoryId;
    }

    //文件ID
    private String fid;

    //业务单据id
    private String businessDocId;


    //是否允许上传多文件
    private Boolean multiple = Boolean.TRUE;



    public boolean isMultipart() {
        return isMultipart;
    }

    public void setMultipart(boolean multipart) {
        isMultipart = multipart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getChunks() {
        return chunks;
    }

    public void setChunks(int chunks) {
        this.chunks = chunks;
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getFileName() {
        return fileName;
    }
    public String getFileExtension(){

        return this.fileName.substring(fileName.lastIndexOf(".")+1);
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public FileItem getFileItem() {
        return fileItem;
    }

    public void setFileItem(FileItem fileItem) {
        this.fileItem = fileItem;
    }

    public HashMap<String, String> getParam() {
        return param;
    }

    public void setParam(HashMap<String, String> param) {
        this.param = param;
    }

    public Boolean getMultiple() {
        return multiple;
    }

    public void setMultiple(Boolean multiple) {
        this.multiple = multiple;
    }

}