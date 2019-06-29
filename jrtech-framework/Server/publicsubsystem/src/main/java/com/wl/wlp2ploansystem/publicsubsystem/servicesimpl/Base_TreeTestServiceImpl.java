package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import java.util.List;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Service; 
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException; 
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.BaseServiceImpl;
import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.infrastructures.common.dto.TreeNodeDropInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeService;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_TreeTest;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_TreeTestRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_TreeTestService;


																

/**
 * 树形测试
 */

@Service
public class Base_TreeTestServiceImpl extends BaseServiceImpl implements Base_TreeTestService {

    protected static final Logger logger = LoggerFactory.getLogger(Base_TreeTestServiceImpl.class);

    @Autowired
    private Base_TreeTestRepository repository;

    
                                                                                                                                                                                            
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
    public Page<Base_TreeTest> getPagedList(Page<Base_TreeTest> page, EntityWrapper<Base_TreeTest> ew) {
        return page.setRecords(repository.selectPage(page, ew));
    }

    /**
     * <p>
     * 根据 entity 条件，查询全部记录
     * </p>
     *
     * @param ew 实体对象封装操作类（可以为 null）
     * @return 树形测试实体集合
     */
    @Override
    @Transactional(readOnly = true)
    public List<Base_TreeTest> getList(EntityWrapper<Base_TreeTest> ew) {
        return repository.selectList(ew);
    }

		 /**
     * <p>
     * 根据 entity 条件，查询记录并形成树形结构结果
     * </p>
     *
     * @param ew 实体对象封装操作类（可以为 null）
     * @return 树形测试树形结构结果
     */
    @Override
	@Transactional(readOnly = true)
	public Collection<TreeObject<Base_TreeTest>> getTreeList(EntityWrapper<Base_TreeTest> ew) {

		Collection<Base_TreeTest> list = this.getList(ew);

		TreeService<Base_TreeTest> treeFactory = new TreeService<Base_TreeTest>(list);

		Collection<TreeObject<Base_TreeTest>> results = treeFactory.getTreeRootList();

		return results;

	}
	
	/***
	 * 更新父节点和序号
	 * @param inputDto 输入Dto
	 */
	@Override
	@Transactional
	public void updateParentIdAndSortIndex(TreeNodeDropInputDto inputDto){

		Base_TreeTest entity = repository.selectById(inputDto.getId());
		entity.setParentId(inputDto.getNewParentId());
		//entity.setSortIndex(inputDto.getNewSortIndex());

		repository.updateAllColumnById(entity);
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
    public Base_TreeTest get(String id) {
        Base_TreeTest entity = repository.selectById(id);

                                                                                                                                                                                                                                                                                                                    
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
    public Integer count(EntityWrapper<Base_TreeTest> ew) {
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
		EntityWrapper<Base_TreeTest> entityWrapper = new EntityWrapper<>();
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
    public String insert(Base_TreeTest input) {
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
    public void insertList(List<Base_TreeTest> list) {
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
    public void update(Base_TreeTest input) {
	
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
    public String save(Base_TreeTest input) {
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
                                                                                                                                                                                        }
