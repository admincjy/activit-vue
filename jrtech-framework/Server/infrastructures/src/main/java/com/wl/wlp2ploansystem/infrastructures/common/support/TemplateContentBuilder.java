package com.wl.wlp2ploansystem.infrastructures.common.support;

import java.text.MessageFormat;
import java.util.List;
 
 
public  class TemplateContentBuilder {
	
	public static String build(String templateContent, List<String> templateVaribles) {
		
		return MessageFormat.format(templateContent, templateVaribles);
	}

}
