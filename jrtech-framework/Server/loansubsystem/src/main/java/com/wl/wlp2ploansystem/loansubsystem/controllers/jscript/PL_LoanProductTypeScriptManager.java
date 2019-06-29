package com.wl.wlp2ploansystem.loansubsystem.controllers.jscript;

import com.wl.wlp2ploansystem.infrastructures.common.jsscript.ScriptManager;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanProductSubType;
import com.wl.wlp2ploansystem.loansubsystem.entities.PL_LoanProductType;
import com.wl.wlp2ploansystem.loansubsystem.services.PL_LoanProductTypeService;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_DataDictionary;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_DataDictionaryCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class PL_LoanProductTypeScriptManager implements ScriptManager {

    @Autowired
    private PL_LoanProductTypeService service;

    public PL_LoanProductTypeScriptManager() {
    }
    @Override
    public String getScript() {
        Collection<PL_LoanProductType> productTypeList = service.getLoanProductTypeList();
        Collection<PL_LoanProductSubType> subProductTypeList = service.getLoanProductSubTypeList();
        StringBuilder script =new StringBuilder();

        script.append("(function(){ \n");
        script.append("    tapp.pl_loanproducttypes = [");
        for (PL_LoanProductType category : productTypeList) {
            script.append("{\n");
            script.append("            id: '" + category.getId() + "',\n");
            script.append("            name: '" + category.getName() + "',\n");

            Collection<PL_LoanProductSubType> categoryItems = subProductTypeList.stream()
                    .filter(p -> p.getLoanProductTypeId().equals(category.getId()))
                    .collect(Collectors.toList());

            if (categoryItems.size() <= 0) {
                script.append("items:[]\n");
            } else {
                script.append("items:[\n");
                categoryItems.forEach(sp -> {
                    script.append("{\n");
                    script.append("            id: '" + sp.getId() + "',\n");
                    script.append("            name: '" + sp.getName() + "',\n");
                    script.append("            loanProductTypeId: '" + sp.getLoanProductTypeId() + "',\n");
                    script.append("            formTemplateTypeId: '" + sp.getFormTemplateTypeId() + "',\n");
                    script.append("            returnMoneyMethodId: '" + sp.getReturnMoneyMethodId() + "',\n");
                    script.append("},\n");
                });
                script.append("]\n");
            }
            script.append("},\n");
        }
        script.append("]; \n");
        script.append("})();\n");
        return script.toString();
    }
}
