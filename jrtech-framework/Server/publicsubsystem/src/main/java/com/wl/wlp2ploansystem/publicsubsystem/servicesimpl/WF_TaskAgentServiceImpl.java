package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.infrastructures.workflow.vo.WF_AgentUserInfo;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_TaskAgent;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_TaskAgentFull;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.WF_TaskAgentFullRepository;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.WF_TaskAgentRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_UserService;
import com.wl.wlp2ploansystem.publicsubsystem.services.WF_TaskAgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 任务授权
 */

@Service
public class WF_TaskAgentServiceImpl extends BaseServiceImpl implements WF_TaskAgentService {

    protected static final Logger logger = LoggerFactory.getLogger(WF_TaskAgentServiceImpl.class);

    @Autowired
    private WF_TaskAgentRepository repository;
    @Autowired
    private WF_TaskAgentFullRepository fullRepository;
    @Autowired
    private Base_UserService userService;

    /**
     * <p>
     * 根据 entity 条件，查询全部记录（并翻页）
     * </p>
     *
     * @param page 分页查询条件（可以为 RowBounds.DEFAULT）
     * @param ew   实体对象封装操作类（可以为 null）
     * @return 实体分页对象
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WF_TaskAgentFull> getPagedList(Page<WF_TaskAgentFull> page, EntityWrapper<WF_TaskAgentFull> ew) {
        return page.setRecords(fullRepository.selectPage(page, ew));
    }

    /**
     * <p>
     * 根据 entity 条件，查询全部记录
     * </p>
     *
     * @param ew 实体对象封装操作类（可以为 null）
     * @return 任务授权实体集合
     */
    @Override
    @Transactional(readOnly = true)
    public List<WF_TaskAgent> getList(EntityWrapper<WF_TaskAgent> ew) {
        return repository.selectList(ew);
    }

    /**
     * <p>
     * 根据单据id ，查询一条记录
     * </p>
     *
     * @param id 单据id
     * @return 实体
     */
    @Override
    @Transactional(readOnly = true)
    public WF_TaskAgentFull get(String id) {
        WF_TaskAgentFull entity = fullRepository.selectById(id);


        return entity;
    }

    /**
     * <p>
     * 根据 ew 条件，查询总记录数
     * </p>
     *
     * @param ew 实体对象
     * @return 满足条件记录数
     */
    @Override
    @Transactional(readOnly = true)
    public Integer count(EntityWrapper<WF_TaskAgent> ew) {
        return repository.selectCount(ew);
    }

    /**
     * 是否存在记录
     *
     * @param id 单据id
     * @return true:存在 false:不存在
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(String id) {
        EntityWrapper<WF_TaskAgent> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("id", id);

        return this.count(entityWrapper) > 0;
    }

    /**
     * <p>
     * 插入一条记录
     * </p>
     *
     * @param input 实体对象
     * @return 单据id
     */
    @Override
    @Transactional
    public String insert(WF_TaskAgent input) {
        if (StringUtils.isEmpty(input.getId())) {
            input.setId(IdGenerator.get());
        }
        repository.insert(input);
        return input.getId();
    }

    /**
     * <p>
     * 批量插入记录
     * </p>
     */
    @Override
    @Transactional
    public void insertList(List<WF_TaskAgent> list) {
        list.forEach(p -> {
            insert(p);
        });
    }

    /***
     *获取用户特定流程可用的授权用户，可能为空
     * @param processDefinitionKey 流程定义
     * @param assignee 被授权人
     */
    public WF_AgentUserInfo getAssigneeEnabledAgents(String  processDefinitionKey, String assignee){

        EntityWrapper<WF_TaskAgent> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("assignee",assignee);
        entityWrapper.eq("processDefinitionKey",processDefinitionKey);
        entityWrapper.le("startTime", LocalDateTime.now());
        entityWrapper.gt("endTime", LocalDateTime.now().plusDays(-1).toLocalDate());
        entityWrapper.orderBy("id",false);
        List<WF_TaskAgent>  agents = repository.selectList(entityWrapper);

        WF_TaskAgent wf_taskAgent = repository.selectList(entityWrapper).stream().findFirst().orElse(null);

        if(wf_taskAgent !=null){
            String userName = userService.getUserName(wf_taskAgent.getAttorney());
            return  new WF_AgentUserInfo(wf_taskAgent.getAttorney(),userName);
        }
        return  null;
    }

    /**
     * <p>
     * 更新一条记录
     * </p>
     *
     * @param input 实体对象
     * @return 单据id
     */
    @Override
    @Transactional
    public void update(WF_TaskAgent input) {

        Integer effectRecords = repository.updateAllColumnById(input);

        if (effectRecords == 0) {
            throw new OptimisticConcurrencyException();
        }
    }

    /**
     * <p>
     * 插入或更新一条记录(id:null 插入；id:not null 更新)
     * </p>
     *
     * @param input 实体对象
     * @return 单据id
     */
    @Override
    @Transactional
    public String save(WF_TaskAgent input) {
        if (StringUtils.isEmpty(input.getId()) || !this.exists(input.getId())) {
            String id = this.insert(input);
            input.setId(id);
        } else {
            this.update(input);
        }
        return input.getId();
    }

    /**
     * <p>
     * 根据单据id ，删除一条记录
     * </p>
     *
     * @param id 单据id
     */
    @Override
    @Transactional
    public Integer delete(String id) {
        return repository.deleteById(id);
    }

    /**
     * <p>
     * 根据单据id集合 ，批量删除记录
     * </p>
     *
     * @param ids 单据id集合
     */
    @Override
    @Transactional
    public Integer batchDelete(List<String> ids) {

        return repository.deleteBatchIds(ids);
    }

    /**
     * <p>
     * 根据单据id集合 ，批量启用或禁用记录
     * </p>
     *
     * @param ids 单据id集合
     */
    @Override
    @Transactional
    public Integer batchEnabledOrDisable(List<String> ids,Boolean enabled) {

        int rowsCount = 0;
        String status = enabled?WF_TaskAgent.WF_TaskAgent_Status_Enabled:WF_TaskAgent.WF_TaskAgent_Status_Disabled;
        EntityWrapper<WF_TaskAgent> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("id",ids);
        List<WF_TaskAgent>  agents = repository.selectList(entityWrapper);

        for(WF_TaskAgent p : agents) {
            if(!status.equals(p.getStatus())) {
                p.setStatus(status);
                repository.updateAllColumnById(p);

                rowsCount++;
            }
        }
        return  Integer.valueOf(rowsCount);
    }
}
