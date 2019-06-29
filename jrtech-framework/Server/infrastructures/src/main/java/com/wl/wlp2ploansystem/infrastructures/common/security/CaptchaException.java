package com.wl.wlp2ploansystem.infrastructures.common.security;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码错误异常类
 *
 */
public class CaptchaException  extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    public CaptchaException(String message) {
        super(message);
    }

    public CaptchaException(String message, Throwable t) {
        super(message, t);
    }
}
