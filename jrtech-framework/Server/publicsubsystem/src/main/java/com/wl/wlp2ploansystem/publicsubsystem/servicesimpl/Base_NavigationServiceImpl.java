package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.dto.TreeNodeDropInputDto;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.eventbus.EntityChangedEvent;
import com.wl.wlp2ploansystem.infrastructures.common.eventbus.EntityChangedType;
import com.wl.wlp2ploansystem.infrastructures.common.security.DomainUserDetails;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeService;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Navigation;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_NavigationRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_NavigationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Base_NavigationServiceImpl extends BaseServiceImpl implements Base_NavigationService {
	@Autowired
	private Base_NavigationRepository repository;

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public Collection<Base_Navigation> getAll() {

		return repository.selectList(null).stream().sorted(Comparator.comparing(Base_Navigation::getSortIndex)).collect(Collectors.toList());
	}

	@Override
	public Collection<Base_Navigation> getAuthoritiyAll() {
		Collection<Base_Navigation> allng = this.getAll();
		DomainUserDetails user = SecurityUtils.getCurrentUser();
		if(user.getAuthorities() == null){ 
			return Collections.EMPTY_LIST;
		}
		List<String> authorities =user.getAuthorities().stream().map(p->{ return p.getAuthority(); }).collect(Collectors.toList());
		return allng.stream().filter(r -> {
			
			return authorities .contains(r.getRequiredPermissionId());
		}).collect(Collectors.toList());
	}

	@Override
	public Collection<TreeObject<Base_Navigation>> getTreeAll() {

		Collection<Base_Navigation> navs = this.getAll();

		TreeService<Base_Navigation> treeService = new TreeService<Base_Navigation>(navs);
		Collection<TreeObject<Base_Navigation>> treeNavs = treeService.getTreeRootList();

		return treeNavs;

	}

	@Override
	public Collection<TreeObject<Base_Navigation>> getAuthoritiyTreeAll() {

		Collection<Base_Navigation> navs = this.getAuthoritiyAll().stream().filter(p->p.getShow() == null || p.getShow().compareTo(Boolean.TRUE)>=0)
				.collect(Collectors.toList());

		TreeService<Base_Navigation> treeService = new TreeService<Base_Navigation>(navs);
		Collection<TreeObject<Base_Navigation>> treeNavs = treeService.getTreeRootList();

		return treeNavs;

	}

	/***
	 * 更新父节点和序号
	 * @param inputDto 输入Dto
	 */
	@Override
	@Transactional
	public void updateParentIdAndSortIndex(TreeNodeDropInputDto inputDto){

		Base_Navigation entity = repository.selectById(inputDto.getId());
		entity.setParentId(inputDto.getNewParentId());
		entity.setSortIndex(inputDto.getNewSortIndex());

		repository.updateAllColumnById(entity);

		applicationContext.publishEvent(new EntityChangedEvent(this,"JSScriptChanged",null, EntityChangedType.insertOrUpdate));

		applicationContext.publishEvent(new EntityChangedEvent(this,"ClientJsonChanged",null, EntityChangedType.insertOrUpdate));
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
	public Base_Navigation get(String id) {
		Base_Navigation entity = repository.selectById(id);


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
	public Integer count(EntityWrapper<Base_Navigation> ew) {
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
		EntityWrapper<Base_Navigation> entityWrapper = new EntityWrapper<>();
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
	public String insert(Base_Navigation input) {
		if (StringUtils.isEmpty(input.getId())) {
			input.setId(IdGenerator.get());
		}
		repository.insert(input);

		applicationContext.publishEvent(new EntityChangedEvent(this,"JSScriptChanged",null, EntityChangedType.insertOrUpdate));

		applicationContext.publishEvent(new EntityChangedEvent(this,"ClientJsonChanged",null, EntityChangedType.insertOrUpdate));

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
	public void insertList(List<Base_Navigation> list) {
		list.forEach(p -> {
			insert(p);
		});

		applicationContext.publishEvent(new EntityChangedEvent(this,"JSScriptChanged",null, EntityChangedType.insertOrUpdate));

		applicationContext.publishEvent(new EntityChangedEvent(this,"ClientJsonChanged",null, EntityChangedType.insertOrUpdate));
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
	public void update(Base_Navigation input) {

		Integer effectRecords = repository.updateAllColumnById(input);

		if (effectRecords == 0) {
			throw new OptimisticConcurrencyException();
		}

		applicationContext.publishEvent(new EntityChangedEvent(this,"JSScriptChanged",null, EntityChangedType.insertOrUpdate));

		applicationContext.publishEvent(new EntityChangedEvent(this,"ClientJsonChanged",null, EntityChangedType.insertOrUpdate));
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
	public String save(Base_Navigation input) {
		if (StringUtils.isEmpty(input.getId())) {
			String id = this.insert(input);
			input.setId(id);
		} else {
			this.update(input);
		}
		applicationContext.publishEvent(new EntityChangedEvent(this,"JSScriptChanged",null, EntityChangedType.insertOrUpdate));

		applicationContext.publishEvent(new EntityChangedEvent(this,"ClientJsonChanged",null, EntityChangedType.insertOrUpdate));
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


		Integer rowsEffected =  repository.deleteById(id);

		applicationContext.publishEvent(new EntityChangedEvent(this,"JSScriptChanged",null, EntityChangedType.insertOrUpdate));

		applicationContext.publishEvent(new EntityChangedEvent(this,"ClientJsonChanged",null, EntityChangedType.insertOrUpdate));

		return  rowsEffected;
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

		Integer rowsEffected =   repository.deleteBatchIds(ids);

		applicationContext.publishEvent(new EntityChangedEvent(this,"JSScriptChanged",null, EntityChangedType.insertOrUpdate));

		applicationContext.publishEvent(new EntityChangedEvent(this,"ClientJsonChanged",null, EntityChangedType.insertOrUpdate));

		return rowsEffected;
	}
}
