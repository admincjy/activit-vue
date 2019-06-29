import me.javaroad.plugins.annotations.*;

import java.time.LocalDateTime;

@Display("信息公告")
@TypeMeta(value = "publicsubsystem", uiType = TypeMeta.EUIType.nav, uiFormColumnNums = 1)
public class Base_Information {



    @Display("标题")
    @Required
    @MaxLength(50)
    private  String title;

    @Display("摘要")
    @MaxLength(200)
    private  String summary;

    @Display("内容")
    @MaxLength(4000)
    private  String content;

    @Display("分类")
    @DataDictionary("base_information_classification")
    private String classificationId;

    @Display("类别：文章，下载")
    @DataDictionary("base_information_type")
    private String type;

    @Display("状态")
    @DataDictionary("base_information_status")
    private String status;

    @Display("状态日期")
    private LocalDateTime statusDate;

    @Display("状态操作人")
    @MaxLength(50)
    private String statusOperator;

}
