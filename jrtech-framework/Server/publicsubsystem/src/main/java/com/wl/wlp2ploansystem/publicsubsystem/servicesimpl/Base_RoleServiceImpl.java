package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.wl.wlp2ploansystem.infrastructures.common.dto.CommonCategoryIdAndSortingDto;
import com.wl.wlp2ploansystem.infrastructures.common.dto.SortDirection;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.infrastructures.common.eventbus.EntityChangedEvent;
import com.wl.wlp2ploansystem.infrastructures.common.eventbus.EntityChangedType;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.infrastructures.common.support.SimpleTreeObject;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeService;
import com.wl.wlp2ploansystem.publicsubsystem.entities.*;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.*;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class Base_RoleServiceImpl extends BaseServiceImpl implements Base_RoleService  {
	@Autowired
	private Base_RoleRepository roleRepository;
	@Autowired
	private Base_OrganizationRepository orgRepository;

	@Autowired
	private Base_RolePermissionRepository rolePermissionRepository;
	@Autowired
	private Base_RoleDataPermissionRepository roleDataPermissionRepository;
	@Autowired
	private Base_UserRoleRepository userRoleRepository;

	@Autowired
	private Base_UserRepository userRepository;

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public  Base_Role get(String id){
		return  roleRepository.selectById(id);
	}
	/* (non-Javadoc)
	 * @see com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.Base_RoleService#getOrgRoleTree()
	 */
	@Override
	public Collection<TreeObject<SimpleTreeObject>> getOrgRoleTree() {
		List<SimpleTreeObject> treeList = new ArrayList<SimpleTreeObject>();

		List<Base_Role> rolelist = roleRepository.selectList(null);
		List<Base_Organization> orglist = orgRepository.selectList(null);

		rolelist.forEach(p -> {
			treeList.add(new SimpleTreeObject(p.getId(), p.getOrganizationId(), p.getName(), "Role"));
		});

		orglist.forEach(p -> {
			treeList.add(new SimpleTreeObject(p.getId(), p.getParentId(), p.getName(), p.getNodeType()));
		});

		TreeService<SimpleTreeObject> treeFactory = new TreeService<SimpleTreeObject>(treeList);

		Collection<TreeObject<SimpleTreeObject>> results = treeFactory.getTreeRootList();

		return results;

	}
	@Override
	public List<Base_Role> getRolesOfOrg(CommonCategoryIdAndSortingDto input) {
		EntityWrapper<Base_Role> eWrapper = new EntityWrapper<Base_Role>();
		eWrapper.eq("organizationId", input.getCategoryId());

		if (!StringUtils.isEmpty(input.getSortingFiled())) {
			eWrapper.orderBy(input.getSortingFiled(), input.getSortDirection() == SortDirection.Asc);
		}

		return roleRepository.getViewList(eWrapper);
	}

	@Override
	public Integer selectCount(Wrapper<Base_Role> roleEntityWrapper){

		return roleRepository.selectCount(roleEntityWrapper);
	}
	@Override
	public void copy(String id){
		Base_Role role = this.get(id);

		EntityWrapper<Base_RoleWithPermissionIds> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("id",id);
		List<Base_RoleWithPermissionIds> list = this.getWithPermissionIdsList(entityWrapper);
		list.forEach(p->{
			p.resetFileds();
			p.setName(role.getName()+"-复制");
			p.setType(1);
			this.save(p);
		});
	}
	@Override
	public void copyWhere(EntityWrapper<Base_RoleWithPermissionIds> roleWithPermissionIdsEntityWrapper,String newOrganizationId){
		List<Base_RoleWithPermissionIds> list = this.getWithPermissionIdsList(roleWithPermissionIdsEntityWrapper);

		list.forEach(p->{
			p.resetFileds();
			p.setId(null);
			p.setType(1);
			p.setOrganizationId(newOrganizationId);
			this.save(p);
		});
	}
	@Override
	public List<Base_Role> getRoleListOfOrg(String organizationId) {
		EntityWrapper<Base_Role> eWrapper = new EntityWrapper<Base_Role>();
		eWrapper.eq("organizationId",organizationId);
		eWrapper.orderBy("id", true);
		return roleRepository.getViewList(eWrapper);
	}
	/* (non-Javadoc)
	 * @see com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.Base_RoleService#findOneWithPermissionIds(java.lang.String)
	 */
	@Override
	public Base_RoleWithPermissionIds findOneWithPermissionIds(String id) {
		EntityWrapper<Base_RoleWithPermissionIds> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("id",id);
		List<Base_RoleWithPermissionIds> list =  this.getWithPermissionIdsList(entityWrapper);

		return  list == null || list.size() == 0?null:list.get(0);
	}
	@Override
	public List<Base_RoleWithPermissionIds> getWithPermissionIdsList(EntityWrapper<Base_RoleWithPermissionIds> entityWrapper) {

		List<Base_RoleWithPermissionIds> list =  roleRepository.getWithPermissionIdsList(entityWrapper);
		return  list;
	}
	@Override
	@Transactional
	public String save(Base_RoleWithPermissionIds input) {

		boolean updated = false;
		if (StringUtils.isEmpty(input.getId())) {
			input.setId(IdGenerator.get());
			roleRepository.insert(input);

			updated = false;
		} else {
			Base_Role roleEntity = new Base_Role();
			roleEntity.setId(input.getId());
			roleEntity.setName(input.getName());
			roleEntity.setOrganizationId(input.getOrganizationId());
			roleEntity.setRemark(input.getRemark());
			roleEntity.setRoleCategoryId(input.getRoleCategoryId());
			roleEntity.setGmtVersion(input.getGmtVersion());
			Integer effectRecords= roleRepository.updateAllColumnById(roleEntity);

			updated  = true;

			if(effectRecords == 0){
				throw new OptimisticConcurrencyException();
			}
		}

		rolePermissionRepository.saveRolePermissions(input.getId(), input.getPermissionIds());
		roleDataPermissionRepository.saveRoleDataPermissions(input.getId(),input.getDataPermissionIds());

		if(updated) {
			//通知缓存变更
			List<String> roleUserIds =  this.getRoleUserList(input.getId()).stream().map(p->p.getId()).collect(Collectors.toList());
			applicationContext.publishEvent(new EntityChangedEvent(this, "base_userWithAuthorities", roleUserIds
					, EntityChangedType.update));
		}
		return input.getId();
	}


	/* (non-Javadoc)
	 * @see com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.Base_RoleService#delete(java.lang.String)
	 */
	@Override
	@Transactional
	public void delete(String id) {
		Base_Role entity = roleRepository.selectById(id);
		if (entity == null) {
			return;
		}
		if (!entity.canDelete()) {
			throw new UserFriendlyException(MessageFormat.format("岗位{0}禁止删除,无法删除！", entity.getName()));
		}
		/*
		if (userRoleRepository.selectCount(new EntityWrapper<Base_UserRole>().eq("roleId", id)) != 0) {
			throw new UserFriendlyException(MessageFormat.format("岗位{0}存在用户,无法删除！", entity.getName()));
		}
		*/
		roleDataPermissionRepository.delete(new EntityWrapper<Base_RoleDataPermission>().eq("roleId", id));
		rolePermissionRepository.delete(new EntityWrapper<Base_RolePermission>().eq("roleId", id));
		userRoleRepository.delete(new EntityWrapper<Base_UserRole>().eq("roleId", id));
		roleRepository.deleteById(id);

		//通知缓存变更
		List<String> roleUserIds =  this.getRoleUserList(id).stream().map(p->p.getId()).collect(Collectors.toList());
		applicationContext.publishEvent(new EntityChangedEvent(this, "base_userWithAuthorities", roleUserIds
				, EntityChangedType.update));
	}

	/* (non-Javadoc)
	 * @see com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.Base_RoleService#batchDelete(java.util.List)
	 */
	@Override
	@Transactional
	public void batchDelete(List<String> ids) {

		ids.stream().sorted((p1,p2)->p1.compareTo(p2)).forEach(p -> {
			delete(p);
		});

		//通知缓存变更
		List<String> roleUserIds =  this.getRolesUserList(ids).stream().map(p->p.getId()).collect(Collectors.toList());
		applicationContext.publishEvent(new EntityChangedEvent(this, "base_userWithAuthorities", roleUserIds
				, EntityChangedType.update));
	}
	@Override
	@Transactional
	public void deleteByOrganizationId(String organizationId){
		List<String> roleIds = this.getRoleListOfOrg(organizationId).stream().map(p->p.getId()).collect(Collectors.toList());
		if(roleIds.size()>0) {
			this.batchDelete(roleIds);
		}
	}
	@Override
	public Collection<Base_User> getRoleCategoryUsers(String departmentId,String roleCategoryId) {
		return userRepository.getRoleCategoryUsers(departmentId,roleCategoryId);
	}

	public Collection<Base_User> getRoleUserList(String roleId) {
		return  userRepository.getRoleUserList(roleId);
	}

	public Collection<Base_User> getRolesUserList(Collection<String> roleIds) {
		return  userRepository.getRolesUserList(roleIds);
	}
}