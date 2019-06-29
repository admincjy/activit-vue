import me.javaroad.plugins.annotations.*;
import me.javaroad.plugins.entity.FullAuditedEntity;
import me.javaroad.plugins.type.Children;
import me.javaroad.plugins.type.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/***
 * 类@Display,@TypeMeta代码生成元数据说明
 */

/***
 * @Display: 显示名称（前端导航、权限显示名称）
 */
@Display("主子实体")

/***
 * @TypeMeta 类元数据
 * value:所属子项目，可选值：loansubsystem,publicsubsystem
 * uiType：UI生成方式，可为 dialog:弹出； nav：菜单导航
 */
@TypeMeta(value = "loansubsystem", uiType = TypeMeta.EUIType.nav, uiFormColumnNums = 2)

/***
 * 类需要继承 FullAuditedEntity，如类实现 ITreeObject接口，则表明是树形实体
 */
public class PL_EntityWithChildren extends FullAuditedEntity {

    /***
     * 文本输入字段
     */

    /***
     * @Display: 显示名称
     */
    @Display("申请编号")
    /***
     * 是否必输
     */
    @Required
    /***
     * 最大长度，如未设置，默认长度50
     */
    @MaxLength(50)
    private String customerCode;


    /***
     * 日期输入字段，无需设置 @MaxLength注解
     */

    /***
     * @Display: 显示名称
     */
    @Display("成立时间")
    /***
     * 是否必输
     */
    @Required
    private LocalDateTime companyRegistrationDate;

    /***
     * 货币输入字段，字段类型：Currency, 无需设置 @MaxLength注解
     */

    /***
     * @Display: 显示名称
     */
    @Display("注册资金")
    /***
     * 是否必输
     */
    @Required
    private Currency companyRegistrationCapital;

    /***
     * 普通数值输入字段，无需设置 @MaxLength注解
     */

    /***
     * @Display: 显示名称
     */
    @Display("建筑面积")
    /***
     * 是否必输
     */
    @Required
    private BigDecimal constructionArea;

    /***
     * 整数输入字段，无需设置 @MaxLength注解
     */

    /***
     * @Display: 显示名称
     */
    @Display("贷款期限")
    /***
     * 是否必输
     */
    @Required
    private Integer houseLoanTermCount;

    /***
     * 百分比输入字段，字段类型：Percent, 无需设置 @MaxLength注解
     */

    /***
     * @Display: 显示名称
     */
    @Display("费率")
    /***
     * 是否必输
     */
    @Required
    private me.javaroad.plugins.type.Percent housePurchasePrice;


    /***
     * 字典下拉输入字段， 无需设置 @MaxLength注解
     */

    /***
     * @Display: 显示名称
     */
    @Display("人员规模")

    /***
     * 是否必输
     */
    @Required

    /***
     * 字典下拉类别
     */
    @DataDictionary("pl_customerrelation_category")
    private String companyStaffSize;


    /***
     * Boolean 类型字段， 无需设置 @MaxLength注解
     */
    @Display("是否启用")
    /***
     * 是否必输
     */
    @Required
    private Boolean activited;

    /***
     * 对象参照字段，需设置ObjectRef：系统自动在数据库生成索引字段，在服务方法中加入 getListBy{字段名称},deleteBy{字段名称},deleteBatchBy{字段名称}方法
     */
    @Display("类别Id")
    @Required
    @ObjectRef("PL_LoanEnterWlCategory")
    private String categoryId;
    /***
     * 主子类型字段，需设置ForeignKey：外键，类型设置为：Children<...>
     * 注意子类中的对应字段（本例中的loanDocId一定要添加@ObjectRef("***")对象参照注解
     */

    /***
     * @Display: 显示名称
     */
    @Display("关联关系")

    /***
     * 子类产照父类字段名称
     */
    @ForeignKey("loanDocId")

    /***
     * 类型设置为：Children<...>
     */
    private Children<PL_EntityChild> customerRelations;

}