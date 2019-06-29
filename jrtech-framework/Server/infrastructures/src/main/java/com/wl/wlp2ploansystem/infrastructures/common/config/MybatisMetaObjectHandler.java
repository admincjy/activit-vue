package com.wl.wlp2ploansystem.infrastructures.common.config;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import com.wl.wlp2ploansystem.infrastructures.common.security.DomainUserDetails;
import com.wl.wlp2ploansystem.infrastructures.common.security.SecurityUtils;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
* Mybatis plus MetaObjectHandler 配置
* */
public class MybatisMetaObjectHandler extends MetaObjectHandler {


	/**
    * 在数据库插入时设置gmtCreatedOn（创建人），gmtCreatedOn（创建时间），gmtUpdatedOn（最后修改时间）
    * */
	public void insertFill(MetaObject metaObject) {

		Object gmtCreatedOnValue = getFieldValByName("gmtCreatedOn", metaObject);

		if (gmtCreatedOnValue == null) {
			setFieldValByName("gmtCreatedOn", LocalDateTime.now(), metaObject);
		}
		if (metaObject.hasSetter("gmtUpdatedOn")) {
			setFieldValByName("gmtUpdatedOn", LocalDateTime.now(), metaObject);
		}
	 DomainUserDetails currUser =  SecurityUtils.getCurrentUser();

	   if(currUser !=null) {
		 Object gmtCreatedByValue = getFieldValByName("gmtCreatedBy", metaObject);
		 if (gmtCreatedByValue == null) {
			 setFieldValByName("gmtCreatedBy", currUser.getId(), metaObject);
		 }
		 if (metaObject.hasSetter("gmtUpdatedBy")) {
			 setFieldValByName("gmtUpdatedBy", currUser.getId(), metaObject);
		 }

	 }
	}

	/**
* 在数据库更新时设置gmtUpdatedBy（最后修改人），gmtUpdatedOn（最后修改时间）
* */
	@Override
	public void updateFill(MetaObject metaObject) {

		if (metaObject.hasSetter("gmtUpdatedOn")) {
			setFieldValByName("gmtUpdatedOn", LocalDateTime.now(), metaObject);
		}

		DomainUserDetails currUser =  SecurityUtils.getCurrentUser();
		if(currUser !=null) {
			if (metaObject.hasSetter("gmtUpdatedBy")) {
				setFieldValByName("gmtUpdatedBy", currUser.getId(), metaObject);
			}
		}
	}
}
