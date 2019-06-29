package com.wl.wlp2ploansystem.infrastructures.common.templateExport;

import com.aspose.words.*;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.util.List;

/**
 * 模板导出组件
 */

//https://blog.csdn.net/sinat_30276961/article/details/48372981
//字段格式化 https://blog.csdn.net/aroc_lo/article/details/39927573

@Component
public class TemplateExportSimpleStrategy implements TemplateExportStrategy {

    static {
        try {
            com.aspose.cells.License excellicense = new com.aspose.cells.License();
            String excelLicensePath = ResourceUtils.getFile("classpath:license/aspose-cells-8.5.2_license.xml").getPath();
            excellicense.setLicense(URLDecoder.decode(excelLicensePath, "UTF-8")); //这是加载Aspose.cells.lic

            com.aspose.words.License wordlicense = new com.aspose.words.License();
            String wordLicensePath = ResourceUtils.getFile("classpath:license/aspose-words-15.8.0_license.xml").getPath();
            wordlicense.setLicense(URLDecoder.decode(wordLicensePath, "UTF-8")); //这是加载Aspose.Words.lic


            if (System.getProperty("os.name").toLowerCase().indexOf("windows") == -1
                    && System.getProperty("os.name").toLowerCase()
                    .indexOf("mac") == -1) {// 在linux下加载字体库
                FontSettings.setFontsFolder("//usr//share//fonts", true);
            }
        } catch (Exception e) {
            //e.printStackTrace();
            //throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public byte[] exportWord(String templateFilePath, List<IMailMergeDataSource> dataSources) throws Exception {

        if (dataSources == null )
        {
            return null;
        }

        Document doc = new Document(templateFilePath);
        MailMerge mailMerge = doc.getMailMerge();
        for (IMailMergeDataSource dataSource : dataSources) {
            mailMerge.executeWithRegions(dataSource);
        }

        // 删除未使用的空白域
        mailMerge.deleteFields();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        doc.save(os,new OoxmlSaveOptions());

        return  os.toByteArray();
    }

}
