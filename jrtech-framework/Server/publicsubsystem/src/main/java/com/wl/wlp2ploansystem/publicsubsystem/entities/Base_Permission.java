package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.infrastructures.common.support.ITreeObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_permission")
public class Base_Permission extends FullAuditedEntity implements ITreeObject {

    /***
     * 登陆用户基础权限
     */
    public static final String Base_Permission_Common = "common";

    @Display("父编号")
    private String parentId;

    @Display("名称")
    @NotBlank
    private String name;

    private String remark;

    private Integer sortIndex;

    @Override
    public String getNodeType() {
        return "Base_Permission_Entity";
    }

}
