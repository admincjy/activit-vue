package com.wl.wlp2ploansystem.infrastructures.common.config;


import com.wl.wlp2ploansystem.infrastructures.common.security.MyAuthenticationEntryPoint;
import com.wl.wlp2ploansystem.infrastructures.common.security.jwt.JWTConfigurer;
import com.wl.wlp2ploansystem.infrastructures.common.security.jwt.TokenProvider;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.PostConstruct;

/**
 * Spring security配置
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Order(2147483640)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


	private final AuthenticationManagerBuilder authenticationManagerBuilder;

	private final UserDetailsService userDetailsService;

	private final TokenProvider tokenProvider;

	private final CorsFilter corsFilter;

	public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder, UserDetailsService userDetailsService,
								 TokenProvider tokenProvider,
								 CorsFilter corsFilter) {

		this.authenticationManagerBuilder = authenticationManagerBuilder;
		this.userDetailsService = userDetailsService;
		this.tokenProvider = tokenProvider;
		this.corsFilter = corsFilter;
	}

	@PostConstruct
	public void init() {
		try {
			authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		} catch (Exception e) {
			throw new BeanInitializationException("Security configuration failed", e);
		}
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				.antMatchers("/pmtapi/**").permitAll()
				.antMatchers("/authapi/**").authenticated()
				.antMatchers("/kaptcha/**").permitAll()
				.antMatchers("/jsscript/**").permitAll()
				.antMatchers("/wf/**").permitAll()
				.antMatchers("/modeler.html").hasAnyAuthority("menu_wf_workflowdef")
				.antMatchers("/editor-app/**").permitAll()
				.antMatchers("/editor/**").permitAll()
				.antMatchers("/process-instance/**").permitAll()
				.antMatchers("/process-definition/**").permitAll()
				.antMatchers("/model/**").permitAll()
				.antMatchers("/css/**").permitAll()
				.antMatchers("/js/**").permitAll()
				.antMatchers("/images/**").permitAll()
				.antMatchers("/favicon.ico").permitAll()
				.antMatchers("/error").permitAll()
				//.antMatchers("/druid/**").permitAll()

				.antMatchers("/swagger-ui.html").hasAnyAuthority("menu_base_apidoc")
				.antMatchers("/druid/**").hasAnyAuthority("menu_base_dbdictionary")

				.antMatchers("/webjars/**").permitAll()
				.antMatchers("/swagger-resources/**").permitAll()
				.antMatchers("/v2/**").permitAll()
				// 其他地址的访问均需验证权限（需要登录）
				.anyRequest().authenticated()
				.and()
				.addFilterAt(corsFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling().authenticationEntryPoint(new MyAuthenticationEntryPoint())
				.and()
				.csrf()
				.disable()
				.headers()
				.frameOptions()
				.disable()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
				.and()
				.apply(securityConfigurerAdapter());

	}

	private JWTConfigurer securityConfigurerAdapter() {
		return new JWTConfigurer(tokenProvider);
	}
}