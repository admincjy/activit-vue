package com.wl.wlp2ploansystem.infrastructures.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder; 

/**
 * Spring Security.工具
 */
public final class SecurityUtils {

	private SecurityUtils() {
	}

	/**
	 * 获取当前用户
	 *
	 */
	public static DomainUserDetails getCurrentUser() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Authentication authentication = securityContext.getAuthentication();
		if(authentication == null
		 || "anonymousUser".equalsIgnoreCase(authentication.getPrincipal().toString())){
			return  null;
		}else {

			DomainUserDetails springSecurityUser = (DomainUserDetails) authentication.getPrincipal();
			return springSecurityUser;
		}
	} 
}
