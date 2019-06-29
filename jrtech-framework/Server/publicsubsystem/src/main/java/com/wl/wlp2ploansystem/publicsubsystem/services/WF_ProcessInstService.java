package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_BusinessKey;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_ProcessInst;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_ProcessInstFull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface WF_ProcessInstService extends BaseService {

    /**
     * <p>
     * 根据 entity 条件，查询全部记录（并翻页）
     * </p>
     *
     * @param pager 分页查询条件（可以为 RowBounds.DEFAULT）
     * @param ew 实体对象封装操作类（可以为 null）
     * @return 实体分页对象
     */
    Page<WF_ProcessInstFull> getPagedList(Page<WF_ProcessInstFull> pager, EntityWrapper<WF_ProcessInstFull> ew);

    void createProcessInst(String instId,String businessId, String processDefination, String origiator, LocalDateTime saveDate,String folio, String remark);

    void afterCreateTask(String id, String procInstId, String actId, String actName, String actUserId, String formUrl, String
            status, WF_BusinessKey businessKey, Map<String, Object> variables);

    void createProcessInstHistory(String taskId, String processInstId, String actId, String actName, String taskUserSource, String taskUserId, String doAction, String taskResult, String taskRemark, LocalDateTime actionDate);
    /**
     * <p>
     * 根据单据id ，查询一条记录
     * </p>
     *
     * @param id 单据id
     * @return 实体
     */
    WF_ProcessInst get(String id);

    void delete(String id);

    /**
     * <p>
     * 根据单据id集合 ，批量删除记录
     * </p>
     *
     * @param ids 单据id集合
     */
    Integer batchDelete(List<String> ids);

    void setProcessInstStatus(String instId, String status);
}

