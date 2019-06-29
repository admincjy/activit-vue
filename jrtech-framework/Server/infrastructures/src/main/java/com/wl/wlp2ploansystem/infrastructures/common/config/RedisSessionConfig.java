package com.wl.wlp2ploansystem.infrastructures.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Http Session 配置,
 * */
@Configuration
@EnableRedisHttpSession(redisNamespace = "")
public class RedisSessionConfig {
}