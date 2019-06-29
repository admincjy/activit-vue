package com.wl.wlp2ploansystem.infrastructures.common.ueditor;


/***
 * UEditor 配置器
 */
public interface UEditorConfigurer {
    /***
     * 获取自定义允许上传的附件类型，覆盖配置文件中fileAllowFiles的设置
     * @return
     */
    String[] getUploadFileAllowFiles();
}
