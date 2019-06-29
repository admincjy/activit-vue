package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_ProcessInstHistory;
import com.wl.wlp2ploansystem.publicsubsystem.entities.WF_ProcessInstHistoryFull;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.WF_ProcessInstHistoryFullRepository;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.WF_ProcessInstHistoryRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.WF_ProcessInstHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


		

/**
 * 流程实例
 */

@Service
public class WF_ProcessInstHistoryServiceImpl extends BaseServiceImpl implements WF_ProcessInstHistoryService {

    protected static final Logger logger = LoggerFactory.getLogger(WF_ProcessInstHistoryServiceImpl.class);

    @Autowired
    private WF_ProcessInstHistoryRepository repository;

    @Autowired
    private WF_ProcessInstHistoryFullRepository fullRepository;
                    
    /**
    * <p>
    * 根据 entity 条件，查询全部记录（并翻页）
    * </p>
    *
    * @param page 分页查询条件（可以为 RowBounds.DEFAULT）
    * @param ew 实体对象封装操作类（可以为 null）
    * @return 实体分页对象
    */
    @Override
    @Transactional(readOnly = true)
    public Page<WF_ProcessInstHistoryFull> getPagedList(Page<WF_ProcessInstHistoryFull> page, EntityWrapper<WF_ProcessInstHistoryFull> ew) {
        return page.setRecords(fullRepository.selectPage(page, ew));
    }

    /**
     * <p>
     * 根据 entity 条件，查询全部记录
     * </p>
     *
     * @param ew 实体对象封装操作类（可以为 null）
     * @return 流程实例实体集合
     */
    @Override
    @Transactional(readOnly = true)
    public List<WF_ProcessInstHistoryFull> getList(EntityWrapper<WF_ProcessInstHistoryFull> ew) {
        return fullRepository.selectList(ew);
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
    public WF_ProcessInstHistoryFull get(String id) {
        WF_ProcessInstHistoryFull entity = fullRepository.selectById(id);

                            
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
    public Integer count(EntityWrapper<WF_ProcessInstHistory> ew) {
        return repository.selectCount(ew);
    }

	/**
	 * 是否存在记录
	 * @param id 单据id
	 * @return true:存在 false:不存在
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean exists(String id){
		EntityWrapper<WF_ProcessInstHistory> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("id",id);

		return  this.count(entityWrapper)>0;
	}
    /**
    * <p>
    * 插入一条记录
    * </p>
    * @param input 实体对象
    * @return 单据id
	*/
    @Override
    @Transactional
    public String insert(WF_ProcessInstHistory input) {
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
     *
     */
    @Override
    @Transactional
    public void insertList(List<WF_ProcessInstHistory> list) {
        list.forEach(p -> {
            insert(p);
        });
    }

    /**
     * <p>
     * 更新一条记录
     * </p>
     * @param input 实体对象
     * @return 单据id
     */
    @Override
    @Transactional
    public void update(WF_ProcessInstHistory input) {
	
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
    public String save(WF_ProcessInstHistory input) {
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
             * 根据 流程实例Id，查询全部记录
             * </p>
             *
             * @param processInstId 流程实例Id
             * @return 流程实例实体集合
             */
            @Override
            @Transactional(readOnly = true)
            public List<WF_ProcessInstHistory> getListByProcessInstId(String processInstId) {
                EntityWrapper<WF_ProcessInstHistory> ew = new EntityWrapper<>();
                ew.eq("processInstId" ,processInstId);
                if (null != ew) {
                    ew.orderBy("id" , true);
                }
                return repository.selectList(ew);
            }

            /**
             * <p>
             * 根据 流程实例Id，删除全部记录
             * </p>
             *
             * @param processInstId 流程实例Id
             * @return 删除行数
             */
            @Override
			@Transactional
            public Integer deleteByProcessInstId(String processInstId) {
                EntityWrapper<WF_ProcessInstHistory> ew = new EntityWrapper<>();
                ew.eq("processInstId" ,processInstId);
                return repository.delete(ew);
            }

            /**
             * <p>
             * 根据 流程实例Id列表，批量删除全部记录
             * </p>
             *
             * @param processInstIdList 流程实例Id列表
             * @return 删除行数
             */
            @Override
            @Transactional
            public Integer deleteBatchByProcessInstId(List<String> processInstIdList) {
                EntityWrapper<WF_ProcessInstHistory> ew = new EntityWrapper<>();
                ew.in("processInstId" , processInstIdList);
                return repository.delete(ew);
            }
            }
