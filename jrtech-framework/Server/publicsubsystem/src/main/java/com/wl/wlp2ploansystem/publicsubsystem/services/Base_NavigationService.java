package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.dto.TreeNodeDropInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Navigation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface Base_NavigationService  extends BaseService {
	
	Collection<Base_Navigation> getAll();
	Collection<Base_Navigation> getAuthoritiyAll();
	Collection<TreeObject<Base_Navigation>> getTreeAll();
	Collection<TreeObject<Base_Navigation>> getAuthoritiyTreeAll();

	void updateParentIdAndSortIndex(TreeNodeDropInputDto inputDto);
	/**
	 * <p>
	 * 根据单据id ，查询一条记录
	 * </p>
	 *
	 * @param id 单据id
	 * @return 实体
	 */
	Base_Navigation get(String id);

	/**
	 * <p>
	 * 根据 ew 条件，查询总记录数
	 * </p>
	 *
	 * @param ew 实体对象
	 * @return 满足条件记录数
	 */
	Integer count(EntityWrapper<Base_Navigation> ew);

	/**
	 * 是否存在记录
	 * @param id 单据id
	 * @return true:存在 false:不存在
	 */
	@Transactional(readOnly = true)
	boolean exists(String id);

	/**
	 * <p>
	 * 插入一条记录
	 * </p>
	 *
	 * @param entity 实体对象
	 * @return 单据主键
	 */
	String insert(Base_Navigation entity);

	/**
	 * <p>
	 * 批量插入记录
	 * </p>
	 *
	 */
	void insertList(List<Base_Navigation> list);

	/**
	 * <p>
	 * 更新一条记录
	 * </p>
	 *
	 * @param input 实体对象
	 */
	void update(Base_Navigation input);

	/**
	 * <p>
	 * 插入或更新一条记录(id:null 插入；id:not null 更新)
	 * </p>
	 *
	 * @param input 实体对象
	 * @return 单据主键
	 */
	String  save(Base_Navigation input);

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
