package com.wl.wlp2ploansystem.infrastructures.common.jsscript;

/**
 * 后端请求映射（RequestMapping）自动生成前端js函数
 */
import com.wl.wlp2ploansystem.infrastructures.common.support.TypeHelper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;
@Component
public class RequestMappingScriptManager implements  ScriptManager {

	private static LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	@Override
	public String getScript() {

		List<String> ignoreBeanServiceNameList = Arrays.asList("kaptcha", "public", "kaptcha", "abpAppView",
				"basicError", "abpAppView");
		StringBuilder script = new StringBuilder();

		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
		Set<RequestMappingInfo> keySet = handlerMethods.keySet();

		List<RequestMethodItem> aequestMethodList = new ArrayList<RequestMethodItem>();

		for (RequestMappingInfo requestMappingInfo : keySet) {
			// 请求路径
			String path = requestMappingInfo.getPatternsCondition().toString().replace("[", "").replace("]", "");

			if(path.equals("/error")){
				continue;
			}
			// 请求方法
			String requestMethod = requestMappingInfo.getMethodsCondition().toString().replace("[", "").replace("]",
					"");

			// Controller的处理方法
			HandlerMethod handlerMethod = handlerMethods.get(requestMappingInfo);

			String handlerMethodName = handlerMethod.getMethod().getName();
			// 参数
			MethodParameter[] methodParameters = handlerMethod.getMethodParameters();

			List<RequestMethodParameter> parameters = new ArrayList<RequestMethodParameter>(methodParameters.length);
			for (MethodParameter methodParameter : methodParameters) {
				// 参数名称
				// 如果没有discover参数会是null.参考LocalVariableTableParameterNameDiscoverer
				methodParameter.initParameterNameDiscovery(discoverer);
				String parameterName = methodParameter.getParameterName();

				// 参数类型
				Class<?> parameterType = methodParameter.getParameterType();

				// 参数注解
				Object[] parameterAnnotations = methodParameter.getParameterAnnotations();

				// 注解
				String annoation = Arrays.toString(parameterAnnotations);

				RequestMethodParameter parameter = new RequestMethodParameter();
				parameter.setAnnoation(annoation);
				parameter.setName(parameterName);
				parameter.setType(parameterType);
				parameters.add(parameter);
			}

			// 所属bean名称
			String beanName = handlerMethod.getBean().toString();
			//首字母小写
			beanName = org.apache.commons.lang3.StringUtils.uncapitalize(beanName);
			// 返回header类型
			String responseType = requestMappingInfo.getProducesCondition().toString();

			RequestMethodItem rmi = new RequestMethodItem();
			rmi.setBeanName(beanName);
			rmi.setPath(path);
			rmi.setMethodName(handlerMethodName);
			rmi.setParameters(parameters);
			rmi.setResponseType(responseType);
			rmi.setRequestMethod(requestMethod);

			aequestMethodList.add(rmi);
		}

		Map<String, List<RequestMethodItem>> beanOfRequestMethodMap = aequestMethodList.stream()
				.filter(p -> !ignoreBeanServiceNameList.contains(p.getBeanServiceName()))
				.collect(Collectors.groupingBy(RequestMethodItem::getBeanServiceName));
		beanOfRequestMethodMap.forEach((beanServiceName, requestMethodList) -> {

			script.append("(function () { \n");

			script.append("	var " + beanServiceName + "_service = tapp.utils.createNamespace(tapp,'services." + beanServiceName + "'); \n");

			requestMethodList.forEach(requestMethod -> {

				List<String> primitiveParams = requestMethod.getParameters().stream()
						.filter(param -> TypeHelper.isPrimitive(param.getType())).map(param -> {
							return param.getName();
						}).collect(Collectors.toList());

				List<String> nonPrimitiveParams = requestMethod.getParameters().stream()
						.filter(param -> !TypeHelper.isPrimitive(param.getType())
								&& !param.getType().isAssignableFrom(HttpServletRequest.class)
								&& !param.getType().isAssignableFrom(HttpServletResponse.class)
								&& !param.getType().isAssignableFrom(BindingResult.class)
						)
						.map(param -> {
							return param.getName();
						}).collect(Collectors.toList());

				List<String> jsInputParams = new ArrayList<String>();
				jsInputParams.addAll(primitiveParams);
				jsInputParams.addAll(nonPrimitiveParams);

				String jsInputParamsString = String.join(",", jsInputParams);

				if (jsInputParamsString.length() > 0 && jsInputParamsString.endsWith(",")) {
					jsInputParamsString = jsInputParamsString.substring(0,jsInputParamsString.length()-1);
				}

				StringBuilder requestPathBuilder = new StringBuilder();

				for(int i= 0;i<primitiveParams.size();i++) {

					String p = primitiveParams.get(i);
					if (requestPathBuilder.length() == 0) {
						requestPathBuilder.append("?");
					} else {
						requestPathBuilder.append("&");
					}
					requestPathBuilder.append(p);
					requestPathBuilder.append("=' + escape(" + p + ") + '");

					i++;
				}
				String requestPath = requestMethod.getPath().substring(1) + requestPathBuilder.toString();

				String nonPrimitiveParam = nonPrimitiveParams.size() == 1 ? nonPrimitiveParams.get(0) : "{}";

				String reqMethod = requestMethod.getRequestMethod();
				if (StringUtils.isEmpty(reqMethod)) {
					reqMethod = "post";
				}
				script.append("    " +beanServiceName + "_service." + requestMethod.methodName + " = function (" + jsInputParamsString + ") { \n");
				script.append("			  var vue = window.vue;  \n");
				script.append("			  return vue.$http."+reqMethod.toLowerCase()+"('" + requestPath +"',"+nonPrimitiveParam+"); \n");
				script.append("	};  \n\n");
			});

			script.append("})();\n");
		});

		return script.toString();
	}

	@Data
	static class RequestMethodItem implements Comparable<RequestMethodItem> {

		private String beanName;
		private String path;
		private String requestMethod;
		private String methodName;
		private String responseType;
		private List<RequestMethodParameter> parameters;

		@Override
		public int compareTo(RequestMethodItem o) {
			return this.getMethodName().compareTo(o.getMethodName());
		}

		public String getBeanServiceName() {
			if (this.beanName.toLowerCase().endsWith("controller")) {
				return this.beanName.substring(0, this.beanName.length() - "controller".length());
			}
			return this.beanName;
		}

	}

	static class RequestMethodParameter {
		private String name;
		private String annoation;
		private Class<?> type;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAnnoation() {
			return annoation;
		}

		public void setAnnoation(String annoation) {
			this.annoation = annoation;
		}

		public Class<?> getType() {
			return type;
		}

		public void setType(Class<?> type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return "Parameter [name=" + name + ", annoation=" + annoation + ", type=" + type + "]";
		}
	}

}
