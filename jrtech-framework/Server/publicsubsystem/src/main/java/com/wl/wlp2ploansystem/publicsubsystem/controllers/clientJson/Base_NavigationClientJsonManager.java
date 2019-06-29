package com.wl.wlp2ploansystem.publicsubsystem.controllers.clientJson;

import com.wl.wlp2ploansystem.infrastructures.common.clientJson.ClientJsonManager;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Navigation;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_NavigationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Base_NavigationClientJsonManager implements ClientJsonManager {

	public Base_NavigationClientJsonManager(Base_NavigationService service){
		this.service = service;
	} 
	private Base_NavigationService service;
	@Override
	public Map<String, Object> getClientJsonObject() {
		List<Base_Navigation> userMenus = service.getAll().stream().filter(p->!StringUtils.isEmpty(p.getComponent())).collect(Collectors.toList());
		return Collections.singletonMap("base_navigation",userMenus);
	}
}
