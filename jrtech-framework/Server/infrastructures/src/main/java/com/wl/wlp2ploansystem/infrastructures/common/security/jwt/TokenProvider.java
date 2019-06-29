package com.wl.wlp2ploansystem.infrastructures.common.security.jwt;


import com.wl.wlp2ploansystem.infrastructures.common.ApplicationProperties;
import com.wl.wlp2ploansystem.infrastructures.common.security.DomainUserDetails;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * JWT 解析组件
 */
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private final Logger log = LoggerFactory.getLogger(TokenProvider.class);
    private final ApplicationProperties applicationProperties;
    private final UserDetailsService userService;
    private String secretKey;
    private int tokenValidityInMilliseconds;

    public TokenProvider(ApplicationProperties applicationProperties,
                         UserDetailsService userService) {
        this.applicationProperties = applicationProperties;
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        this.secretKey =
                applicationProperties.getSecurity().getAuthentication().getJwt().getSecret();

        this.tokenValidityInMilliseconds =
                1000 * applicationProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds();
            }

    /**
     * 创建 token
     * @return token
     */
    public String createToken() {

        long now = (new Date()).getTime();
        Date   validity = new Date(now + this.tokenValidityInMilliseconds);

        DomainUserDetails domainUserDetails = SecurityUtils.getCurrentUser();

        return Jwts.builder()
                .setSubject(domainUserDetails.getLoginId())
                .claim(AUTHORITIES_KEY, domainUserDetails.getId())
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setExpiration(validity)
                .compact();
    }

    /**
     * 从token中解析出Authentication对象
     * @param token token
     * @return Authentication对象
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
        String userId = claims.get(AUTHORITIES_KEY).toString();
        String loginId = claims.getSubject();

        DomainUserDetails principal = (DomainUserDetails)userService.loadUserByUsername(loginId);

       if(principal !=null)
       {

            return new UsernamePasswordAuthenticationToken(principal, token, principal.getAuthorities());
        }else{
                throw new UsernameNotFoundException("账号错误！");
       }

    }
    /**
     * 验证token的有效性
     * @param authToken token
     * @return 验证结果
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            return false;
        } catch (MalformedJwtException e) {
            return false;
        } catch (ExpiredJwtException e) {
            return  false;
        } catch (UnsupportedJwtException e) {
            throw  e;
        } catch (IllegalArgumentException e) {
            throw  e;
        }
    }
}
