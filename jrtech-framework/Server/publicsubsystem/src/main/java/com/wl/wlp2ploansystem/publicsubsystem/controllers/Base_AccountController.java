package com.wl.wlp2ploansystem.publicsubsystem.controllers;

import com.wl.wlp2ploansystem.infrastructures.common.ApplicationProperties;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Display;
import com.wl.wlp2ploansystem.infrastructures.common.annotations.Log;
import com.wl.wlp2ploansystem.infrastructures.common.errors.UserFriendlyException;
import com.wl.wlp2ploansystem.infrastructures.common.security.AuthoritiesConstants;
import com.wl.wlp2ploansystem.infrastructures.common.security.DomainUserDetails;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import com.wl.wlp2ploansystem.infrastructures.common.security.jwt.JWTConfigurer;
import com.wl.wlp2ploansystem.infrastructures.common.security.jwt.TokenProvider;
import com.wl.wlp2ploansystem.infrastructures.common.support.SpringContextUtil;
import com.wl.wlp2ploansystem.infrastructures.common.support.TreeObject;
import com.wl.wlp2ploansystem.publicsubsystem.controllers.dto.LoginRequestVo;
import com.wl.wlp2ploansystem.publicsubsystem.controllers.dto.LoginResponseVo;
import com.wl.wlp2ploansystem.publicsubsystem.controllers.dto.UserSystemInfoVo;
import com.wl.wlp2ploansystem.publicsubsystem.entities.Base_Navigation;
import com.wl.wlp2ploansystem.publicsubsystem.manager.Base_UserLoginAttemptManager;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_NavigationService;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_NotificationService;
import com.wl.wlp2ploansystem.publicsubsystem.services.Base_UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@Display("用户验证服务")
@Api("用户验证服务")
public class Base_AccountController {

    private final Logger logger = LoggerFactory.getLogger(Base_AccountController.class);
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private Base_UserService userService;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private Base_NotificationService notificationService;
    @Autowired
    private Base_NavigationService navigationService;

    @Autowired
    private Base_UserLoginAttemptManager loginAttemptManager;


    @PostMapping("/pmtapi/base_Account/login")
    @Log(value = "用户登陆")
    @ApiOperation(value = "用户登陆", notes = "用户登陆")
    @ApiImplicitParam(name = "input", value = "用户登陆实体", required = true, dataType = "LoginRequestVo")
    public LoginResponseVo login(@Valid @RequestBody LoginRequestVo input) throws UnsupportedEncodingException {

        HttpServletRequest request = SpringContextUtil.getHttpServletRequest();
        HttpServletResponse response = SpringContextUtil.getHttpServletResponse();


        if (applicationProperties.getKaptcha().getEnabled()) {
            int  failedCount =  loginAttemptManager.setAndGetLoginFailed(input.getUsername());
            if(failedCount>=3) { //输入失败三次，要求用户输入验证码。
                String verification = input.getCaptchaCode();
                String captcha = (String) request.getSession().getAttribute(AuthoritiesConstants.KAPTCHA_KEY);

                if (StringUtils.isEmpty(verification) || !captcha.equalsIgnoreCase(verification)) {
                    throw new UserFriendlyException("验证码不匹配或者不存在!",null, null,1);
                }
            }
        }
        request.getSession().removeAttribute(AuthoritiesConstants.KAPTCHA_KEY);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(input.getUsername(), input.getPassword());

        try {
            Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            DomainUserDetails currentUser = SecurityUtils.getCurrentUser();
            //记录最后登录日期
            userService.saveLastLoginTime(currentUser.getId(),
                    LocalDateTime.now());

            //删除登陆失败记录
            loginAttemptManager.deleteLoginFailed(input.getUsername());

            logger.info(MessageFormat.format("user {0} login in,ip:{1}", SecurityUtils.getCurrentUser().getLoginId(),
                    request.getRemoteAddr()));
            String jwt = tokenProvider.createToken();

            int tokenValidityInSeconds = applicationProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();

            final Base64 base64 = new Base64();
            final byte[] bearerJwtByte = ("Bearer " + jwt).getBytes("UTF-8");
            final String bearerJwtEncodingText = base64.encodeToString(bearerJwtByte);

            Cookie cookie = new Cookie(JWTConfigurer.AUTHORIZATION_HEADER, bearerJwtEncodingText);
            cookie.setMaxAge(tokenValidityInSeconds + 60 * 60 * 24); //单位秒，24小时
            cookie.setPath("/");
            response.addCookie(cookie);

            LoginResponseVo responseDto = new LoginResponseVo();
            responseDto.setAppKey((bearerJwtEncodingText));
            responseDto.setUserId(currentUser.getId());
            responseDto.setUserLoginId(currentUser.getLoginId());
            responseDto.setUserDisplayName(currentUser.getName());
            responseDto.setUserMobile(currentUser.getMobile());
            responseDto.setUserShouldChangePassword (currentUser.getShouldChangePassword());
            return responseDto;
        } catch (AuthenticationException ex) {

            //记录登录失败次数
            int  failedCount =  loginAttemptManager.setAndGetLoginFailed(input.getUsername());
            //输入失败三次，要求用户输入验证码。
            if(failedCount >=3){
                //payload(返回参数)为1,需要用户输入验证码，
                throw new UserFriendlyException("账号或密码错误!",null, ex,1);
            }
            else{
                //payload(返回参数)为0,不需要用户输入验证码，
                throw new UserFriendlyException("账号或密码错误!",null, ex,0);
            }

        }
    }

    @PostMapping("/authapi/base_Account/getUserSystemInfo")
    @Log("获取用户系统信息")
    @ApiOperation(value = "获取用户系统信息", notes = "获取用户系统信息")
    public UserSystemInfoVo getUserSystemInfo() {

        DomainUserDetails user = SecurityUtils.getCurrentUser();
        Collection<String> authorities = user.getAuthorities().stream().map(p->p.getAuthority()).collect(Collectors.toList());

        Integer notificationCount = notificationService.getNotificationCount();
        Collection<TreeObject<Base_Navigation>> authoritiyNavigationTree = navigationService.getAuthoritiyTreeAll();

        UserSystemInfoVo result = new UserSystemInfoVo();
        result.setNotificationCount(notificationCount);
        result.setAuthoritiyNavigationTree(authoritiyNavigationTree);
        result.setPermissions(authorities);
        return result;
    }

    @PostMapping("/authapi/base_Account/loginout")
    @Log("用户注销")
    public  void loginout(){
        Cookie cookie = new Cookie(JWTConfigurer.AUTHORIZATION_HEADER, "");
        cookie.setMaxAge(0);  //立即删除
        cookie.setPath("/");
        HttpServletResponse response = SpringContextUtil.getHttpServletResponse();
        response.addCookie(cookie);
    }

}
