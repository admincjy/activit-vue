import me.javaroad.plugins.annotations.*;
import me.javaroad.plugins.entity.FullAuditedEntity;
import me.javaroad.plugins.type.Currency;
import me.javaroad.plugins.type.Percent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Display("子实体")
@TypeMeta(value = "loansubsystem", uiType = TypeMeta.EUIType.nav)
public class PL_EntityChild extends FullAuditedEntity {

    @Display("申请单")
    @ObjectRef("PL_LoanEnterWl")
    private String loanDocId;

    @Display("关联关系")
    @Required
    @DataDictionary("pl_customerrelation_category")
    private String relationCategoryId;

    @Display("姓名")
    @Required
    private String name;

    @Display("年龄")
    private Integer age;

    @Display("月薪")
    private BigDecimal mobile;

    @Display("出生日期")
    private LocalDateTime birthday;

    @Display("是否在职")
    private Boolean inWork;

    @Display("税率")
    private Percent monthTax;

    @Display("月收入")
    private Currency monthIncome;

}