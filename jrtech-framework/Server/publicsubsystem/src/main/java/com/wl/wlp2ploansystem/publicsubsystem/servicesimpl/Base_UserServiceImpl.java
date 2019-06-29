package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wl.wlp2ploansystem.infrastructures.common.errors.OptimisticConcurrencyException;
import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.infrastructures.common.eventbus.EntityChangedEvent;
import com.wl.wlp2ploansystem.infrastructures.common.eventbus.EntityChangedType;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.infrastructures.common.support.CommonHelper;
import com.wl.wlp2ploansystem.infrastructures.common.support.IdGenerator;
import com.wl.wlp2ploansystem.infrastructures.common.support.ValidationHelper;
import com.wl.wlp2ploansystem.publicsubsystem.entities.*;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_RoleRepository;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_UserRepository;
import com.wl.wlp2ploansystem.publicsubsystem.repositories.Base_UserRoleRepository;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Base_UserServiceImpl extends BaseServiceImpl implements Base_UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private Base_UserRepository userRepository;

	@Autowired
	private Base_RoleRepository roleRepository;

	@Autowired
	private Base_UserRoleRepository userRoleRepository;

	@Autowired
	private ApplicationContext applicationContext;

	@Transactional(readOnly=true)
	public Page<Base_UserWithDepartmentNames> getPagedList(Page<Base_UserWithDepartmentNames> page,
														   EntityWrapper<Base_UserWithDepartmentNames> ew) {
		return page.setRecords(userRepository.getUserWithDepartmentNamesList(page,ew));

	}

	@Transactional(readOnly=true)
	public Page<Base_User> getPagedCurrDepartmentRoleCategoryUsers(Page<Base_User> page,String roleCategoryId,Wrapper<Base_User> ew){
		String departmentId = null;
		if(!SecurityUtils.getCurrentUser().isSystemAdmin()) {
			departmentId = SecurityUtils.getCurrentUser().getDepartmentIds().stream().findFirst().orElse(null);
			if (StringUtils.isEmpty(departmentId)) {
				throw new UserFriendlyException("发起人营业部不能为空");
			}
		}
		return page.setRecords(userRepository.getPagedRoleCategoryUsers(page, departmentId,roleCategoryId,ew));
	}

	@Override
	@Transactional(readOnly=true)
	public Base_UserWithRoleIds findOneWithRoleIds(String id) {
		Base_UserWithRoleIds dbUser = userRepository.findOneWithRoleIds(id);

		return dbUser;
	}
	@Override
	@Transactional(readOnly=true)
	public Base_UserWithDepartments getUserWithDepartments(String id) {
		Base_UserWithDepartments dbUser = userRepository.getUserWithDepartments(id);

		return dbUser;
	}

	@Override
	@Transactional(readOnly=true)
	public Base_User get(String id) {
		Base_User user = userRepository.selectById(id);

		return user;
	}

	@Override
	@Transactional(readOnly=true)
	public String getUserName(String id) {
		Base_User user = userRepository.selectById(id);

		return user ==null?"": user.getName();
	}
	@Override
	@Transactional
	public String saveUser(Base_UserWithRoleIds input) {
		Base_User loginIdOfuser = userRepository.findOneByLoginId(input.getLoginId());

		if (StringUtils.isEmpty(input.getId())) {
			if (loginIdOfuser != null) {
				throw new UserFriendlyException("登陆名已存在！");
			}
		} else {
			if (loginIdOfuser !=null && !loginIdOfuser.getId().equals(input.getId())) {
				throw new UserFriendlyException("登陆名已存在！");
			}
		}
		Base_User user =  null;

		if(!StringUtils.isEmpty(input.getId())) {
			user = userRepository.selectById(input.getId());
		}

		if (user != null && user.isSystemAdmin()) {
			throw new UserFriendlyException("用户名admin是系统账号,无法修改！");
		}

		if(input.getRoleIds() != null && input.getRoleIds().size()>0){

			EntityWrapper<Base_Role> rolerEntityWrapper = new EntityWrapper<Base_Role>();
			rolerEntityWrapper.in("id",input.getRoleIds());
			List<Base_Role> roles = roleRepository.selectList(rolerEntityWrapper);
			long rolesCount = roles.stream().filter(p->Base_Role.Rolecategory_trackingpersoninfomr.equals(p.getRoleCategoryId()))
					.count();
			if(rolesCount>1){
				throw new UserFriendlyException("用户只能属于一个开发人岗位！");
			}
		}
		  if (user == null) {
              user = new Base_User();
              user.setId(IdGenerator.get());
              user.setName(input.getName());
              user.setLoginId(input.getLoginId()); 
              user.setEmail(input.getEmail());  
              user.setActivited(input.getActivited());
              user.setLoginPassword(input.getLoginPassword());
              user.setMobile(input.getMobile());
              user.setRemark(input.getRemark());
              user.setShouldChangePassword(input.getShouldChangePassword());
              userRepository.insert(user);
          }
          else
          {
              user.setName(input.getName());
              user.setLoginId(input.getLoginId()); 
              user.setEmail(input.getEmail());  
              user.setActivited(input.getActivited());
              user.setMobile(input.getMobile());
              user.setRemark(input.getRemark());
			  user.setShouldChangePassword(input.getShouldChangePassword());
			  Integer effectRecords= userRepository.updateAllColumnById(user);

			  if(effectRecords == 0){
				  throw new OptimisticConcurrencyException();
			  }
          }

		userRoleRepository.saveUserRoles(user.getId(),input.getRoleIds());
		applicationContext.publishEvent(new EntityChangedEvent(this,"base_userWithAuthorities", Arrays.asList(user.getId()), EntityChangedType.insertOrUpdate));

		return  user.getId();
	}

	@Override
	@Transactional
	public List<String> batchInsert(List<Base_UserWithRoleIds> input) {

		List<String> errors = new ArrayList<>();

		input.forEach(p->{
			List<String> validErrors = ValidationHelper.validate(p);
			if(validErrors.size()>0){
				errors.add("用户:"+p.getLoginId() +"-"+ p.getName() + CommonHelper.stringJoin(validErrors));
			}
		});
		if(errors.size()>0){
			return  errors;
		}
		List<String> loginIds = input.stream().map(p->p.getLoginId()).collect(Collectors.toList());
		List<String> distinctLoginIds = loginIds.stream().distinct().collect(Collectors.toList());

		if(distinctLoginIds.size() != loginIds.size()){
			List<String> repeatLoginIds =  loginIds.stream().filter(p->!distinctLoginIds.contains(p)).collect(Collectors.toList());
			errors.add("用户名"+CommonHelper.stringJoin(repeatLoginIds) +"存在重复！");
			return errors;
		}

		EntityWrapper<Base_User> userEntityWrapper = new EntityWrapper<>();
		userEntityWrapper.in("loginId",loginIds);
		List<Base_User> existUsers = userRepository.selectList(userEntityWrapper);

		if(existUsers.size()>0){
			List<String> errorUsers = existUsers.stream().map(p->p.getLoginId() + "-" + p.getName()).collect(Collectors.toList());

			errors.add(CommonHelper.stringJoin(errorUsers) + "已存在！");
			return  errors;
		}

		List<String> roleIds = input.stream().map(p->p.getRoleIds()).flatMap(sp->sp.stream()).collect(Collectors.toList());
		EntityWrapper<Base_Role> roleEntityWrapper = new EntityWrapper<>();
		roleEntityWrapper.in("id",roleIds);
		List<Base_Role> globalRoles = roleRepository.selectList(roleEntityWrapper);

		input.forEach(p->{
			if(p.getRoleIds() != null && p.getRoleIds().size()>0) {
				List<Base_Role> roles = globalRoles.stream().filter(sp -> p.getRoleIds().contains(sp.getId())).collect(Collectors.toList());
				long rolesCount = roles.stream().filter(sp -> Base_Role.Rolecategory_trackingpersoninfomr.equals(sp.getRoleCategoryId()))
						.count();
				if (rolesCount > 1) {
					errors.add("用户"+p.getLoginId()+"只能属于一个开发人岗位！");
				}
			}
		});

		if(errors.size()>0){
			return  errors;
		}

		input.forEach(p->{
			Base_User user = new Base_User();
			user.setId(IdGenerator.get());
			user.setName(p.getName());
			user.setLoginId(p.getLoginId());
			user.setEmail(p.getEmail());
			user.setActivited(p.getActivited());
			user.setLoginPassword(passwordEncoder.encode(p.getLoginPassword()));
			user.setMobile(p.getMobile());
			user.setRemark(p.getRemark());
			userRepository.insert(user);
			userRoleRepository.saveUserRoles(user.getId(),p.getRoleIds());

		});

		applicationContext.publishEvent(new EntityChangedEvent(this,"base_userWithAuthorities", input.stream().map(p->p.getId()).collect(Collectors.toList())
				, EntityChangedType.insert));

		return  errors;

	}

	@Override
	@Transactional
	public void delete(String id) {

		Base_User entity = userRepository.selectById(id);
		if (entity == null) {
			return;
		}
		if (!entity.canDelete()) {
			throw new UserFriendlyException(MessageFormat.format("用户{0}禁止删除,无法删除！", entity.getName()));
		}
		userRepository.deleteById(id);
		userRoleRepository.delete(new EntityWrapper<Base_UserRole>().eq("userId", id));

		applicationContext.publishEvent(new EntityChangedEvent(this,"base_userWithAuthorities", Arrays.asList(id)
				, EntityChangedType.delete));
	}

	@Override
	@Transactional
	public void batchDelete(List<String> ids) {
		ids.forEach(p -> {
			delete(p);
		});

		applicationContext.publishEvent(new EntityChangedEvent(this,"base_userWithAuthorities", ids
				, EntityChangedType.delete));
	}
	@Override
	@Transactional
	public  void updateUser(Base_User input){
		Base_User user = userRepository.selectById(input.getId());
		user.setName(input.getName());
		user.setLoginId(input.getLoginId());
		user.setEmail(input.getEmail());
		user.setActivited(input.getActivited());
		user.setMobile(input.getMobile());
		user.setRemark(input.getRemark());

		userRepository.updateAllColumnById(user);

		applicationContext.publishEvent(new EntityChangedEvent(this,"base_userWithAuthorities", Arrays.asList(input.getId())
				, EntityChangedType.update));

	}
	@Override
	@Transactional
	public void changePassword(String id, String encodeNewPassword) {
		Base_User entity = userRepository.selectById(id);
		if (entity == null) {
			throw new UserFriendlyException("用户不存在！");
		}
		entity.setShouldChangePassword(Boolean.FALSE);
		entity.setLoginPassword(encodeNewPassword);
		userRepository.updateById(entity);

		applicationContext.publishEvent(new EntityChangedEvent(this,"base_userWithAuthorities", Arrays.asList(id)
				, EntityChangedType.update));
	}
	@Override
	@Transactional
	public void adminChangePassword(String id, String encodeNewPassword,Boolean shouldChangePassword) {
		Base_User entity = userRepository.selectById(id);
		if (entity == null) {
			throw new UserFriendlyException("用户不存在！");
		}
		entity.setShouldChangePassword(shouldChangePassword);
		entity.setLoginPassword(encodeNewPassword);
		userRepository.updateById(entity);

		applicationContext.publishEvent(new EntityChangedEvent(this,"base_userWithAuthorities", Arrays.asList(id)
				, EntityChangedType.update));
	}
	@Override
	@Transactional
	@Async
	public void saveLastLoginTime(String id, LocalDateTime lastLoginTime) {
		Base_User entity = userRepository.selectById(id);
		entity.setLastLoginTime(lastLoginTime);
		userRepository.updateAllColumnById(entity);
	}

	@Transactional(readOnly=true)
	public Page<Base_User> getUserPagedList(Page<Base_User> page, EntityWrapper<Base_User> ew) {
		return page.setRecords(userRepository.selectPage(page,ew));
	}
}