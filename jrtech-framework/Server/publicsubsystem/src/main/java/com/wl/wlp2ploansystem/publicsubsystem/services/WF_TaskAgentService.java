package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_AgentUserInfo;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_TaskAgent;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_TaskAgentFull;

import java.util.List;
 

/**
 * 任务授权
 */
public interface WF_TaskAgentService extends BaseService {

    /**
    * <p>
    * 根据 entity 条件，查询全部记录（并翻页）
    * </p>
    *
    * @param pager 分页查询条件（可以为 RowBounds.DEFAULT）
    * @param ew 实体对象封装操作类（可以为 null）
    * @return 实体分页对象
    */
    Page<WF_TaskAgentFull> getPagedList(Page<WF_TaskAgentFull> pager, EntityWrapper<WF_TaskAgentFull> ew);

    /**
    * <p>
    * 根据 entity 条件，查询全部记录
    * </p>
    *
    * @param ew 实体对象封装操作类（可以为 null）
    * @return 任务授权实体集合
    */
	List<WF_TaskAgent> getList(EntityWrapper<WF_TaskAgent> ew);
	
		
	/**
     * <p>
     * 根据单据id ，查询一条记录
     * </p>
     *
     * @param id 单据id
     * @return 实体
     */
    WF_TaskAgentFull get(String id);

    /**
    * <p>
    * 根据 ew 条件，查询总记录数
    * </p>
    *
    * @param ew 实体对象
    * @return 满足条件记录数
    */
	Integer count(EntityWrapper<WF_TaskAgent> ew);

	/**
	 * 是否存在记录
	 * @param id 单据id
	 * @return true:存在 false:不存在
	 */
	boolean exists(String id);
	
    /**
    * <p>
    * 插入一条记录
    * </p>
    *
    * @param entity 实体对象
    * @return 单据主键
    */
    String insert(WF_TaskAgent entity);
	
	/**
    * <p>
    * 批量插入记录
    * </p>
    *
    */
    void insertList(List<WF_TaskAgent> list);
	
	/**
    * <p>
    * 更新一条记录
    * </p>
    *
    * @param input 实体对象
    */
	void update(WF_TaskAgent input);

    /**
    * <p>
    * 插入或更新一条记录(id:null 插入；id:not null 更新)
    * </p>
    *
    * @param input 实体对象
    * @return 单据主键
    */
    String  save(WF_TaskAgent input);

    /**
     * <p>
     * 根据单据id ，删除一条记录
     * </p>
     *
     * @param id 单据id
     */
    Integer delete(String id);
    /**
     * <p>
     * 根据单据id集合 ，批量删除记录
     * </p>
     *
     * @param ids 单据id集合
     */
    Integer batchDelete(List<String> ids);

    Integer batchEnabledOrDisable(List<String> ids,Boolean enabled);

    WF_AgentUserInfo getAssigneeEnabledAgents(String  processDefinitionKey, String assignee);
}