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
 * 树形测试
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_tree_test")
    public class Base_TreeTest extends FullAuditedEntity  implements ITreeObject  {
 /**
 * 父菜单
 */
            @Size(max = 50)
            private String parentId;
       /**
 * 名称
 */
        @NotBlank
            @Size(max = 50)
            private String name;
       /**
 * 年龄
 */
        @NotNull
                private Integer age;
       /**
 * 月薪
 */
                private BigDecimal mobile;
       /**
 * 出生日期
 */
                private LocalDateTime birthday;
       /**
 * 是否在职
 */
                private Boolean inWork;
       /**
 * 税率
 */
                private BigDecimal monthTax;
       /**
 * 月收入
 */
                private BigDecimal monthIncome;
       /**
 * companyStaffSize
 */
                private String companyStaffSize;
                  	@Override
	public String getNodeType() {
		return "Base_TreeTest";
	}
}
