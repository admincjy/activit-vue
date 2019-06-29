import lombok.Data;
import lombok.EqualsAndHashCode;
import me.javaroad.plugins.annotations.*;
import me.javaroad.plugins.entity.FullAuditedEntity;
import me.javaroad.plugins.entity.ITreeObject;
import me.javaroad.plugins.type.Currency;
import me.javaroad.plugins.type.Percent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)

/***
 * @Display: 显示名称（前端导航、权限显示名称）
 */
@Display("树形测试")

/***
 * @TypeMeta 类元数据
 * value:所属子项目，可选值：loansubsystem,publicsubsystem
 * uiType：UI生成方式，可为 dialog:弹出； nav：菜单导航
 */
@TypeMeta(value = "publicsubsystem", uiType = TypeMeta.EUIType.dialog, uiFormColumnNums = 1)
public class Base_TreeTest extends FullAuditedEntity implements ITreeObject {

    /***
     * 父菜单,不要设置为必输,如父菜单为空，则为根结点
     */
    @Display("父菜单")
    @MaxLength(50)
    public String parentId;

    /***
     * @Display: 显示名称
     */
    @Display("名称")
    /***
     * 是否必输
     */
    @Required
    /***
     * 最大长度，如未设置，默认长度50
     */
    @MaxLength(50)
    private String name;

    /***
     * @Display: 显示名称
     */
    @Display("年龄")
    /***
     * 是否必输
     */
    @Required
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

    /***
     * 字典下拉类别
     */
    @DataDictionary("pl_customerrelation_category")
    private String companyStaffSize;

}