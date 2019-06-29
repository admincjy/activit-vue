package com.wl.wlp2ploansystem.publicsubsystem.controllers.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonPagedInputDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

 
/**
 * 信息公告查询全部记录（并翻页）输入数据传输对象
*/
@EqualsAndHashCode(callSuper = true)
@Data
public class Base_InformationQueryInDto extends CommonPagedInputDto {

			/**
	 * 标题结束
	 */
	private String title;
				/**
	 * 摘要结束
	 */
	private String summary;
				/**
	 * 内容结束
	 */
	private String content;
				/**
	 * 分类
	 */
	private List<String> classificationIdList;
				/**
	 * 类别：文章，下载
	 */
	private List<String> typeList;
				/**
	 * 状态
	 */
	private List<String> statusList;
				
	 
	/**
	 * 状态日期开始
	 */
	private  LocalDateTime  statusDateBegin;
	 /**
	 * 状态日期结束
	 */
	private  LocalDateTime  statusDateEnd;
				/**
	 * 状态操作人结束
	 */
	private String statusOperator;
		}
