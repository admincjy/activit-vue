package com.wl.wlp2ploansystem.loansubsystem.servicesimpl.templateExport;

import com.aspose.words.IMailMergeDataSource;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.support.BeanHelper;
import com.wl.wlp2ploansystem.infrastructures.common.support.SpringContextUtil;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterAssetInformation;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanEnterAssetInformationService;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanEnterService;

import java.util.List;
import java.util.Map;


public class PL_LoanEnterTemplateExportDataSource implements IMailMergeDataSource {

    int repeatCount = 1;

    private Map headerEntity;
    private List<PL_LoanEnterAssetInformation> assetInformationList;


   public PL_LoanEnterTemplateExportDataSource(String loanDocId){
       PL_LoanEnterService enterService = SpringContextUtil.getBean(PL_LoanEnterService.class);
       Object data = enterService.get(loanDocId);

       headerEntity = BeanHelper.objectToMap(data);

       PL_LoanEnterAssetInformationService assetInformationService = SpringContextUtil.getBean(PL_LoanEnterAssetInformationService.class);
       EntityWrapper<PL_LoanEnterAssetInformation> ew = new EntityWrapper<PL_LoanEnterAssetInformation>();

       ew.eq("loanDocId",loanDocId);
       ew.orderBy("id", true);

       assetInformationList  = assetInformationService.getList(ew);

    }
    @Override
    public String getTableName() throws Exception {
        return "lc";
    }

    @Override
    public boolean moveNext() throws Exception {
        if (repeatCount > 0) {
            repeatCount--;
            return true;
        }
        return false;
    }

    @Override
    public boolean getValue(String key, Object[] objects) throws Exception {

        if(objects != null && objects.length > 0) {
            objects[0] = this.headerEntity.get(key);
            return true;
        } else {
            return false;
        }

    }

    @Override
    public IMailMergeDataSource getChildDataSource(String tableName) throws Exception {
        if (tableName.equals("lrs")) {
            return new PL_LoanEnterAssetInformationTemplateExportDataSource(assetInformationList);
        }
        return null;
    }
}
