package com.wl.wlp2ploansystem.infrastructures.common.security.jwt;

import com.wl.wlp2ploansystem.infrastructures.common.support.HttpHelper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * JWTFilter
 */
public class JWTFilter extends GenericFilterBean {

    private TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        if (StringUtils.hasText(jwt) && this.tokenProvider.validateToken(jwt)) {
            Authentication authentication = this.tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 解析HttpServletRequest中的token
     */
    public String resolveToken(HttpServletRequest request) throws UnsupportedEncodingException {
        String bearerToken = null;
        if(HttpHelper.isAjaxRequest(request)) {
            bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
        } else {
            Map<String, String> cookieValues = HttpHelper.readCookieMap(request);
            if (cookieValues.containsKey(JWTConfigurer.AUTHORIZATION_HEADER)) {
                bearerToken = cookieValues.get(JWTConfigurer.AUTHORIZATION_HEADER);
            }
        }
        if (StringUtils.hasText(bearerToken) ) {
            final Base64 base64 = new Base64();
            String  decodeBearerToken = new String(base64.decode(bearerToken), "UTF-8");
            return decodeBearerToken.substring(7, decodeBearerToken.length());
        }
        return null;
    }
}
