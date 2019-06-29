package com.wl.wlp2ploansystem.infrastructures.common.config;

import com.baomidou.mybatisplus.entity.TableInfo;
import com.baomidou.mybatisplus.mapper.AutoSqlInjector;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;

/**
 * Mybatis plus AutoSqlInjector配置,
 * */
public class MybatisSqlInjector extends AutoSqlInjector {


	@Override
	public void inject(Configuration configuration, MapperBuilderAssistant builderAssistant, Class<?> mapperClass,
			Class<?> modelClass, TableInfo table) {

	}

}
