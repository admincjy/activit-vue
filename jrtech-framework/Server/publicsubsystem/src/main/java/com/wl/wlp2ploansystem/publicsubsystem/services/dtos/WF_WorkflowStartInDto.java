package com.wl.wlp2ploansystem.publicsubsystem.services.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Map;

/***
 * 启动流程输入dto
 */
@Data
public class WF_WorkflowStartInDto {


 public WF_WorkflowStartInDto(@NotBlank String processDefinitionKey, @NotBlank String originator, @NotBlank String departmentId, @NotBlank String businessId, @NotBlank String folio, @NotBlank LocalDateTime saveDate, String remark, Map<String, Object> variables) {
  this.processDefinitionKey = processDefinitionKey;
  this.originator = originator;
  this.departmentId = departmentId;
  this.businessId = businessId;
  this.folio = folio;
  this.saveDate = saveDate;
  this.remark = remark;
  this.variables = variables;
 }

 /***
     * 流程定义key
     */
   @NotBlank
    private String processDefinitionKey;

 /***
  * 流程发起人Id
  */
 @NotBlank
 private  String originator;
 /***
  * 业务单据所属部门id
  */
 @NotBlank
 private  String departmentId;

    /***
     * 业务单据id
     */
    @NotBlank
    private  String businessId;
    /***
     * 业务单据描述
     */
    @NotBlank
    private  String folio;
    /***
     * 单据开始时间
     */
    @NotBlank
    private LocalDateTime saveDate;
    /***
     * 备注
     */
    private String remark;


    /***
     * 工作流变量
     */
    private Map<String, Object> variables;
}
