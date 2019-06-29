package com.wl.wlp2ploansystem.infrastructures.common.security;

import com.wl.wlp2ploansystem.infrastructures.common.errors.ErrorVM;
import com.wl.wlp2ploansystem.infrastructures.common.support.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationEntryPoint
 *
 */
public class MyAuthenticationEntryPoint implements  AuthenticationEntryPoint  {
	private final Logger log = LoggerFactory.getLogger(MyAuthenticationEntryPoint.class);
	/**
	 * Always returns a 401 error code to the client.
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {


			ErrorVM vm = new ErrorVM(null, "用户未登陆或者登陆已超时", null,null);

			String errorJson = JsonHelper.object2Json(vm, false);
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(errorJson);

		}


	}
