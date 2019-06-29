package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.wl.wlp2ploansystem.infrastructures.common.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


@RestController
public class Base_ExcelExportManagementController extends HttpServlet {

    @Autowired
    ApplicationProperties applicationProperties;

    @GetMapping("/authapi/base_ExcelExportManagement/downloadTemplate")
    @PreAuthorize("hasAuthority('common')")
    public void downloadTemplate(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String fileName = request.getParameter("f");

        File file = ResourceUtils.getFile("classpath:exceltemplate/"+fileName);

        String userAgent = request.getHeader("User-Agent");

        // 针对IE或者以IE为内核的浏览器：
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } else {
            // 非IE浏览器的处理：
            fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
        }

        InputStream fis = new BufferedInputStream(new FileInputStream(file));
        response.reset();
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
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
}
