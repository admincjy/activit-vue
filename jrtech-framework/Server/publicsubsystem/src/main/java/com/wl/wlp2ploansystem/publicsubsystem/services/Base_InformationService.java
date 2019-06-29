package com.wl.wlp2ploansystem.publicsubsystem.services;

import java.util.List;
import java.util.Collection;
import com.wl.wlp2ploansystem.publicsubsystem.services.BaseService;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;


import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Information;
 

/**
 * 信息公告
 */
public interface Base_InformationService extends BaseService {

    /**
    * <p>
    * 根据 entity 条件，查询全部记录（并翻页）
    * </p>
    *
    * @param pager 分页查询条件（可以为 RowBounds.DEFAULT）
    * @param ew 实体对象封装操作类（可以为 null）
    * @return 实体分页对象
    */
    Page<Base_Information> getPagedList(Page<Base_Information> pager, EntityWrapper<Base_Information> ew);

    /**
    * <p>
    * 根据 entity 条件，查询全部记录
    * </p>
    *
    * @param ew 实体对象封装操作类（可以为 null）
    * @return 信息公告实体集合
    */
	List<Base_Information> getList(EntityWrapper<Base_Information> ew);
	
		
	/**
     * <p>
     * 根据单据id ，查询一条记录
     * </p>
     *
     * @param id 单据id
     * @return 实体
     */
    Base_Information get(String id); 

    /**
    * <p>
    * 根据 ew 条件，查询总记录数
    * </p>
    *
    * @param ew 实体对象
    * @return 满足条件记录数
    */
	Integer count(EntityWrapper<Base_Information> ew);

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
    String insert(Base_Information entity);
	
	/**
    * <p>
    * 批量插入记录
    * </p>
    *
    */
    void insertList(List<Base_Information> list);
	
	/**
    * <p>
    * 更新一条记录
    * </p>
    *
    * @param input 实体对象
    */
	void update(Base_Information input);

    /**
    * <p>
    * 插入或更新一条记录(id:null 插入；id:not null 更新)
    * </p>
    *
    * @param input 实体对象
    * @return 单据主键
    */
    String  save(Base_Information input);

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