package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.infrastructures.common.support.ITreeObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_data_dictionary_category")
public class Base_DataDictionaryCategory extends FullAuditedEntity implements ITreeObject {

    @Display("父编号")
    @NotBlank
    public String parentId;

    @Display("名称")
    @NotBlank
    private String name;

    private String remark;

    private Boolean visible;

    @Override
    public String getNodeType() {
        return "Base_DataDictionaryCategory";
    }
}
