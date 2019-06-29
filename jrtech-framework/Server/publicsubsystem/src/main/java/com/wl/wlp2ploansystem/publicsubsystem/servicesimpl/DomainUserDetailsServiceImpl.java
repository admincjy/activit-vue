package com.wl.wlp2ploansystem.publicsubsystem.servicesimpl;

import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_UserWithAuthorities;
import com.wl.wlp2ploansystem.publicsubsystem.manager.Base_UserWithAuthoritiesCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component("userDetailsService")
public class DomainUserDetailsServiceImpl implements UserDetailsService {

	private final Logger log = LoggerFactory.getLogger(DomainUserDetailsServiceImpl.class);

	@Autowired
	private Base_UserWithAuthoritiesCacheManager userWithAuthoritiesCacheManager;


	@Override
	@Transactional
	public UserDetails loadUserByUsername(final String login) {

		Base_UserWithAuthorities userFromDatabase = userWithAuthoritiesCacheManager.get(login);


		if(userFromDatabase == null || !userFromDatabase.getActivited()){
			throw new UserFriendlyException("账号或密码错误！");
		}

		return  userFromDatabase.toUserDetail();
	}


}
