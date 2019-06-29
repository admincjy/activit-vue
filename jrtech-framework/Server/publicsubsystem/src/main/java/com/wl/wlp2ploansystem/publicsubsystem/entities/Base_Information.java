package com.wl.wlp2ploansystem.publicsubsystem.entities;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


								
/**
 * 信息公告
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("base_information")
    public class Base_Information extends FullAuditedEntity {
 /**
 * 标题
 */
        @NotBlank
            @Size(max = 50)
            private String title;
       /**
 * 摘要
 */
            @Size(max = 200)
            private String summary;
       /**
 * 内容
 */
            @Size(max = 4000)
            private String content;
       /**
 * 分类
 */
                private String classificationId;
       /**
 * 类别：文章，下载
 */
                private String type;
       /**
 * 状态
 */
                private String status;
       /**
 * 状态日期
 */
                private LocalDateTime statusDate;
       /**
 * 状态操作人
 */
            @Size(max = 50)
            private String statusOperator;
      }
