package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Organization;

import java.util.Collection;
import java.util.List;

public interface Base_OrganizationService extends BaseService {

	Collection<TreeObject<Base_Organization>> getTreeOrganizations();

	Base_Organization get(String id);

	String save(Base_Organization input);

	String copy(String id);

	void delete(String id);

	void batchDelete(List<String> ids);


	void updateParentId(String sourceId, String toParentId);

	Collection<TreeObject<Base_Organization>> getOrganizationTreeChildren(String parentId);

}