package com.wl.wlp2ploansystem.infrastructures.common.support;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 实体验证工具类
 */
public class ValidationHelper {

    /**
     * 实体验证
     */
    public static List<String> validate(Object obj) {
        List<String> result = new ArrayList<>() ;

        if (null == obj) {
            result.add("入参vo不能为空");
        }
        Set<ConstraintViolation<Object>> validResult = Validation.buildDefaultValidatorFactory().getValidator().validate(obj);
        if (null != validResult && validResult.size() > 0) {
            for (Iterator<ConstraintViolation<Object>> iterator = validResult.iterator(); iterator.hasNext();) {
                ConstraintViolation<Object> constraintViolation = (ConstraintViolation<Object>) iterator.next();
                if(StringUtils.isNotBlank(constraintViolation.getMessage())) {
                     result.add(constraintViolation.getMessage());
                } else {
                    result.add(constraintViolation.getPropertyPath().toString() + "不合法");
                }
            }
        }
        return result;
    }
}
