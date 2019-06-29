package com.wl.wlp2ploansystem.publicsubsystem.services;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_User;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_UserWithDepartmentNames;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_UserWithDepartments;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_UserWithRoleIds;

import java.time.LocalDateTime;
import java.util.List;

public interface Base_UserService extends BaseService {

	Base_UserWithRoleIds findOneWithRoleIds(String id);
	Base_UserWithDepartments getUserWithDepartments(String id);

	Base_User get(String id);

	String getUserName(String id);

	String saveUser(Base_UserWithRoleIds userWithRoleIds);

	List<String> batchInsert(List<Base_UserWithRoleIds> input);

	void delete(String id);

	void batchDelete(List<String> ids);

	void changePassword(String id, String encodeNewPassword) ;
	void adminChangePassword(String id, String encodeNewPasswordCar,Boolean shouldChangePassword);
	void updateUser(Base_User input);

	void saveLastLoginTime(String id,LocalDateTime lastLoginTime);

	Page<Base_UserWithDepartmentNames> getPagedList(Page<Base_UserWithDepartmentNames> page, EntityWrapper<Base_UserWithDepartmentNames> ew);

	Page<Base_User> getPagedCurrDepartmentRoleCategoryUsers(Page<Base_User> page,String roleCategoryId,Wrapper<Base_User> ew);


	Page<Base_User> getUserPagedList(Page<Base_User> page, EntityWrapper<Base_User> ew);
}
