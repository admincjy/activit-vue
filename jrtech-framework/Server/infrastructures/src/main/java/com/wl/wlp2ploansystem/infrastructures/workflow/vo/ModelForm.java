package com.wl.wlp2ploansystem.infrastructures.workflow.vo;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import org.activiti.engine.repository.Model;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@EqualsAndHashCode()
public class ModelForm {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private  String id;
    private String category;
    @NotBlank
    private String name;
    @NotBlank
    private String key;
    @NotBlank
    private String desc;

    private Date createTime;
    private Date lastUpdateTime;
    private Integer version;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }




    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ModelForm(){

    }
    public ModelForm(Model m) throws  Exception{
        this.key =m.getKey();
        this.name = m.getName();
        this.category = m.getCategory();
        this.createTime = m.getCreateTime();
        this.lastUpdateTime = m.getLastUpdateTime();
        this.version = m.getVersion();
        this.id = m.getId();

        if(!StringUtils.isEmpty(m.getMetaInfo())) {
            JsonNode responseJSON = new ObjectMapper().readTree(m.getMetaInfo());
            JsonNode descJSON = responseJSON.get("description");
            if(descJSON !=null) {
                this.desc = descJSON.textValue();
            }
        }


    }
}
