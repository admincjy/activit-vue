package com.wl.wlp2ploansystem.loansubsystem.servicesimpl.templateExport;

import com.aspose.words.IMailMergeDataSource;
import com.wl.wlp2ploansystem.infrastructures.common.support.BeanHelper;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanEnterAssetInformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class PL_LoanEnterAssetInformationTemplateExportDataSource implements IMailMergeDataSource {

    private List<Map> dataList = new ArrayList<>();

    private int index = -1;

    public PL_LoanEnterAssetInformationTemplateExportDataSource(List<PL_LoanEnterAssetInformation>  recoverySchedules ){

        dataList = recoverySchedules.stream().map(p->{
            return BeanHelper.objectToMap(p);
        }).collect(Collectors.toList());

    }
    /**
     * 获取结果集总数
     * @return
     */
    private int getCount() {
        return this.dataList.size();
    }


    @Override
    public String getTableName() throws Exception {
        return "lrc";
    }

    @Override
    public boolean moveNext() throws Exception {
        index += 1;
        if (index >= this.getCount()) {
            return false;
        }
        return true;

    }
    @Override
    public boolean getValue(String key, Object[] args) throws Exception {
        if(index < 0 || index >= this.getCount()) {
            return false;
        }
        if(args != null && args.length > 0) {
            args[0] = this.dataList.get(index).get(key);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public IMailMergeDataSource getChildDataSource(String s) throws Exception {
        return null;
    }
}
