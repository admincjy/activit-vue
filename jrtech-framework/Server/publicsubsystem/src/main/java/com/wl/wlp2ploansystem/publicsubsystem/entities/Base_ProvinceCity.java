package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.wl.wlp2ploansystem.publicsubsystem.entities.FullAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import com.wl.wlp2ploansystem.infrastructures.common.support.ITreeObject;  


								
/**
 * 省市数据
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_province_city")
    public class Base_ProvinceCity extends FullAuditedEntity  implements ITreeObject  {
 /**
 * 父节点
 */
            @Size(max = 50)
            private String parentId;
       /**
 * 名称
 */
        @NotBlank
            @Size(max = 100)
            private String name;
                  	@Override
	public String getNodeType() {
		return "Base_ProvinceCity";
	}
}
