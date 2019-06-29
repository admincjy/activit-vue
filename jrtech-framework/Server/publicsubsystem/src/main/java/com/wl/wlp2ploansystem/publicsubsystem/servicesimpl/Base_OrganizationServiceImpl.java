package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.infrastructures.common.support.*;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Organization;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Role;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_RoleWithPermissionIds;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_OrganizationRepository;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_RoleRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_OrganizationService;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class Base_OrganizationServiceImpl extends BaseServiceImpl implements Base_OrganizationService  {

	@Autowired
	private Base_OrganizationRepository orgRepository;
	@Autowired
	private Base_RoleService roleService;

	/* (non-Javadoc)
	 * @see com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.Base_OrganizationService#getTreeOrganizations()
	 */
	@Override
	public Collection<TreeObject<Base_Organization>> getTreeOrganizations() {

		Collection<Base_Organization> orglist = orgRepository.getAll();

		TreeService<Base_Organization> treeFactory = new TreeService<Base_Organization>(orglist);

		Collection<TreeObject<Base_Organization>> results = treeFactory.getTreeRootList();

		return results;

	}

	@Override
	public Collection<TreeObject<Base_Organization>> getOrganizationTreeChildren(String parentId) {

		Collection<Base_Organization> orglist = orgRepository.getAll();

		TreeService<Base_Organization> treeFactory = new TreeService<Base_Organization>(orglist, Arrays.asList(parentId),false);

		Collection<TreeObject<Base_Organization>> results = treeFactory.getTreeRootList();

		return results;
	}

	/* (non-Javadoc)
	 * @see com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.Base_OrganizationService#get(java.lang.String)
	 */
	@Override
	public Base_Organization get(String id) {
		return orgRepository.selectById(id);
	}

	public boolean exists(String id){

		EntityWrapper<Base_Organization> organizationEntityWrapper = new EntityWrapper<>();
		organizationEntityWrapper.eq("id",id);
		return  orgRepository.selectCount(organizationEntityWrapper).intValue() != 0;

	}

	/* (non-Javadoc)
	 * @see com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.Base_OrganizationService#save(com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Organization)
	 */
	@Override
	@Transactional
	public String save(Base_Organization input) {
		if (StringUtils.isEmpty(input.getId()) || !this.exists(input.getId())) {
			if(StringUtils.isEmpty(input.getId())) {
				input.setId(IdGenerator.get());
			}
			orgRepository.insert(input);
		} else {
			Integer effectRecords= orgRepository.updateAllColumnById(input);

			if(effectRecords == 0){
				throw new OptimisticConcurrencyException();
			}
		}

		return input.getId();
	}
	@Override
	@Transactional
	public String copy(String id){

		String newOrgId = IdGenerator.get();

		Base_Organization entity = this.get(id);
		this.singleCopySave(id,newOrgId,entity.getName() +"-复制",entity.getParentId());

		Collection<TreeObject<Base_Organization>> children = this.getOrganizationTreeChildren(id);
		TreeService<Base_Organization> treeService = TreeService.of(children);
		treeService.retriveTree(p->{
			String newId = IdGenerator.get();
			p.setParentId(null);
			p.setId(newId);
		});

		List<TreeObject<Base_Organization>>  leafOrgList =  treeService.getLeafNodes();
		leafOrgList.forEach(p->{
	        TreeObject<Base_Organization> treeOrg = p;
			TreeObject<Base_Organization> parentTreeOrg =  p.getParent();
			while (parentTreeOrg !=null){
				treeOrg.setParentId(parentTreeOrg.getId());
				treeOrg = parentTreeOrg;
				parentTreeOrg = parentTreeOrg.getParent();
			}
		});
		treeService.retriveTree(p->{
			String parentId = p.getParentId() == null?newOrgId:p.getParentId();
			this.singleCopySave(p.getSelf().getId(),p.getId(),p.getName(),parentId);
		});

		return  newOrgId;
	}


	/* (non-Javadoc)
	 * @see com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.Base_OrganizationService#delete(java.lang.String)
	 */
	@Override
	@Transactional
	public void delete(String id) {
		Base_Organization org = orgRepository.selectById(id);
		if (org == null) {
			return;
		}
		if (!org.canDelete()) {
			throw new UserFriendlyException(MessageFormat.format("组织机构{0}是根结点或者禁止删除,无法删除！", org.getName()));
		}

		//把子结点的父结点更新为爷爷结点
		EntityWrapper<Base_Organization> childrenOrgEw = new EntityWrapper<>();
		childrenOrgEw.eq("parentId",id);
		List<Base_Organization> childrenOrgList = orgRepository.selectList(childrenOrgEw);
		childrenOrgList.forEach(p->{
			this.updateParentId(p.getId(),org.getParentId());
		});

 		roleService.deleteByOrganizationId(id);
		orgRepository.deleteById(id);

	}

	/* (non-Javadoc)
	 * @see com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.Base_OrganizationService#batchDelete(java.util.List)
	 */
	@Override
	@Transactional
	public void batchDelete(List<String> ids) {
		ids.forEach(p -> {
			delete(p);
		});
	}

	/* (non-Javadoc)
	 * @see com.wl.wlp2ploansystem.publicsubsystem.servicesimpl.Base_OrganizationService#updateParentId(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public void updateParentId(String sourceId, String toParentId) {
		Base_Organization org = new Base_Organization();
		org.setId(sourceId);
		org.setParentId(toParentId);
		Integer effectRecords= orgRepository.updateAllColumnById(org);

		if(effectRecords == 0){
			throw new OptimisticConcurrencyException();
		}
	}

	private void singleCopySave(String id,String newId,String newName,String newParentId){

		Base_Organization entity = this.get(id);
		entity.resetFileds();
		entity.setId(newId);
		entity.setParentId(newParentId);
		entity.setName(newName);
		entity.setType(1);
		this.save(entity);

		EntityWrapper<Base_RoleWithPermissionIds> roleWithPermissionIdsEntityWrapper  = new EntityWrapper<>();
		roleWithPermissionIdsEntityWrapper.eq("organizationId",id);
		roleService.copyWhere(roleWithPermissionIdsEntityWrapper,newId);
	}

}
