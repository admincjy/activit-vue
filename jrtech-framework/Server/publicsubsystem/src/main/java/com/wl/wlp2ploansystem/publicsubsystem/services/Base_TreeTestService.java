package com.wl.wlp2ploansystem.publicsubsystem.services;

import java.util.List;
import java.util.Collection;
import com.wl.wlp2ploansystem.publicsubsystem.services.BaseService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import com.wl.wlp2ploansystem.infrastructures.common.dto.TreeNodeDropInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject; 

import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_TreeTest;
 

/**
 * 树形测试
 */
public interface Base_TreeTestService extends BaseService {

    /**
    * <p>
    * 根据 entity 条件，查询全部记录（并翻页）
    * </p>
    *
    * @param pager 分页查询条件（可以为 RowBounds.DEFAULT）
    * @param ew 实体对象封装操作类（可以为 null）
    * @return 实体分页对象
    */
    Page<Base_TreeTest> getPagedList(Page<Base_TreeTest> pager, EntityWrapper<Base_TreeTest> ew);

    /**
    * <p>
    * 根据 entity 条件，查询全部记录
    * </p>
    *
    * @param ew 实体对象封装操作类（可以为 null）
    * @return 树形测试实体集合
    */
	List<Base_TreeTest> getList(EntityWrapper<Base_TreeTest> ew);
	
		 /**
     * <p>
     * 根据 entity 条件，查询记录并形成树形结构结果
     * </p>
     *
     * @param ew 实体对象封装操作类（可以为 null）
     * @return 树形测试树形结构结果
     */
	Collection<TreeObject<Base_TreeTest>> getTreeList(EntityWrapper<Base_TreeTest> ew);
	
	/***
	 * 更新父节点和序号
	 * @param inputDto 输入Dto
	 */
	void updateParentIdAndSortIndex(TreeNodeDropInputDto inputDto);
		
	/**
     * <p>
     * 根据单据id ，查询一条记录
     * </p>
     *
     * @param id 单据id
     * @return 实体
     */
    Base_TreeTest get(String id); 

    /**
    * <p>
    * 根据 ew 条件，查询总记录数
    * </p>
    *
    * @param ew 实体对象
    * @return 满足条件记录数
    */
	Integer count(EntityWrapper<Base_TreeTest> ew);

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
    String insert(Base_TreeTest entity);
	
	/**
    * <p>
    * 批量插入记录
    * </p>
    *
    */
    void insertList(List<Base_TreeTest> list);
	
	/**
    * <p>
    * 更新一条记录
    * </p>
    *
    * @param input 实体对象
    */
	void update(Base_TreeTest input);

    /**
    * <p>
    * 插入或更新一条记录(id:null 插入；id:not null 更新)
    * </p>
    *
    * @param input 实体对象
    * @return 单据主键
    */
    String  save(Base_TreeTest input);

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

                                                                                                                                                                                       } 