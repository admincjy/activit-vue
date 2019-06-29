package com.wl.wlp2ploansystem.infrastructures.workflow.vo;

import com.wl.wlp2ploansystem.infrastructures.common.support.JsonHelper;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode()
public class WF_BusinessKey {


    public WF_BusinessKey(String originator,String departmentId,String id, String folio) {
        this.originator = originator;
        this.departmentId = departmentId;
        this.id = id;
        this.folio = folio;
    }
    public WF_BusinessKey() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public  static WF_BusinessKey of(String json){
        return  (WF_BusinessKey) JsonHelper.json2Object(json,WF_BusinessKey.class);
    }

    @Override
    public String toString() {
        return  JsonHelper.object2Json(this);
    }

    private String originator;
    private  String departmentId;
    private  String id;
    private  String folio;


}
