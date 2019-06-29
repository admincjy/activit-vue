package com.wl.wlp2ploansystem.infrastructures.common.templateExport;

import com.aspose.words.IMailMergeDataSource;

import java.util.List;

/**
 * 模板导出接口
 */
public interface TemplateExportStrategy {

    /**
     * word pdf 导出
     * @param templateFilePath 模板文件路径
     * @param dataSource 数据源
     * @return 文件内容
     * @throws Exception 错误信息
     */
    byte[] exportWord(String templateFilePath, List<IMailMergeDataSource> dataSource) throws Exception;

}
