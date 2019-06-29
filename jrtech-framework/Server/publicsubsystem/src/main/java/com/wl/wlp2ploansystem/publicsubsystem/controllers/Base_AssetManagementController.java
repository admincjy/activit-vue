package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.ApplicationProperties;
import com.wl.wlp2ploansystem.infrastructures.common.assetManagement.MultipartFileParam;
import com.wl.wlp2ploansystem.infrastructures.common.assetManagement.MultipartFileUploadUtils;
import com.wl.wlp2ploansystem.infrastructures.common.support.*;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Attachment;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_AttachmentService;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_SettingService;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.Base_AttachmentCategoryTreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.services.dtos.Base_AttachmentClassificationIdsWithBusinessDocIdDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.activation.FileTypeMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class Base_AssetManagementController extends HttpServlet {

    @Autowired
    ApplicationProperties applicationProperties;

    @Autowired
    Base_AttachmentService attachmentService;

    @Autowired
    Base_SettingService settingService;


    @PostMapping("/authapi/base_AssetManagement/upload")
    @PreAuthorize("hasAuthority('common')")
    public void upload(HttpServletRequest request) throws Exception {
        MultipartFileParam param = MultipartFileUploadUtils.parse(request);
        if (!param.isMultipart()) {
            return;
        }
        String fileAllowedExtentionValues = settingService.getStringValue("Attachment_Accept_Extensions");

        String globalDirPath = applicationProperties.getFileupload().getFilePath();
        String fileExten = param.getFileExtension();
        String fid = param.getFid();
        String businessDocId= param.getBusinessDocId();
        String attachmentCategoryId= param.getAttachmentCategoryId();
        String fileName = param.getFileName();
        boolean multiple = param.getMultiple().booleanValue();
        HttpHelper.multipartFileUpload(request,param,fileAllowedExtentionValues,globalDirPath,p->{
            File newFile = new File(p);

            Base_Attachment fielModel = new Base_Attachment();
            fielModel.setId(fid);
            fielModel.setName(fileName);
            fielModel.setAttachmentCategoryId(attachmentCategoryId);
            fielModel.setBusinessDocId(businessDocId);
            fielModel.setDownloadCount(0);
            fielModel.setFileSize(newFile.length());
            fielModel.setFileType(fileExten);
            fielModel.setPath(p);
            if(multiple) {
                attachmentService.save(fielModel);
            }
            else {
                attachmentService.saveSingle(fielModel);
            }
        });
    }

    @GetMapping("/authapi/base_AssetManagement/download")
    @PreAuthorize("hasAuthority('common')")
    public void download(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String id = request.getParameter("id");
        Base_Attachment attachmentModel = attachmentService.get(id);
        String filename = attachmentModel.getName();

        HttpHelper.download(request,response,filename,attachmentModel.getPath());

    }

    @GetMapping("/authapi/base_AssetManagement/view")
    @PreAuthorize("hasAuthority('common')")
    public void view(HttpServletRequest request,HttpServletResponse response) throws Exception {

        String id = request.getParameter("id");
        String thumbnails = request.getParameter("tb");
        Base_Attachment attachmentModel = attachmentService.get(id);
        String filePath = attachmentModel.getPath();
        File file = null;

        if(StringUtils.isEmpty(thumbnails) || !ImageHelper.isImageType(filePath)){
            file = new File(filePath);
        }
        else{
            String thumbnailsPath = FileHelper.combarePath(FileHelper.getParentFile(filePath),FileHelper.getFileNameNotSuffix(filePath) +"_thumbnails." + FileHelper.getFileSuffix(filePath));

            file = new File(thumbnailsPath);
            if(!file.exists()){
                ImageHelper.ImageThumb(filePath,thumbnailsPath,300,300);
            }
        }
        InputStream fis = new BufferedInputStream(new FileInputStream(file));
        response.reset();
        response.addHeader("Content-Length", "" + file.length());
        OutputStream out = new BufferedOutputStream(response.getOutputStream());
        if("pdf".equals(FileHelper.getFileSuffix(attachmentModel.getPath()))){
            response.setHeader("Content-type","application/pdf");
        }
        else {
            String contentType = FileTypeMap.getDefaultFileTypeMap().getContentType(attachmentModel.getPath());
            response.setHeader("Content-type",contentType);
        }
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
	 * 一键下载附件
	 * @param request
	 * @param response
	 * @throws Exception
	 */
    @RequestMapping(value = "/authapi/base_AssetManagement/batchDownload")
    @PreAuthorize("hasAuthority('common')")
    public void batchDownload(HttpServletRequest request,HttpServletResponse response)  throws Exception {
    	String assetCategoryClassifications = request.getParameter("assetCategoryClassifications");
    	String businessDocId = request.getParameter("businessDocId");
    	String customername = request.getParameter("customername");
    	if(StringUtils.isEmpty(customername)){
    		customername = "";
		}
    	String basePath = FileHelper.combarePath(applicationProperties.getFileupload().getFilePath() ,"download",businessDocId);
    	String globalDirPath = FileHelper.combarePath(basePath, customername + "资料包");
    	String downloadzipname = customername + "资料包.zip";
        String zipFilePath = FileHelper.combarePath(basePath, downloadzipname);

    	String[] assetCategoryarr = assetCategoryClassifications.split(",");
    	Base_AttachmentClassificationIdsWithBusinessDocIdDto input = new Base_AttachmentClassificationIdsWithBusinessDocIdDto();
    	input.setBusinessDocId(businessDocId);
    	input.setCagegoryClassificationIds(Arrays.asList(assetCategoryarr));
        Collection<TreeObject<Base_AttachmentCategoryTreeObject>> attachementtree = attachmentService.getCategoriesTreeWithClassificationId(input);

        List<String> allAttachmentCategoryIds =new ArrayList<>();
        TreeService<Base_AttachmentCategoryTreeObject> attachmentCategoryTreeObjectTreeService = new TreeService<Base_AttachmentCategoryTreeObject>();
        attachmentCategoryTreeObjectTreeService.of(attachementtree);
        attachmentCategoryTreeObjectTreeService.retriveTree(p->{
            allAttachmentCategoryIds.add(p.getId());
        });
        EntityWrapper<Base_Attachment> ew = new EntityWrapper<Base_Attachment>();
        ew.in("attachmentCategoryId",allAttachmentCategoryIds);
        ew.eq("businessDocId",businessDocId);
        ew.orderBy("id", false);
        Collection<Base_Attachment> allAttachments = attachmentService.getList(ew);

        FileHelper.emptyDirectory(globalDirPath);
        FileHelper.deleteFile(zipFilePath);

        // 遍历附件树，创建文件夹和文件
        attachmentCategoryTreeObjectTreeService.retriveTree(tree->{
            ArrayList<String> treePathList = new ArrayList<String>(Arrays.asList(tree.getPath().split(";")));

            if(treePathList.size()>0 && treePathList.get(0).equals(".")){
                treePathList.set(0,globalDirPath);
            }
            String filepath = FileHelper.combarePath(treePathList);

            File categoryDir = new File(filepath);
            if (!categoryDir.exists()) {
                categoryDir.mkdirs();
            }
            Collection<Base_Attachment> attachments = allAttachments.stream().filter(p->p.getAttachmentCategoryId().equals(tree.getId())).collect(Collectors.toList());
            for(Base_Attachment attachmentModel : attachments){
                File  oldFile  = new File(attachmentModel.getPath());
                File newFile  = new File(FileHelper.combarePath(filepath,attachmentModel.getName()));

                try {
                    FileUtils.copyFile(oldFile,newFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
		// 创建压缩文件
		FileHelper.fileToZip(globalDirPath,zipFilePath);
        HttpHelper.download(request,response,downloadzipname,zipFilePath);
    }
}
