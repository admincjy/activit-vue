package com.wl.wlp2ploansystem.infrastructures.common.config;

import com.baomidou.mybatisplus.mapper.ISqlInjector;
import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
import com.baomidou.mybatisplus.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* Mybatis plus 配置
* */
@Configuration
@MapperScan(basePackages="com.wl.wlp2ploansystem.**.repositories*")
public class MybatisPlusConfiguration {
	/**
	 * 性能分析拦截器，用于输出每条 SQL 语句及其执行时间
	 */
	@Bean
	public PerformanceInterceptor2 performanceInterceptor() {
		return new PerformanceInterceptor2();
	}


	/**
	*乐观锁配置
	* */
	@Bean
	public OptimisticLockerInterceptor optimisticLockerInterceptor() {
		return new OptimisticLockerInterceptor();
	}

	/**
	* Mybatis plus MetaObjectHandler 配置
	* */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		// paginationInterceptor.setLocalPage(true);// 开启 PageHelper 的支持
		return paginationInterceptor;
	}

	/**
	 *
	 * 分页配置
	 */
	@Bean
	public MybatisMetaObjectHandler myMetaObjectHandler() {
		MybatisMetaObjectHandler myMetaObjectHandler = new MybatisMetaObjectHandler();
		return myMetaObjectHandler;
	}
	/**
	*逻辑删除
	* */
	@Bean
	public ISqlInjector sqlInjector(){
		return new LogicSqlInjector();
	}
	
}
